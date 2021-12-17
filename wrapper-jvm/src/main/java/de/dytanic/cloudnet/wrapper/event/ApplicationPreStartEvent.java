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

package de.dytanic.cloudnet.wrapper.event;

import de.dytanic.cloudnet.driver.event.events.DriverEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * This event is only interesting for wrapper modules. It is called before the actual program is started in a new
 * thread.
 *
 * @see DriverEvent
 */
public final class ApplicationPreStartEvent extends DriverEvent {

  /**
   * The current singleton instance of the Wrapper class
   *
   * @see Wrapper
   */
  private final Wrapper cloudNetWrapper;

  /**
   * The class, which is set in the manifest as 'Main-Class' by the archive of the wrapped application
   */
  private final Class<?> applicationMainClass;
  /**
   * The arguments for the main method of the application
   */
  private final Collection<String> arguments;

  public ApplicationPreStartEvent(
    @NotNull Wrapper cloudNetWrapper,
    @NotNull Class<?> applicationMainClass,
    @NotNull Collection<String> arguments
  ) {
    this.cloudNetWrapper = cloudNetWrapper;
    this.applicationMainClass = applicationMainClass;
    this.arguments = arguments;
  }

  public @NotNull Wrapper wrapper() {
    return this.cloudNetWrapper;
  }

  public @NotNull Class<?> clazz() {
    return this.applicationMainClass;
  }

  public @NotNull Collection<String> arguments() {
    return this.arguments;
  }
}
