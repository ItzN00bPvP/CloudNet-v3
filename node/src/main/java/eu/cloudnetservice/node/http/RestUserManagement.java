/*
 * Copyright 2019-2023 CloudNetService team & contributors
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

package eu.cloudnetservice.node.http;

import java.util.regex.Pattern;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface RestUserManagement {

  String SCOPE_NAMING_REGEX = "(^[a-z][a-z0-9_]{4,39}):([a-z0-9.\\-_]+)";

  // https://regex101.com/r/3nG0Nu/1
  Pattern SCOPE_NAMING_PATTERN = Pattern.compile(SCOPE_NAMING_REGEX);

  @Nullable RestUser restUser(@NonNull String id);

  void saveRestUser(@NonNull RestUser user);

  boolean deleteRestUser(@NonNull RestUser user);

  @NonNull RestUser.Builder builder();

  @NonNull RestUser.Builder builder(@NonNull RestUser restUser);

}
