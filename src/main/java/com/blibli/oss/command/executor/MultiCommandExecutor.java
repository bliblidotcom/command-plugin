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
import com.blibli.oss.command.CommandBuilder;
import com.blibli.oss.command.tuple.Tuple2;
import com.blibli.oss.command.tuple.Tuple3;
import com.blibli.oss.command.tuple.Tuple4;
import com.blibli.oss.command.tuple.Tuple5;
import rx.Single;

/**
 * Executor for multiple commands with different request for each command.
 *
 * @author Eko Kurniawan Khannedy
 */
public interface MultiCommandExecutor {

  <R, T> CommandBuilder<R, T> build(Class<? extends Command<R, T>> commandClass, R request);

  <R1, T1, R2, T2> Single<Tuple2<T1, T2>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2
  );

  <R1, T1, R2, T2, R3, T3> Single<Tuple3<T1, T2, T3>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2,
      CommandBuilder<R3, T3> commandBuilder3
  );

  <R1, T1, R2, T2, R3, T3, R4, T4> Single<Tuple4<T1, T2, T3, T4>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2,
      CommandBuilder<R3, T3> commandBuilder3,
      CommandBuilder<R4, T4> commandBuilder4
  );

  <R1, T1, R2, T2, R3, T3, R4, T4, R5, T5> Single<Tuple5<T1, T2, T3, T4, T5>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2,
      CommandBuilder<R3, T3> commandBuilder3,
      CommandBuilder<R4, T4> commandBuilder4,
      CommandBuilder<R5, T5> commandBuilder5
  );
}
