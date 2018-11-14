package com.blibli.oss.command;

import com.blibli.oss.command.cache.CommandCacheable;
import com.blibli.oss.command.helper.CommandHelper;
import reactor.core.publisher.Mono;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface Command<R, T> extends CommandCacheable<R, T>, CommandHelper {

  /**
   * Command logic implementation
   *
   * @param request command request
   * @return command response in Single
   */
  Mono<T> execute(R request);

  /**
   * If {@link #execute(Object)} produce error,
   * this method will executed as fallback method
   *
   * @param throwable error from {@link #execute(Object)}
   * @param request   command request
   * @return fallback command response
   */
  default Mono<T> fallback(Throwable throwable, R request) {
    return Mono.error(throwable);
  }

  /**
   * Is request need to be validated before execute command
   *
   * @return true if need, false if not need
   */
  default boolean validateRequest() {
    return true;
  }

}
