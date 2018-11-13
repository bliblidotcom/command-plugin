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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheInterceptorTest {

  public static final String CACHE_KEY = "cacheKey";
  public static final String CACHE_VALUE = "cacheValue";
  public static final String ID = "id";
  public static final DataResponse DATA_RESPONSE = DataResponse.builder().id(ID).build();
  public static final DataRequest DATA_REQUEST = DataRequest.builder()
    .id(ID)
    .build();
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private CommandCache commandCache;

  @Mock
  private CommandCacheMapper commandCacheMapper;

  @InjectMocks
  private CommandCacheInterceptor interceptor;

  @Mock
  private DataCommand dataCommand;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void beforeExecuteWithResult() {
    when(dataCommand.cacheKey(DATA_REQUEST))
      .thenReturn(CACHE_KEY);
    when(commandCache.get(CACHE_KEY))
      .thenReturn(Mono.just(CACHE_VALUE));
    when(dataCommand.responseClass())
      .thenReturn(DataResponse.class);
    when(commandCacheMapper.fromString(CACHE_VALUE, DataResponse.class))
      .thenReturn(DATA_RESPONSE);

    DataResponse response = interceptor.beforeExecute(dataCommand, DATA_REQUEST).block();

    assertEquals(DATA_RESPONSE, response);

    verify(dataCommand).cacheKey(DATA_REQUEST);
    verify(commandCache).get(CACHE_KEY);
    verify(dataCommand).responseClass();
    verify(commandCacheMapper).fromString(CACHE_VALUE, DataResponse.class);
  }

  @Test
  public void beforeExecuteNoResult() {
    DataResponse response = interceptor.beforeExecute(dataCommand, DATA_REQUEST).block();

    assertNull(response);

    verify(dataCommand).cacheKey(DATA_REQUEST);
  }

  @Test
  public void afterSuccessExecute() {
    when(dataCommand.cacheKey(DATA_REQUEST))
      .thenReturn(CACHE_KEY);
    when(dataCommand.evictKeys(DATA_REQUEST))
      .thenReturn(Collections.singletonList(CACHE_KEY));
    when(commandCacheMapper.toString(DATA_RESPONSE))
      .thenReturn(CACHE_VALUE);
    when(commandCache.cache(CACHE_KEY, CACHE_VALUE))
      .thenReturn(Mono.just(true));
    when(commandCache.evict(CACHE_KEY))
      .thenReturn(Mono.just(1L));

    Void result = interceptor.afterSuccessExecute(dataCommand, DATA_REQUEST, DATA_RESPONSE).block();

    assertNull(result);

    verify(dataCommand).cacheKey(DATA_REQUEST);
    verify(dataCommand).evictKeys(DATA_REQUEST);
    verify(commandCacheMapper).toString(DATA_RESPONSE);
    verify(commandCache).cache(CACHE_KEY, CACHE_VALUE);
    verify(commandCache).evict(CACHE_KEY);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(commandCache, commandCacheMapper);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DataRequest {

    private String id;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DataResponse {

    private String id;
  }

  public static interface DataCommand extends Command<DataRequest, DataResponse> {

  }
}