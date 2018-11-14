package com.blibli.oss.command.plugin;

import com.blibli.oss.command.Command;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
public interface CommandInterceptor extends Ordered {

  /**
   * Invoked before command executed, if return <code>null</code>,
   * command will be executed, if <code>not null</code>
   * the response will be returned without execute command
   *
   * @param command command
   * @param request command request
   * @param <R>     request type
   * @param <T>     response type
   * @return response or null
   */
  default <R, T> Mono<T> beforeExecute(Command<R, T> command, R request) {
    return Mono.empty();
  }

  /**
   * Invoked after command success executed
   *
   * @param command  command
   * @param request  command request
   * @param response command response
   * @param <R>      request type
   * @param <T>      response type
   */
  default <R, T> Mono<Void> afterSuccessExecute(Command<R, T> command, R request, T response) {
    return Mono.empty();
  }

  /**
   * Invoked after command failed executed
   *
   * @param command   command
   * @param request   command request
   * @param throwable error
   * @param <R>       request type
   * @param <T>       response type
   */
  default <R, T> Mono<Void> afterFailedExecute(Command<R, T> command, R request, Throwable throwable) {
    return Mono.empty();
  }

  /**
   * Get the order value of this object.
   *
   * @return default is 0
   */
  @Override
  default int getOrder() {
    return 0;
  }
}
