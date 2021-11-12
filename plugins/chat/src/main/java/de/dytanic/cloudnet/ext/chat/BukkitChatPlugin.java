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

package de.dytanic.cloudnet.ext.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitChatPlugin extends JavaPlugin implements Listener {

  private String format;

  @Override
  public void onEnable() {
    this.getConfig().options().copyDefaults(true);
    this.saveConfig();

    this.format = this.getConfig().getString("format");
    this.getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void handleChat(@NotNull AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    String formattedMessage = ChatFormatter.buildFormat(
      player.getUniqueId(),
      player.getName(),
      player.getDisplayName(),
      this.format,
      event.getMessage(),
      player::hasPermission,
      ChatColor::translateAlternateColorCodes);

    if (formattedMessage == null) {
      event.setCancelled(true);
    } else {
      event.setFormat(formattedMessage);
    }
  }
}
