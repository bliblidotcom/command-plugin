package com.blibli.oss.command.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.CommandBuilder;
import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.cache.CacheUtil;
import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.hystrix.CommandHystrix;
import com.blibli.oss.command.hystrix.CommandHystrixCacheSupport;
import com.blibli.oss.command.plugin.CommandGroupStrategy;
import com.blibli.oss.command.plugin.CommandKeyStrategy;
import com.blibli.oss.command.properties.CommandProperties;
import com.blibli.oss.command.tuple.*;
import com.blibli.oss.common.error.ValidationException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import rx.Single;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandExecutorImpl implements CommandExecutor, ApplicationContextAware {

  private Validator validator;

  private CommandKeyStrategy commandKeyStrategy;

  private CommandGroupStrategy commandGroupStrategy;

  private CommandProperties commandProperties;

  private ApplicationContext applicationContext;

  private CommandCache commandCache;

  private CommandCacheMapper commandCacheMapper;

  public CommandExecutorImpl(Validator validator,
                             CommandKeyStrategy commandKeyStrategy,
                             CommandGroupStrategy commandGroupStrategy,
                             CommandProperties commandProperties,
                             CommandCache commandCache,
                             CommandCacheMapper commandCacheMapper) {
    this.validator = validator;
    this.commandKeyStrategy = commandKeyStrategy;
    this.commandGroupStrategy = commandGroupStrategy;
    this.commandProperties = commandProperties;
    this.commandCache = commandCache;
    this.commandCacheMapper = commandCacheMapper;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public <R, T> CommandBuilder<R, T> build(Class<? extends Command<R, T>> commandClass, R request) {
    return new CommandBuilderImpl<>(request, commandClass);
  }

  @Override
  public <R, T> Single<T> execute(Class<? extends Command<R, T>> commandClass, R request) {
    return validateRequest(request)
        .flatMap(validRequest -> doExecute(commandClass, validRequest));
  }

  @Override
  public <R1, T1, R2, T2> Single<Tuple2<T1, T2>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2
  ) {
    return Single.zip(
        validateRequest(commandBuilder1.getRequest()),
        validateRequest(commandBuilder2.getRequest()),
        Tuple::of
    ).flatMap(tuple ->
        Single.zip(
            doExecute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            doExecute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            Tuple2::new
        )
    );
  }

  @Override
  public <R1, T1, R2, T2, R3, T3> Single<Tuple3<T1, T2, T3>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2,
      CommandBuilder<R3, T3> commandBuilder3
  ) {
    return Single.zip(
        validateRequest(commandBuilder1.getRequest()),
        validateRequest(commandBuilder2.getRequest()),
        validateRequest(commandBuilder3.getRequest()),
        Tuple::of
    ).flatMap(tuple ->
        Single.zip(
            doExecute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            doExecute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            doExecute(commandBuilder3.getCommandClass(), tuple.getThird()),
            Tuple3::new
        )
    );
  }

  @Override
  public <R1, T1, R2, T2, R3, T3, R4, T4> Single<Tuple4<T1, T2, T3, T4>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2,
      CommandBuilder<R3, T3> commandBuilder3,
      CommandBuilder<R4, T4> commandBuilder4
  ) {
    return Single.zip(
        validateRequest(commandBuilder1.getRequest()),
        validateRequest(commandBuilder2.getRequest()),
        validateRequest(commandBuilder3.getRequest()),
        validateRequest(commandBuilder4.getRequest()),
        Tuple::of
    ).flatMap(tuple ->
        Single.zip(
            doExecute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            doExecute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            doExecute(commandBuilder3.getCommandClass(), tuple.getThird()),
            doExecute(commandBuilder4.getCommandClass(), tuple.getForth()),
            Tuple4::new
        )
    );
  }

  @Override
  public <R1, T1, R2, T2, R3, T3, R4, T4, R5, T5> Single<Tuple5<T1, T2, T3, T4, T5>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2,
      CommandBuilder<R3, T3> commandBuilder3,
      CommandBuilder<R4, T4> commandBuilder4,
      CommandBuilder<R5, T5> commandBuilder5
  ) {
    return Single.zip(
        validateRequest(commandBuilder1.getRequest()),
        validateRequest(commandBuilder2.getRequest()),
        validateRequest(commandBuilder3.getRequest()),
        validateRequest(commandBuilder4.getRequest()),
        validateRequest(commandBuilder5.getRequest()),
        Tuple::of
    ).flatMap(tuple ->
        Single.zip(
            doExecute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            doExecute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            doExecute(commandBuilder3.getCommandClass(), tuple.getThird()),
            doExecute(commandBuilder4.getCommandClass(), tuple.getForth()),
            doExecute(commandBuilder5.getCommandClass(), tuple.getFifth()),
            Tuple5::new
        )
    );
  }

  private <R> Single<R> validateRequest(R request) {
    return Single.create(singleSubscriber -> {
      try {
        validateAndThrownIfInvalid(request);
        singleSubscriber.onSuccess(request);
      } catch (Throwable throwable) {
        singleSubscriber.onError(throwable);
      }
    });
  }

  private <R> void validateAndThrownIfInvalid(R request)
      throws ValidationException {
    Set<ConstraintViolation<R>> constraintViolations = validator.validate(request);
    if (!constraintViolations.isEmpty()) {
      throw new ValidationException(constraintViolations);
    }
  }

  private <R, T> Single<T> doExecute(Class<? extends Command<R, T>> commandClass, R request) {
    Command<R, T> command = applicationContext.getBean(commandClass);
    T response = getResponseFromCache(command, request);
    if (response == null) {
      return doExecuteCommand(commandClass, request, command);
    } else {
      return Single.just(response);
    }
  }

  private <R, T> T getResponseFromCache(Command<R, T> command, R request) {
    if (!commandProperties.getCache().isEnabled()) {
      return null;
    }

    String cacheKey = command.cacheKey(request);
    if (cacheKey == null) {
      return null;
    }

    String result = commandCache.get(cacheKey);
    if (result == null) {
      return null;
    }

    Class<T> responseClass = command.responseClass();
    return commandCacheMapper.fromString(result, responseClass);
  }

  private <R, T> Single<T> doExecuteCommand(Class<? extends Command<R, T>> commandClass, R request, Command<R, T> command) {
    if (commandProperties.getHystrix().isEnabled()) {
      return doExecuteWithHystrix(commandClass, request, command);
    } else {
      return doExecuteWithoutHystrix(commandClass, request, command);
    }
  }

  private <R, T> Single<T> doExecuteWithoutHystrix(Class<? extends Command<R, T>> commandClass, R request, Command<R, T> command) {
    return command.execute(request)
        .doOnSuccess(response -> {
          CacheUtil.evictCommandResponse(commandCache, command, request);
          CacheUtil.cacheCommandResponse(commandCache, commandCacheMapper, command, request, response);
        })
        .onErrorResumeNext(throwable -> command.fallback(throwable, request));
  }

  private <R, T> Single<T> doExecuteWithHystrix(Class<? extends Command<R, T>> commandClass, R request, Command<R, T> command) {
    String commandKey = commandKeyStrategy.getCommandKey(command);
    String commandGroup = commandGroupStrategy.getCommandGroup(command);

    if (commandProperties.getCache().isEnabled()) {
      return new CommandHystrixCacheSupport<>(
          command, request, commandKey,
          commandGroup, commandCache, commandCacheMapper
      ).toObservable().toSingle();
    } else {
      return new CommandHystrix<>(
          command, request, commandKey, commandGroup
      ).toObservable().toSingle();
    }
  }
}
