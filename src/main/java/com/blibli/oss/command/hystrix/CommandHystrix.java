package com.blibli.oss.command.hystrix;

import com.blibli.oss.command.Command;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public class CommandHystrix<R, T> extends HystrixObservableCommand<T> {

  private Command<R, T> command;

  private R request;

  public CommandHystrix(Command<R, T> command, R request, String commandKey, String commandGroup) {
    super(Setter
        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandGroup))
        .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
    );

    this.command = command;
    this.request = request;
  }

  @Override
  protected Observable<T> construct() {
    return command.execute(request).toObservable();
  }

  @Override
  protected Observable<T> resumeWithFallback() {
    return command.fallback(getExecutionException(), request).toObservable();
  }
}
