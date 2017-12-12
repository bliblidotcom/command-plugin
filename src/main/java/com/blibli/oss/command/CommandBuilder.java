package com.blibli.oss.command;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface CommandBuilder<R, T> {

  R getRequest();

  Class<? extends Command<R, T>> getCommandClass();

}
