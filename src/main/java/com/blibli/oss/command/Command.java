package com.blibli.oss.command;

import rx.Single;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface Command<R, T> {

  /**
   * Command logic implementation
   *
   * @param request command request
   * @return command response in Single
   */
  Single<T> execute(R request);

  /**
   * If {@link #execute(Object)} produce error,
   * this method will executed as fallback method
   *
   * @param throwable error from {@link #execute(Object)}
   * @param request   command request
   * @return fallback command response
   */
  default Single<T> fallback(Throwable throwable, R request) {
    return Single.error(throwable);
  }

  /**
   * Get command key
   *
   * @return command key
   */
  default String key() {
    return null;
  }

  /**
   * Get group name
   *
   * @return group name
   */
  default String group() {
    return null;
  }

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
   * Get evict key, if <code>null</code> this command will not trigger evict in cache
   *
   * @param request command request
   * @return evict key
   */
  default String evictKey(R request) {
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
