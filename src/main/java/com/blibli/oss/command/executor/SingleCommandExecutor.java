/*
 * Copyright 2018 the original author or authors.
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

package com.blibli.oss.command.executor;

import com.blibli.oss.command.Command;
import rx.Single;

/**
 * Executor for one command
 *
 * @author Eko Kurniawan Khannedy
 */
public interface SingleCommandExecutor {

  /**
   * Execute single command
   *
   * @param commandClass command class
   * @param request      command request
   * @param <R>          request type
   * @param <T>          result type
   * @return single of result
   */
  <R, T> Single<T> execute(Class<? extends Command<R, T>> commandClass, R request);
}
