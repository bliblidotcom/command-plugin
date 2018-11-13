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
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = GetProductCommandImplTest.Application.class,
  properties = {
    "command.cache.enabled=true"
  }
)
public class GetProductCommandImplTest {

  public static final String CACHE_KEY = "khannedy";

  @MockBean
  private ReactiveStringRedisTemplate stringRedisTemplate;

  @MockBean
  private ReactiveValueOperations<String, String> valueOperations;

  @Autowired
  private CommandProperties commandProperties;

  @Autowired
  private CommandExecutor commandExecutor;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setUp() throws Exception {
    when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  public void testExecuteCommand() {
    when(valueOperations.get(CACHE_KEY))
      .thenReturn(Mono.empty());
    when(valueOperations.set(eq(CACHE_KEY), anyString(), eq(commandProperties.getCache().getTimeoutDuration())))
      .thenReturn(Mono.just(true));
    when(stringRedisTemplate.delete(any(Publisher.class)))
      .thenReturn(Mono.just(1L));

    GetProductCommandRequest request = GetProductCommandRequest.builder()
      .id(CACHE_KEY)
      .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
      .block();

    assertEquals(request.getId(), response.getId());
    assertEquals(request.getId(), response.getName());

    verify(stringRedisTemplate, times(2))
      .opsForValue();
    verify(valueOperations, times(1))
      .get(CACHE_KEY);
    verify(valueOperations, times(1))
      .set(eq(CACHE_KEY), anyString(),
        eq(commandProperties.getCache().getTimeoutDuration()));
    verify(stringRedisTemplate, times(1))
      .delete(any(Publisher.class));
  }

  @Test
  public void testExecuteCache() throws JsonProcessingException {
    GetProductCommandResponse cacheResponse = GetProductCommandResponse.builder()
      .id(CACHE_KEY)
      .name("Cache")
      .build();

    when(valueOperations.get(CACHE_KEY))
      .thenReturn(Mono.just(objectMapper.writeValueAsString(cacheResponse)));

    GetProductCommandRequest request = GetProductCommandRequest.builder()
      .id(CACHE_KEY)
      .build();

    GetProductCommandResponse response = commandExecutor.execute(GetProductCommand.class, request)
      .block();

    assertEquals(request.getId(), response.getId());
    assertEquals(cacheResponse.getName(), response.getName());

    verify(stringRedisTemplate, times(1))
      .opsForValue();
    verify(valueOperations, times(1))
      .get(CACHE_KEY);
  }

  @SpringBootApplication
  public static class Application {

  }

}