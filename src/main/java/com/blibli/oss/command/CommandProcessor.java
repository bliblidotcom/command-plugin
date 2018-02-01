package com.blibli.oss.command;

import rx.Single;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
public interface CommandProcessor {

  <R, T> Single<T> doExecute(Class<? extends Command<R, T>> commandClass, R request);

}
