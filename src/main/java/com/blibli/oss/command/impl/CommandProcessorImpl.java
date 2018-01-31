package com.blibli.oss.command.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.CommandProcessor;
import com.blibli.oss.command.hystrix.CommandHystrix;
import com.blibli.oss.command.plugin.CommandGroupStrategy;
import com.blibli.oss.command.plugin.CommandInterceptor;
import com.blibli.oss.command.plugin.CommandKeyStrategy;
import com.blibli.oss.command.plugin.InterceptorUtil;
import com.blibli.oss.command.properties.CommandProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import rx.Single;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
public class CommandProcessorImpl implements CommandProcessor, ApplicationContextAware, InitializingBean {

  private CommandProperties commandProperties;

  private CommandKeyStrategy commandKeyStrategy;

  private CommandGroupStrategy commandGroupStrategy;

  private ApplicationContext applicationContext;

  private Collection<CommandInterceptor> commandInterceptors = Collections.emptyList();

  public CommandProcessorImpl(CommandProperties commandProperties, CommandKeyStrategy commandKeyStrategy, CommandGroupStrategy commandGroupStrategy) {
    this.commandProperties = commandProperties;
    this.commandKeyStrategy = commandKeyStrategy;
    this.commandGroupStrategy = commandGroupStrategy;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    loadCommandInterceptors();
  }

  private void loadCommandInterceptors() {
    Map<String, CommandInterceptor> interceptorMap = applicationContext.getBeansOfType(CommandInterceptor.class);
    if (interceptorMap != null && !interceptorMap.isEmpty()) {
      commandInterceptors = interceptorMap.values();
    }
  }

  @Override
  public <R, T> Single<T> doExecute(Class<? extends Command<R, T>> commandClass, R request) {
    Command<R, T> command = applicationContext.getBean(commandClass);

    return Single.<T>create(singleSubscriber -> {
      T result = InterceptorUtil.beforeExecute(commandInterceptors, command, request);
      singleSubscriber.onSuccess(result);
    }).flatMap(result -> {
      if (result != null) {
        return Single.just(result);
      } else {
        return doExecuteCommand(request, command);
      }
    });
  }

  private <R, T> Single<T> doExecuteCommand(R request, Command<R, T> command) {
    if (commandProperties.getHystrix().isEnabled()) {
      return doExecuteWithHystrix(request, command);
    } else {
      return doExecuteWithoutHystrix(request, command);
    }
  }

  private <R, T> Single<T> doExecuteWithoutHystrix(R request, Command<R, T> command) {
    return command.execute(request)
        .doOnSuccess(response -> InterceptorUtil.afterSuccessExecute(commandInterceptors, command, request, response))
        .doOnError(throwable -> InterceptorUtil.afterFailedExecute(commandInterceptors, command, request, throwable))
        .onErrorResumeNext(throwable -> command.fallback(throwable, request));
  }

  private <R, T> Single<T> doExecuteWithHystrix(R request, Command<R, T> command) {
    String commandKey = commandKeyStrategy.getCommandKey(command);
    String commandGroup = commandGroupStrategy.getCommandGroup(command);

    return new CommandHystrix<>(command, request, commandKey, commandGroup, commandInterceptors)
        .toObservable().toSingle();
  }
}
