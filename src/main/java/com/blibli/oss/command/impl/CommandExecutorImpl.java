package com.blibli.oss.command.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.CommandBuilder;
import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.CommandProcessor;
import com.blibli.oss.command.exception.CommandValidationException;
import com.blibli.oss.command.helper.ErrorHelper;
import com.blibli.oss.command.tuple.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CommandExecutorImpl implements CommandExecutor, ApplicationContextAware {

  private Validator validator;

  private CommandProcessor commandProcessor;

  private ApplicationContext applicationContext;

  public CommandExecutorImpl(Validator validator, CommandProcessor commandProcessor) {
    this.validator = validator;
    this.commandProcessor = commandProcessor;
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
    return validateRequest(request, commandClass)
        .flatMap(validRequest -> doExecute(commandClass, validRequest));
  }

  @Override
  public <R1, T1, R2, T2> Single<Tuple2<T1, T2>> executeAll(
      CommandBuilder<R1, T1> commandBuilder1,
      CommandBuilder<R2, T2> commandBuilder2
  ) {
    return Single.zip(
        validateRequest(commandBuilder1.getRequest(), commandBuilder1.getCommandClass()),
        validateRequest(commandBuilder2.getRequest(), commandBuilder2.getCommandClass()),
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
        validateRequest(commandBuilder1.getRequest(), commandBuilder1.getCommandClass()),
        validateRequest(commandBuilder2.getRequest(), commandBuilder2.getCommandClass()),
        validateRequest(commandBuilder3.getRequest(), commandBuilder3.getCommandClass()),
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
        validateRequest(commandBuilder1.getRequest(), commandBuilder1.getCommandClass()),
        validateRequest(commandBuilder2.getRequest(), commandBuilder2.getCommandClass()),
        validateRequest(commandBuilder3.getRequest(), commandBuilder3.getCommandClass()),
        validateRequest(commandBuilder4.getRequest(), commandBuilder4.getCommandClass()),
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
        validateRequest(commandBuilder1.getRequest(), commandBuilder1.getCommandClass()),
        validateRequest(commandBuilder2.getRequest(), commandBuilder2.getCommandClass()),
        validateRequest(commandBuilder3.getRequest(), commandBuilder3.getCommandClass()),
        validateRequest(commandBuilder4.getRequest(), commandBuilder4.getCommandClass()),
        validateRequest(commandBuilder5.getRequest(), commandBuilder5.getCommandClass()),
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

  @SafeVarargs
  private final <R> Single<R> validateRequest(R request, Class<? extends Command<?, ?>>... classes) {
    return Single.create(singleSubscriber -> {
      try {
        if (isValidateRequest(classes)) {
          log.info("Validate request {}", request.getClass().getName());
          validateAndThrownIfInvalid(request);
        }

        singleSubscriber.onSuccess(request);
      } catch (Throwable throwable) {
        singleSubscriber.onError(throwable);
      }
    });
  }

  @SafeVarargs
  private final boolean isValidateRequest(Class<? extends Command<?, ?>>... classes) {
    if (classes == null) {
      return false;
    }

    for (Class<? extends Command> clazz : classes) {
      Command command = applicationContext.getBean(clazz);
      if (command.validateRequest()) {
        return true;
      }
    }

    return false;
  }

  private <R> void validateAndThrownIfInvalid(R request)
      throws CommandValidationException {
    Set<ConstraintViolation<R>> constraintViolations = validator.validate(request);
    if (!constraintViolations.isEmpty()) {
      String validationMessage = ErrorHelper.from(constraintViolations).toString();
      log.warn("Invalid command request with validation message {}", validationMessage);
      throw new CommandValidationException(validationMessage, constraintViolations);
    }
  }

  private <R, T> Single<T> doExecute(Class<? extends Command<R, T>> commandClass, R request) {
    return commandProcessor.doExecute(commandClass, request);
  }

  @Override
  public <R, T1, T2> Single<Tuple2<T1, T2>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      R request) {
    return validateRequest(request, command1, command2).flatMap(validRequest ->
        Single.zip(
            doExecute(command1, request),
            doExecute(command2, request),
            Tuple::of
        )
    );
  }

  @Override
  public <R, T1, T2, T3> Single<Tuple3<T1, T2, T3>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      Class<? extends Command<R, T3>> command3,
      R request) {
    return validateRequest(request, command1, command2, command3).flatMap(validRequest ->
        Single.zip(
            doExecute(command1, request),
            doExecute(command2, request),
            doExecute(command3, request),
            Tuple::of
        )
    );
  }

  @Override
  public <R, T1, T2, T3, T4> Single<Tuple4<T1, T2, T3, T4>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      Class<? extends Command<R, T3>> command3,
      Class<? extends Command<R, T4>> command4,
      R request) {
    return validateRequest(request, command1, command2, command3, command4).flatMap(validRequest ->
        Single.zip(
            doExecute(command1, request),
            doExecute(command2, request),
            doExecute(command3, request),
            doExecute(command4, request),
            Tuple::of
        )
    );
  }

  @Override
  public <R, T1, T2, T3, T4, T5> Single<Tuple5<T1, T2, T3, T4, T5>> executeAll(
      Class<? extends Command<R, T1>> command1,
      Class<? extends Command<R, T2>> command2,
      Class<? extends Command<R, T3>> command3,
      Class<? extends Command<R, T4>> command4,
      Class<? extends Command<R, T5>> command5,
      R request) {
    return validateRequest(request, command1, command2, command3, command4, command5).flatMap(validRequest ->
        Single.zip(
            doExecute(command1, request),
            doExecute(command2, request),
            doExecute(command3, request),
            doExecute(command4, request),
            doExecute(command5, request),
            Tuple::of
        )
    );
  }
}
