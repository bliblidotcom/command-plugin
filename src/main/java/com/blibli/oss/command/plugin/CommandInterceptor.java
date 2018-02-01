package com.blibli.oss.command.plugin;

import com.blibli.oss.command.Command;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
public interface CommandInterceptor {

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
  default <R, T> T beforeExecute(Command<R, T> command, R request) {
    return null;
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
  default <R, T> void afterSuccessExecute(Command<R, T> command, R request, T response) {
    // DO NOTHING
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
  default <R, T> void afterFailedExecute(Command<R, T> command, R request, Throwable throwable) {
    // DO NOTHING
  }

}
