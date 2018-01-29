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

}
