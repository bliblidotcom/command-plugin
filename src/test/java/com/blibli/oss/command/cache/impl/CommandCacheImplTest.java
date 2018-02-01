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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheImplTest {

  private static final String VALUE = "VALUE";
  private static final String KEY = "KEY";
  private static final long TIMEOUT = 1000L;
  private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private CommandProperties commandProperties;

  @Mock
  private CommandProperties.CacheProperties cacheProperties;

  @Mock
  private StringRedisTemplate stringRedisTemplate;

  @Mock
  private ValueOperations<String, String> valueOperations;

  @InjectMocks
  private CommandCacheImpl commandCache;

  @Before
  public void setUp() throws Exception {
    setUpStringRedisTemplateValueOperation();
    setUpProperties();
  }

  private void setUpProperties() {
    when(commandProperties.getCache()).thenReturn(cacheProperties);
    when(cacheProperties.getTimeout()).thenReturn(TIMEOUT);
    when(cacheProperties.getTimeoutUnit()).thenReturn(TIMEOUT_UNIT);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(commandProperties, stringRedisTemplate, valueOperations, cacheProperties);
  }

  private void setUpStringRedisTemplateValueOperation() {
    when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  public void testGet() {
    mockValueOperationGet();
    String response = commandCache.get(KEY);
    assertEquals(VALUE, response);

    verify(stringRedisTemplate, times(1)).opsForValue();
    verify(valueOperations, times(1)).get(KEY);
  }

  private void mockValueOperationGet() {
    when(valueOperations.get(KEY)).thenReturn(VALUE);
  }

  @Test
  public void testCache() {
    commandCache.cache(KEY, VALUE);

    verify(stringRedisTemplate, times(1)).opsForValue();
    verify(valueOperations, times(1)).set(KEY, VALUE, TIMEOUT, TIMEOUT_UNIT);
    verify(commandProperties, times(2)).getCache();
    verify(cacheProperties, times(1)).getTimeout();
    verify(cacheProperties, times(1)).getTimeoutUnit();
  }

  @Test
  public void testEvict() {
    commandCache.evict(KEY);

    verify(stringRedisTemplate, times(1)).delete(KEY);
  }
}