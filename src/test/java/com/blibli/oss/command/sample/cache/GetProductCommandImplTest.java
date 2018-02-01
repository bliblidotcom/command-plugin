package com.blibli.oss.command.sample.cache;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.properties.CommandProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GetProductCommandImplTest.Application.class)
public class GetProductCommandImplTest {

  @MockBean
  private StringRedisTemplate stringRedisTemplate;

  @MockBean
  private ValueOperations<String, String> valueOperations;

  @Autowired
  private CommandProperties commandProperties;

  @Autowired
  private CommandExecutor commandExecutor;

  @Autowired
  private CommandCache commandCache;

  @Autowired
  private CommandCacheMapper commandCacheMapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setUp() throws Exception {
    when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  public void testExecuteCommand() {
    commandProperties.getCache().setEnabled(true);

    GetProductCommandRequest request = GetProductCommandRequest.builder()
        .id("khannedy")
        .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
        .toBlocking().value();

    assertEquals(request.getId(), response.getId());
    assertEquals(request.getId(), response.getName());

    verify(stringRedisTemplate, times(2))
        .opsForValue();
    verify(valueOperations, times(1))
        .get("khannedy");
    verify(valueOperations, times(1))
        .set(eq("khannedy"), anyString(),
            eq(commandProperties.getCache().getTimeout()),
            eq(commandProperties.getCache().getTimeoutUnit()));
  }

  @Test
  public void testExecuteCache() throws JsonProcessingException {
    commandProperties.getCache().setEnabled(true);

    GetProductCommandResponse cacheResponse = GetProductCommandResponse.builder()
        .id("khannedy")
        .name("Cache")
        .build();

    when(valueOperations.get("khannedy"))
        .thenReturn(objectMapper.writeValueAsString(cacheResponse));

    GetProductCommandRequest request = GetProductCommandRequest.builder()
        .id("khannedy")
        .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
        .toBlocking().value();

    assertEquals(request.getId(), response.getId());
    assertEquals(cacheResponse.getName(), response.getName());

    verify(stringRedisTemplate, times(1))
        .opsForValue();
    verify(valueOperations, times(1))
        .get("khannedy");
    verify(valueOperations, times(0))
        .set(eq("khannedy"), anyString(),
            eq(commandProperties.getCache().getTimeout()),
            eq(commandProperties.getCache().getTimeoutUnit()));
  }

  @Test
  public void testExecuteHystrixCommand() {
    commandProperties.getCache().setEnabled(true);
    commandProperties.getHystrix().setEnabled(true);

    GetProductCommandRequest request = GetProductCommandRequest.builder()
        .id("khannedy")
        .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
        .toBlocking().value();

    assertEquals(request.getId(), response.getId());
    assertEquals(request.getId(), response.getName());

    verify(stringRedisTemplate, times(2))
        .opsForValue();
    verify(valueOperations, times(1))
        .get("khannedy");
    verify(valueOperations, times(1))
        .set(eq("khannedy"), anyString(),
            eq(commandProperties.getCache().getTimeout()),
            eq(commandProperties.getCache().getTimeoutUnit()));
  }

  @Test
  public void testExecuteHystrixCache() throws JsonProcessingException {
    commandProperties.getCache().setEnabled(true);
    commandProperties.getHystrix().setEnabled(true);

    GetProductCommandResponse cacheResponse = GetProductCommandResponse.builder()
        .id("khannedy")
        .name("Cache")
        .build();

    when(valueOperations.get("khannedy"))
        .thenReturn(objectMapper.writeValueAsString(cacheResponse));

    GetProductCommandRequest request = GetProductCommandRequest.builder()
        .id("khannedy")
        .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
        .toBlocking().value();

    assertEquals(request.getId(), response.getId());
    assertEquals(cacheResponse.getName(), response.getName());

    verify(stringRedisTemplate, times(1))
        .opsForValue();
    verify(valueOperations, times(1))
        .get("khannedy");
    verify(valueOperations, times(0))
        .set(eq("khannedy"), anyString(),
            eq(commandProperties.getCache().getTimeout()),
            eq(commandProperties.getCache().getTimeoutUnit()));
  }

  @Test
  public void testExecuteNonCache() throws JsonProcessingException {
    commandProperties.getCache().setEnabled(false);

    GetProductCommandRequest request = GetProductCommandRequest.builder()
        .id("khannedy")
        .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
        .toBlocking().value();

    assertEquals(request.getId(), response.getId());
    assertEquals(request.getId(), response.getName());

    verify(stringRedisTemplate, times(0))
        .opsForValue();
    verify(valueOperations, times(0))
        .get("khannedy");
    verify(valueOperations, times(0))
        .set(eq("khannedy"), anyString(),
            eq(commandProperties.getCache().getTimeout()),
            eq(commandProperties.getCache().getTimeoutUnit()));
  }

  @Test
  public void testExecuteHystrixNonCache() throws JsonProcessingException {
    commandProperties.getCache().setEnabled(false);
    commandProperties.getHystrix().setEnabled(true);

    GetProductCommandRequest request = GetProductCommandRequest.builder()
        .id("khannedy")
        .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
        .toBlocking().value();

    assertEquals(request.getId(), response.getId());
    assertEquals(request.getId(), response.getName());

    verify(stringRedisTemplate, times(0))
        .opsForValue();
    verify(valueOperations, times(0))
        .get("khannedy");
    verify(valueOperations, times(0))
        .set(eq("khannedy"), anyString(),
            eq(commandProperties.getCache().getTimeout()),
            eq(commandProperties.getCache().getTimeoutUnit()));
  }

  @SpringBootApplication
  public static class Application {

  }

}