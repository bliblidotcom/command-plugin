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

package com.blibli.oss.command.helper;

import rx.Observable;
import rx.Single;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Eko Kurniawan Khannedy
 */
public class RxHelper {

  public static <T> Single<T> single(Supplier<T> supplier) {
    return Single.create(singleSubscriber -> {
      try {
        singleSubscriber.onSuccess(supplier.get());
      } catch (Throwable throwable) {
        singleSubscriber.onError(throwable);
      }
    });
  }

  public static <T> Observable<T> observable(Supplier<Stream<T>> supplier) {
    return Observable.create(subscriber -> {
      try {
        supplier.get().forEach(subscriber::onNext);
        subscriber.onCompleted();
      } catch (Throwable throwable) {
        subscriber.onError(throwable);
      }
    });
  }

}
