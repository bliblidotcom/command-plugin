/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blibli.oss.command.cache.impl;

import com.blibli.oss.command.properties.CommandProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.reactivestreams.Publisher;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheImplTest {

  public static final String VALUE = "value";
  public static final String KEY = "key";
  public static final Duration DURATION = Duration.ZERO;
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private CommandProperties commandProperties;

  @Mock
  private CommandProperties.CacheProperties cacheProperties;

  @Mock
  private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

  @Mock
  private ReactiveValueOperations<String, String> valueOperations;

  @InjectMocks
  private CommandCacheImpl commandCache;

  @Before
  public void setUp() throws Exception {
    when(reactiveStringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    when(commandProperties.getCache()).thenReturn(cacheProperties);
    when(cacheProperties.getTimeoutDuration()).thenReturn(DURATION);
  }

  @Test
  public void testGet() {
    when(valueOperations.get(KEY)).thenReturn(Mono.just(VALUE));

    String result = commandCache.get(KEY).block();

    assertEquals(VALUE, result);

    verify(reactiveStringRedisTemplate).opsForValue();
    verify(valueOperations).get(KEY);
  }

  @Test
  public void testCache() {
    when(valueOperations.set(KEY, VALUE, DURATION))
      .thenReturn(Mono.just(true));

    Boolean result = commandCache.cache(KEY, VALUE).block();

    assertTrue(result);

    verify(reactiveStringRedisTemplate).opsForValue();
    verify(valueOperations).set(KEY, VALUE, DURATION);
  }

  @Test
  public void testEvict() {
    when(reactiveStringRedisTemplate.delete(any(Publisher.class))).thenReturn(Mono.just(1L));

    Long result = commandCache.evict(KEY).block();

    assertEquals(Long.valueOf(1L), result);

    verify(reactiveStringRedisTemplate).delete(any(Publisher.class));
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(reactiveStringRedisTemplate, valueOperations);
  }
}