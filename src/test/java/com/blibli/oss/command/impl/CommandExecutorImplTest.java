package com.blibli.oss.command.impl;

import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheInterceptor;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.exception.CommandValidationException;
import com.blibli.oss.command.plugin.CommandInterceptor;
import com.blibli.oss.command.plugin.impl.CommandGroupStrategyImpl;
import com.blibli.oss.command.plugin.impl.CommandKeyStrategyImpl;
import com.blibli.oss.command.properties.CommandProperties;
import com.blibli.oss.command.Command;
import com.blibli.oss.command.tuple.Tuple2;
import com.blibli.oss.command.tuple.Tuple3;
import com.blibli.oss.command.tuple.Tuple4;
import com.blibli.oss.command.tuple.Tuple5;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

  @Mock
  private CommandCache commandCache;

  @Mock
  private CommandCacheMapper commandCacheMapper;

  private Validator validator;

  private CommandGroupStrategyImpl commandGroupStrategy;

  private CommandKeyStrategyImpl commandKeyStrategy;

  private CommandProperties commandProperties;

  private CommandExecutorImpl commandExecutor;

  private DataCommandRequest request;

  private DataCommandImpl dataCommandImpl;

  private CommandCacheInterceptor commandCacheInterceptor;

  private CommandProcessorImpl commandProcessor;

  @Before
  public void setUp() throws Exception {
    setUpValidator();
    setUpCommandProperties();
    setUpCommandGroupStrategy();
    setUpCommandKeyStrategy();
    setUpCommandProcessor();
    setUpDataCommandImpl();
    setUpApplicationContext();
    setUpDataCommand();
    setUpCommandCacheInterceptor();
    setUpCommandExecutor();
    setUpRequest();
    setUpApplicationContext();
    commandProcessor.afterPropertiesSet();
    commandExecutor.setApplicationContext(applicationContext);
  }

  private void setUpRequest() {
    request = DataCommandRequest.builder()
        .name("Name")
        .build();
  }

  private void setUpCommandExecutor() {
    commandExecutor = new CommandExecutorImpl(validator, commandProcessor);
  }

  private void setUpDataCommand() {
    when(dataCommand.execute(anyObject())).thenReturn(Single.just("OK"));
    when(dataCommand.key()).thenReturn("dataKey");
    when(dataCommand.group()).thenReturn("dataGroup");
    when(dataCommand.validateRequest()).thenReturn(true);
  }

  private void setUpApplicationContext() {
    when(applicationContext.getBean(DataCommand.class))
        .thenReturn(dataCommand);
    when(applicationContext.getBean(DataCommandImpl.class))
        .thenReturn(dataCommandImpl);
    Map<String, CommandInterceptor> interceptorMap = Collections.singletonMap("commandCacheInterceptor", commandCacheInterceptor);
    when(applicationContext.getBeansOfType(CommandInterceptor.class)).thenReturn(interceptorMap);
  }

  private void setUpDataCommandImpl() {
    dataCommandImpl = new DataCommandImpl();
  }

  private void setUpCommandProcessor() {
    commandProcessor = new CommandProcessorImpl(commandProperties, commandKeyStrategy, commandGroupStrategy);
    commandProcessor.setApplicationContext(applicationContext);
  }

  private void setUpCommandKeyStrategy() {
    commandKeyStrategy = new CommandKeyStrategyImpl();
    commandKeyStrategy.setApplicationContext(applicationContext);
  }

  private void setUpCommandGroupStrategy() {
    commandGroupStrategy = new CommandGroupStrategyImpl();
  }

  private void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private void setUpCommandProperties() {
    commandProperties = new CommandProperties();
    commandProperties.getHystrix().setEnabled(true);
  }

  private void setUpCommandCacheInterceptor() {
    commandCacheInterceptor = new CommandCacheInterceptor(commandProperties, commandCache, commandCacheMapper);
  }

  @Test(expected = CommandValidationException.class)
  public void testCommandValidationException() throws Exception {
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

  @Test(expected = CommandValidationException.class)
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

  @Test(expected = CommandValidationException.class)
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

  @Test(expected = CommandValidationException.class)
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

  @Test(expected = CommandValidationException.class)
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

  @Test
  public void testError() {
    when(dataCommand.execute(request))
        .thenReturn(Single.error(new NullPointerException()));

    try {
      commandExecutor.execute(DataCommand.class, request).toBlocking().value();
      fail("It should fail");
    } catch (HystrixRuntimeException e) {
      assertTrue(e.getCause() instanceof NullPointerException);
    }
  }

  @Test(expected = NullPointerException.class)
  public void testErrorWithoutHystrix() {
    commandProperties.getHystrix().setEnabled(false);
    when(dataCommand.execute(request))
        .thenReturn(Single.error(new NullPointerException()));

    commandExecutor.execute(DataCommand.class, request).toBlocking().value();
  }

  @Test
  public void testErrorWithFallback() {
    when(dataCommand.execute(request))
        .thenReturn(Single.error(new NullPointerException()));
    when(dataCommand.fallback(any(NullPointerException.class), eq(request)))
        .thenReturn(Single.just("Fallback"));

    String value = commandExecutor.execute(DataCommand.class, request).toBlocking().value();
    assertEquals("Fallback", value);
  }

  @Test
  public void testWithCacheNotFound() {
    commandProperties.getCache().setEnabled(true);
    commandProperties.getHystrix().setEnabled(false);
    when(dataCommand.cacheKey(request))
        .thenReturn("request");
    when(commandCache.get("request"))
        .thenReturn(null);

    String result = commandExecutor.execute(DataCommand.class, request).toBlocking().value();
    assertEquals("OK", result);

    verify(commandCache, times(1))
        .get("request");
  }

  @Test
  public void testWithCacheFound() {
    commandProperties.getCache().setEnabled(true);
    commandProperties.getHystrix().setEnabled(false);
    dataCommandImpl.setResponse("OK");
    dataCommandImpl.setCacheKey("request");

    when(commandCache.get("request"))
        .thenReturn("CACHE");
    when(commandCacheMapper.fromString("CACHE", String.class))
        .thenReturn("CACHE");

    String result = commandExecutor.execute(DataCommandImpl.class, request).toBlocking().value();
    assertEquals("CACHE", result);

    verify(commandCache, times(1))
        .get("request");
    verify(commandCache, times(0))
        .cache(anyString(), anyString());
    verify(commandCache, times(0))
        .evict(anyString());
  }

  @Test
  public void testSuccessAndCacheIt() {
    commandProperties.getCache().setEnabled(true);
    commandProperties.getHystrix().setEnabled(false);
    dataCommandImpl.setResponse("OK");
    dataCommandImpl.setCacheKey("request");

    when(commandCacheMapper.toString("OK")).thenReturn("OK");

    String result = commandExecutor.execute(DataCommandImpl.class, request).toBlocking().value();
    assertEquals("OK", result);

    verify(commandCache, times(1))
        .get("request");
    verify(commandCache, times(1))
        .cache("request", "OK");
    verify(commandCache, times(1))
        .evict("request");
  }

  @Test
  public void testSuccessHystrixAndCacheIt() {
    commandProperties.getCache().setEnabled(true);
    commandProperties.getHystrix().setEnabled(true);
    dataCommandImpl.setResponse("OK");
    dataCommandImpl.setCacheKey("request");

    when(commandCacheMapper.toString("OK")).thenReturn("OK");

    String result = commandExecutor.execute(DataCommandImpl.class, request).toBlocking().value();
    assertEquals("OK", result);

    verify(commandCache, times(1))
        .get("request");
    verify(commandCache, times(1))
        .cache("request", "OK");
    verify(commandCache, times(1))
        .evict("request");
  }

  @Data
  @Builder
  static class DataCommandRequest {

    @NotBlank
    private String name;

  }

  interface DataCommand extends Command<DataCommandRequest, String> {

  }

  class DataCommandImpl implements DataCommand {

    @Setter
    private String response;

    @Setter
    private String cacheKey;

    @Override
    public Single<String> execute(DataCommandRequest request) {
      return Single.just(response);
    }

    @Override
    public String cacheKey(DataCommandRequest request) {
      return cacheKey;
    }

    @Override
    public Class<String> responseClass() {
      return String.class;
    }

    @Override
    public Collection<String> evictKeys(DataCommandRequest request) {
      return Collections.singleton(cacheKey);
    }
  }

}