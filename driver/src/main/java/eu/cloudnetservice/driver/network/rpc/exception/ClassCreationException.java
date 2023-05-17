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

package eu.cloudnetservice.driver.network.rpc.exception;

import lombok.NonNull;

/**
 * A runtime exception which gets thrown when an exception occurs during runtime code generation.
 *
 * @since 4.0
 */
public class ClassCreationException extends RuntimeException {

  /**
   * Constructs a new class creation exception instance.
   *
   * @param message the detailed message why the exception occurred.
   * @param cause   the original exception thrown during the class creation.
   * @throws NullPointerException if either the given message or exception is null.
   */
  public ClassCreationException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}