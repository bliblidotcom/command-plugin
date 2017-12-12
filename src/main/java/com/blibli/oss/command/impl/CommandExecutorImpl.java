package com.blibli.oss.command.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.CommandBuilder;
import com.blibli.oss.command.CommandExecutor;
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

  private ApplicationContext applicationContext;

  public CommandExecutorImpl(Validator validator) {
    this.validator = validator;
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
    Command<R, T> command = applicationContext.getBean(commandClass);
    return validateRequest(request)
        .flatMap(command::execute);
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
            execute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            execute(commandBuilder2.getCommandClass(), tuple.getSecond()),
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
            execute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            execute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            execute(commandBuilder3.getCommandClass(), tuple.getThird()),
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
            execute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            execute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            execute(commandBuilder3.getCommandClass(), tuple.getThird()),
            execute(commandBuilder4.getCommandClass(), tuple.getForth()),
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
            execute(commandBuilder1.getCommandClass(), tuple.getFirst()),
            execute(commandBuilder2.getCommandClass(), tuple.getSecond()),
            execute(commandBuilder3.getCommandClass(), tuple.getThird()),
            execute(commandBuilder4.getCommandClass(), tuple.getForth()),
            execute(commandBuilder5.getCommandClass(), tuple.getFifth()),
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
}
