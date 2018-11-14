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

package com.blibli.oss.command.cache;

import java.util.Collection;

public interface CommandCacheable<R, T> {

  /**
   * Get cache key, if <code>null</code> this command result will not be cached
   *
   * @param request command request
   * @return cache key
   */
  default String cacheKey(R request) {
    return null;
  }

  /**
   * Get evict keys, if <code>null</code> this command will not trigger evict in cache
   *
   * @param request command request
   * @return evict keys
   */
  default Collection<String> evictKeys(R request) {
    return null;
  }

  /**
   * Get return class for cache serialization
   *
   * @return response class
   */
  default Class<T> responseClass() {
    throw new UnsupportedOperationException("No response class available.");
  }
}
