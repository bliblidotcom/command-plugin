package com.blibli.oss.command.impl;

import com.blibli.oss.common.error.ValidationException;
import com.blibli.oss.command.Command;
import com.blibli.oss.command.tuple.Tuple2;
import com.blibli.oss.command.tuple.Tuple3;
import com.blibli.oss.command.tuple.Tuple4;
import com.blibli.oss.command.tuple.Tuple5;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;
import rx.Single;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandExecutorImplTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private DataCommand dataCommand;

  private Validator validator;

  private CommandExecutorImpl commandExecutor;

  private DataCommandRequest request;

  @Before
  public void setUp() throws Exception {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    when(applicationContext.getBean(DataCommand.class))
        .thenReturn(dataCommand);

    when(dataCommand.execute(anyObject()))
        .thenReturn(Single.just("OK"));

    commandExecutor = new CommandExecutorImpl(validator);
    commandExecutor.setApplicationContext(applicationContext);

    request = DataCommandRequest.builder()
        .name("Name")
        .build();
  }

  @Test(expected = ValidationException.class)
  public void testValidationException() throws Exception {
    request.setName(""); // blank
    commandExecutor.execute(DataCommand.class, request).toBlocking().value();
  }

  @Test
  public void testSuccess() throws Exception {
    String result = commandExecutor.execute(DataCommand.class, request).toBlocking().value();
    assertEquals("OK", result);
  }

  @Test
  public void testExecuteAll2() {
    Tuple2<String, String> value = commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    assertEquals("OK", value.getFirst());
    assertEquals("OK", value.getSecond());
  }

  @Test
  public void testExecuteAll3() {
    Tuple3<String, String, String> value = commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    assertEquals("OK", value.getFirst());
    assertEquals("OK", value.getSecond());
    assertEquals("OK", value.getThird());
  }

  @Test
  public void testExecuteAll4() {
    Tuple4<String, String, String, String> value = commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    assertEquals("OK", value.getFirst());
    assertEquals("OK", value.getSecond());
    assertEquals("OK", value.getThird());
    assertEquals("OK", value.getForth());
  }

  @Test
  public void testExecuteAll5() {
    Tuple5<String, String, String, String, String> value = commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    assertEquals("OK", value.getFirst());
    assertEquals("OK", value.getSecond());
    assertEquals("OK", value.getThird());
    assertEquals("OK", value.getForth());
    assertEquals("OK", value.getFifth());
  }

  @Test(expected = ValidationException.class)
  public void testExecuteAll5Failed() {
    DataCommandRequest requestFailed = DataCommandRequest.builder()
        .name("")
        .build();

    commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, requestFailed),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    fail("It should be failed");
  }

  @Test(expected = ValidationException.class)
  public void testExecuteAll4Failed() {
    DataCommandRequest requestFailed = DataCommandRequest.builder()
        .name("")
        .build();

    commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, requestFailed),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    fail("It should be failed");
  }

  @Test(expected = ValidationException.class)
  public void testExecuteAll3Failed() {
    DataCommandRequest requestFailed = DataCommandRequest.builder()
        .name("")
        .build();

    commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, requestFailed),
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, request)
    ).toBlocking().value();

    fail("It should be failed");
  }

  @Test(expected = ValidationException.class)
  public void testExecuteAll2Failed() {
    DataCommandRequest requestFailed = DataCommandRequest.builder()
        .name("")
        .build();

    commandExecutor.executeAll(
        commandExecutor.build(DataCommand.class, request),
        commandExecutor.build(DataCommand.class, requestFailed)
    ).toBlocking().value();

    fail("It should be failed");
  }

  @Data
  @Builder
  static class DataCommandRequest {

    @NotBlank
    private String name;

  }

  interface DataCommand extends Command<DataCommandRequest, String> {

  }

}