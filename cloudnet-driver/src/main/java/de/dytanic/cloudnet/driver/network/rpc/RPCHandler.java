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

package de.dytanic.cloudnet.driver.network.rpc;

import de.dytanic.cloudnet.common.collection.Pair;
import de.dytanic.cloudnet.driver.network.INetworkChannel;
import de.dytanic.cloudnet.driver.network.buffer.DataBuf;
import de.dytanic.cloudnet.driver.network.rpc.defaults.MethodInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RPCHandler extends RPCProvider {

  @Nullable Object handleRawOn(@Nullable Object instance, @NotNull INetworkChannel channel, @NotNull DataBuf received);

  @Nullable DataBuf handleOn(@Nullable Object instance, @NotNull INetworkChannel channel, @NotNull DataBuf received);

  @Nullable Object handleRaw(@NotNull INetworkChannel channel, @NotNull DataBuf received);

  @Nullable DataBuf handleRPC(@NotNull INetworkChannel channel, @NotNull DataBuf received);

  @NotNull Pair<Object, MethodInformation> handle(@NotNull RPCInvocationContext context);
}