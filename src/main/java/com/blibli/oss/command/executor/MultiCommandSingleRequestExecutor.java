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
import com.blibli.oss.command.tuple.Tuple2;
import com.blibli.oss.command.tuple.Tuple3;
import com.blibli.oss.command.tuple.Tuple4;
import com.blibli.oss.command.tuple.Tuple5;
import rx.Single;

/**
 * Executor for multiple commands but with same request.
 *
 * @author Eko Kurniawan Khannedy
 */
public interface MultiCommandSingleRequestExecutor {

  <R, T1, T2> Single<Tuple2<T1, T2>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      R request
  );

  <R, T1, T2, T3> Single<Tuple3<T1, T2, T3>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      Class<? extends Command<R, T3>> command3,
      R request
  );

  <R, T1, T2, T3, T4> Single<Tuple4<T1, T2, T3, T4>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      Class<? extends Command<R, T3>> command3,
      Class<? extends Command<R, T4>> command4,
      R request
  );

  <R, T1, T2, T3, T4, T5> Single<Tuple5<T1, T2, T3, T4, T5>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      Class<? extends Command<R, T3>> command3,
      Class<? extends Command<R, T4>> command4,
      Class<? extends Command<R, T5>> command5,
      R request
  );
}
