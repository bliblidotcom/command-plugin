package com.blibli.oss.command.exception;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandRuntimeException extends RuntimeException {

  public CommandRuntimeException() {
  }

  public CommandRuntimeException(Throwable cause) {
    super(cause);
  }

  public CommandRuntimeException(String message) {
    super(message);
  }
}
