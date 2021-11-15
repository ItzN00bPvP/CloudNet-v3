/*
 * Copyright 2019-2021 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.dytanic.cloudnet.ext.simplenametags;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.PermissionGroup;
import de.dytanic.cloudnet.driver.permission.PermissionUser;
import de.dytanic.cloudnet.ext.simplenametags.event.PrePlayerPrefixSetEvent;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleNameTagsManager<P> {

  protected static final String TEAM_NAME_FORMAT = "%s%s";

  public SimpleNameTagsManager(@NotNull Executor syncTaskExecutor) {
    CloudNetDriver.getInstance().getEventManager().registerListener(
      new CloudSimpleNameTagsListener<>(syncTaskExecutor, this));
  }

  public void updateNameTagsFor(@NotNull P player, @NotNull UUID playerUniqueId, @NotNull String playerName) {
    // get the permission group of the player
    PermissionGroup group = this.getPermissionGroup(playerUniqueId, player);
    if (group != null) {
      // find the highest sort id length of any known group on this instance
      int maxSortIdLength = CloudNetDriver.getInstance().getPermissionManagement().getGroups().stream()
        .map(PermissionGroup::getSortId)
        .mapToInt(i -> (int) Math.log10(i) + 1)
        .max()
        .orElse(0);
      String groupTeamName = this.selectTeamName(group, maxSortIdLength);
      // reset the scoreboard of the current player
      this.resetScoreboard(player);
      // set the team entries for each player connected to the server
      for (P onlinePlayer : this.getOnlinePlayers()) {
        // reset the scoreboard for the player
        this.resetScoreboard(onlinePlayer);
        // add the player to the score board for this player
        this.registerPlayerToTeam(player, onlinePlayer, groupTeamName, group);

        // get the permission group for the player
        PermissionGroup onlinePlayerGroup = this.getPermissionGroup(this.getPlayerUniqueId(onlinePlayer), onlinePlayer);
        if (onlinePlayerGroup != null) {
          // get the team name of the group
          String onlinePlayerGroupTeamName = this.selectTeamName(onlinePlayerGroup, maxSortIdLength);
          // register the team to the target updated player score board
          this.registerPlayerToTeam(onlinePlayer, player, onlinePlayerGroupTeamName, onlinePlayerGroup);
        }
      }
      // set the players display name
      this.setDisplayName(player, group.getDisplay() + playerName);
    }
  }

  public abstract void updateNameTagsFor(@NotNull P player);

  public abstract @NotNull UUID getPlayerUniqueId(@NotNull P player);

  public abstract void setDisplayName(@NotNull P player, @NotNull String displayName);

  public abstract void resetScoreboard(@NotNull P player);

  public abstract void registerPlayerToTeam(
    @NotNull P player,
    @NotNull P scoreboardHolder,
    @NotNull String name,
    @NotNull PermissionGroup group);

  public abstract @NotNull Collection<? extends P> getOnlinePlayers();

  public abstract @Nullable P getOnlinePlayer(@NotNull UUID uniqueId);

  protected char getColorChar(@NotNull PermissionGroup group) {
    // check if the color of the group is given and valid
    if (group.getColor().length() == 2) {
      // check if the first char is a color indicator
      char indicatorChar = group.getColor().charAt(0);
      if (indicatorChar == '&' || indicatorChar == '§') {
        // the next char should be the color char then
        return group.getColor().charAt(1);
      }
    }
    // search for the last color char in the prefix of the group
    int length = group.getPrefix().length();
    for (int index = length - 2; index >= 0; index--) {
      // check if the current char is a color indicator char
      char atPosition = group.getPrefix().charAt(index);
      if (atPosition == '&' || atPosition == '§') {
        return group.getPrefix().charAt(index + 1);
      }
    }
    // no color char found
    return ' ';
  }

  protected @Nullable PermissionGroup getPermissionGroup(@NotNull UUID playerUniqueId, @NotNull P platformPlayer) {
    // select the best permission group for the player
    PermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(playerUniqueId);
    // no user -> no group
    if (user == null) {
      return null;
    }
    // get the highest group of the player
    PermissionGroup group = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user);
    // no group -> try the default group
    if (group == null) {
      group = CloudNetDriver.getInstance().getPermissionManagement().getDefaultPermissionGroup();
      // no default group -> skip
      if (group == null) {
        return null;
      }
    }
    // post the choose event to let the user modify the permission group of the player (for example to nick a player)
    return CloudNetDriver.getInstance().getEventManager()
      .callEvent(new PrePlayerPrefixSetEvent<>(platformPlayer, group))
      .getGroup();
  }

  protected @NotNull String selectTeamName(@NotNull PermissionGroup group, int highestSortIdLength) {
    // get the length of the group's sort id
    int sortIdLength = (int) Math.log10(group.getSortId()) + 1;
    String teamName = String.format(
      TEAM_NAME_FORMAT,
      highestSortIdLength == sortIdLength
        ? sortIdLength
        : String.format("%0" + highestSortIdLength + "d", group.getSortId()),
      group.getName());
    // shorten the name if needed
    return teamName.length() > 16 ? teamName.substring(0, 16) : teamName;
  }
}