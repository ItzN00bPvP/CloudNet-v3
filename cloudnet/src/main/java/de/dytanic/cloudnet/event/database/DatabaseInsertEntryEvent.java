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

package de.dytanic.cloudnet.event.database;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.database.Database;
import org.jetbrains.annotations.NotNull;

public class DatabaseInsertEntryEvent extends DatabaseEvent {

  private final String key;
  private final JsonDocument document;

  public DatabaseInsertEntryEvent(@NotNull Database database, @NotNull String key, @NotNull JsonDocument document) {
    super(database);

    this.key = key;
    this.document = document;
  }

  public @NotNull String getKey() {
    return this.key;
  }

  public @NotNull JsonDocument getDocument() {
    return this.document;
  }
}
