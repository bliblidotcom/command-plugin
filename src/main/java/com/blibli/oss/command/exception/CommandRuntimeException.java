package com.blibli.oss.command.exception;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandRuntimeException extends RuntimeException {

  public CommandRuntimeException() {
  }

  public CommandRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommandRuntimeException(Throwable cause) {
    super(cause);
  }
}
