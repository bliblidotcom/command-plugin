package com.blibli.oss.command.hystrix;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.plugin.CommandInterceptor;
import com.blibli.oss.command.plugin.InterceptorUtil;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Single;

import java.util.Collection;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public class CommandHystrix<R, T> extends HystrixObservableCommand<T> {

  protected Collection<CommandInterceptor> commandInterceptors;

  protected Command<R, T> command;

  protected R request;

  public CommandHystrix(Command<R, T> command, R request, String commandKey, String commandGroup,
                        Collection<CommandInterceptor> commandInterceptors) {
    super(Setter
        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandGroup))
        .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
    );

    this.command = command;
    this.request = request;
    this.commandInterceptors = commandInterceptors;
  }

  @Override
  protected Observable<T> construct() {
    return command.execute(request)
        .doOnSuccess(response -> InterceptorUtil.afterSuccessExecute(commandInterceptors, command, request, response))
        .toObservable();
  }

  @Override
  protected Observable<T> resumeWithFallback() {
    return Single.just(getExecutionException())
        .doOnSuccess(throwable -> InterceptorUtil.afterFailedExecute(commandInterceptors, command, request, throwable))
        .flatMap(throwable -> command.fallback(throwable, request))
        .toObservable();
  }
}
