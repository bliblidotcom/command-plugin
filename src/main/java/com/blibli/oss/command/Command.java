package com.blibli.oss.command;

import rx.Single;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface Command<R, T> {

  Single<T> execute(R request);

}
