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

package com.blibli.oss.command.cache;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.properties.CommandProperties;
import lombok.Builder;
import lombok.Data;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheInterceptorTest {

  private static final String CACHE_KEY = "CACHE_KEY";
  private static final String DATA = "DATA";
  private static final String CACHE_VALUE = "CACHE_VALUE";
  private static final String MAPPER_VALUE = "MAPPER_VALUE";
  private static final String RESPONSE = "RESPONSE";

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private CommandProperties commandProperties;

  @Mock
  private CommandProperties.CacheProperties cacheProperties;

  @Mock
  private CommandCache commandCache;

  @Mock
  private CommandCacheMapper commandCacheMapper;

  @InjectMocks
  private CommandCacheInterceptor commandCacheInterceptor;

  @Mock
  private DataCommand dataCommand;

  private DataCommandRequest dataCommandRequest;

  @Before
  public void setUp() throws Exception {
    setUpCommandProperties();
    setUpCommandRequest();
  }

  private void setUpCommandProperties() {
    when(commandProperties.getCache()).thenReturn(cacheProperties);
  }

  private void setUpCommandRequest() {
    dataCommandRequest = DataCommandRequest.builder()
        .data(DATA)
        .build();
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(commandCache, commandCacheMapper, dataCommand);
  }

  @Test
  public void testBeforeExecuteDisabled() {
    setUpCachePropertiesDisabled();
    String response = commandCacheInterceptor.beforeExecute(dataCommand, dataCommandRequest);
    assertNull(response);
  }

  private void setUpCachePropertiesDisabled() {
    when(cacheProperties.isEnabled()).thenReturn(false);
  }

  @Test
  public void testBeforeExecuteCacheKeyNull() {
    setUpCachePropertiesEnabled();
    mockDataCommandCacheKeyNull();
    String response = commandCacheInterceptor.beforeExecute(dataCommand, dataCommandRequest);
    assertNull(response);

    verify(dataCommand, times(1)).cacheKey(dataCommandRequest);
  }

  private void mockDataCommandCacheKeyNull() {
    when(dataCommand.cacheKey(dataCommandRequest)).thenReturn(null);
  }

  private void setUpCachePropertiesEnabled() {
    when(cacheProperties.isEnabled()).thenReturn(true);
  }

  @Test
  public void testBeforeExecuteCommandCacheNull() {
    setUpCachePropertiesEnabled();
    mockDataCommandCacheKey();
    mockCommandCacheGetNull();
    String response = commandCacheInterceptor.beforeExecute(dataCommand, dataCommandRequest);
    assertNull(response);

    verify(dataCommand, times(1)).cacheKey(dataCommandRequest);
    verify(commandCache, times(1)).get(CACHE_KEY);
  }

  private void mockCommandCacheGetNull() {
    when(commandCache.get(CACHE_KEY)).thenReturn(null);
  }

  private void mockDataCommandCacheKey() {
    when(dataCommand.cacheKey(dataCommandRequest)).thenReturn(CACHE_KEY);
  }

  @Test
  public void testBeforeExecuteSuccess() {
    setUpCachePropertiesEnabled();
    mockDataCommandCacheKey();
    mockCommandCacheGet();
    mockDataCommandResponseClass();
    mockCommandCacheMapperFromString();
    String response = commandCacheInterceptor.beforeExecute(dataCommand, dataCommandRequest);
    assertEquals(MAPPER_VALUE, response);

    verify(dataCommand, times(1)).cacheKey(dataCommandRequest);
    verify(commandCache, times(1)).get(CACHE_KEY);
    verify(dataCommand, times(1)).responseClass();
    verify(commandCacheMapper, times(1)).fromString(CACHE_VALUE, String.class);
  }

  private void mockCommandCacheMapperFromString() {
    when(commandCacheMapper.fromString(CACHE_VALUE, String.class)).thenReturn(MAPPER_VALUE);
  }

  private void mockDataCommandResponseClass() {
    when(dataCommand.responseClass()).thenReturn(String.class);
  }

  private void mockCommandCacheGet() {
    when(commandCache.get(CACHE_KEY)).thenReturn(CACHE_VALUE);
  }

  @Test
  public void testAfterSuccessExecuteCacheDisabled() {
    setUpCachePropertiesDisabled();
    commandCacheInterceptor.afterSuccessExecute(dataCommand, dataCommandRequest, RESPONSE);

    verify(dataCommand, times(0)).cacheKey(dataCommandRequest);
    verify(dataCommand, times(0)).evictKey(dataCommandRequest);
  }

  @Test
  public void testAfterSuccessExecuteCacheEnabled() {
    setUpCachePropertiesEnabled();
    commandCacheInterceptor.afterSuccessExecute(dataCommand, dataCommandRequest, RESPONSE);

    verify(dataCommand, times(1)).cacheKey(dataCommandRequest);
    verify(dataCommand, times(1)).evictKey(dataCommandRequest);
  }

  @Test
  public void testAfterSuccessExecuteCache() {
    setUpCachePropertiesEnabled();
    mockDataCommandCacheKey();
    mockCommandCacheMapperToString();
    commandCacheInterceptor.afterSuccessExecute(dataCommand, dataCommandRequest, RESPONSE);

    verify(dataCommand, times(1)).cacheKey(dataCommandRequest);
    verify(dataCommand, times(1)).evictKey(dataCommandRequest);
    verify(commandCacheMapper, times(1)).toString(RESPONSE);
    verify(commandCache, times(1)).cache(CACHE_KEY, MAPPER_VALUE);
  }

  private void mockCommandCacheMapperToString() {
    when(commandCacheMapper.toString(RESPONSE)).thenReturn(MAPPER_VALUE);
  }

  @Test
  public void testAfterSuccessExecuteEvict() {
    setUpCachePropertiesEnabled();
    mockCommandEvictKey();
    commandCacheInterceptor.afterSuccessExecute(dataCommand, dataCommandRequest, RESPONSE);

    verify(dataCommand, times(1)).cacheKey(dataCommandRequest);
    verify(dataCommand, times(1)).evictKey(dataCommandRequest);
    verify(commandCache, times(1)).evict(CACHE_KEY);
  }

  private void mockCommandEvictKey() {
    when(dataCommand.evictKey(dataCommandRequest)).thenReturn(CACHE_KEY);
  }

  @Data
  @Builder
  private static class DataCommandRequest {

    private String data;
  }

  private static interface DataCommand extends Command<DataCommandRequest, String> {

  }
}