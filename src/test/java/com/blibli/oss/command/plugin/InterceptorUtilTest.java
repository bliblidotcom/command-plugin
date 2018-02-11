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

package com.blibli.oss.command.plugin;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.exception.CommandRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class InterceptorUtilTest {

  private static final String REQUEST = "REQUEST";
  private static final String RESPONSE = "RESPONSE";

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private CommandInterceptor commandInterceptor;

  @Mock
  private DataCommand dataCommand;

  private List<CommandInterceptor> commandInterceptors;

  private Throwable throwable = new RuntimeException();

  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private CommandInterceptor commandInterceptor1;

  @Mock
  private CommandInterceptor commandInterceptor2;

  @Mock
  private CommandInterceptor commandInterceptor3;

  private Map<String, CommandInterceptor> commandInterceptorMap = new HashMap<>();

  @Before
  public void setUp() throws Exception {
    setUpCommandInterceptors();
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(commandInterceptor, commandInterceptor1,
        commandInterceptor2, commandInterceptor3, applicationContext);
  }

  private void setUpCommandInterceptors() {
    commandInterceptors = Collections.singletonList(commandInterceptor);
  }

  @Test
  public void testBeforeExecuteNull() {
    String response = InterceptorUtil.beforeExecute(commandInterceptors, dataCommand, REQUEST);
    assertNull(response);

    verify(commandInterceptor, times(1)).beforeExecute(dataCommand, REQUEST);
  }

  @Test
  public void testBeforeExecuteError() {
    mockCommandInterceptorBeforeExecuteError();

    String response = InterceptorUtil.beforeExecute(commandInterceptors, dataCommand, REQUEST);
    assertNull(response);

    verify(commandInterceptor, times(1)).beforeExecute(dataCommand, REQUEST);
  }

  private void mockCommandInterceptorBeforeExecuteError() {
    when(commandInterceptor.beforeExecute(dataCommand, REQUEST)).thenThrow(new CommandRuntimeException());
  }

  @Test
  public void testBeforeExecute() {
    mockCommandInterceptorBeforeExecute();

    String response = InterceptorUtil.beforeExecute(commandInterceptors, dataCommand, REQUEST);
    assertEquals(RESPONSE, response);

    verify(commandInterceptor, times(1)).beforeExecute(dataCommand, REQUEST);
  }

  private void mockCommandInterceptorBeforeExecute() {
    when(commandInterceptor.beforeExecute(dataCommand, REQUEST)).thenReturn(RESPONSE);
  }

  @Test
  public void testAfterSuccessExecute() {
    InterceptorUtil.afterSuccessExecute(commandInterceptors, dataCommand, REQUEST, RESPONSE);

    verify(commandInterceptor, times(1)).afterSuccessExecute(dataCommand, REQUEST, RESPONSE);
  }

  @Test
  public void testAfterSuccessExecuteError() {
    doThrow(new CommandRuntimeException()).when(commandInterceptor).afterSuccessExecute(dataCommand, REQUEST, RESPONSE);
    InterceptorUtil.afterSuccessExecute(commandInterceptors, dataCommand, REQUEST, RESPONSE);

    verify(commandInterceptor, times(1)).afterSuccessExecute(dataCommand, REQUEST, RESPONSE);
  }

  @Test
  public void testAfterFailedExecute() {
    InterceptorUtil.afterFailedExecute(commandInterceptors, dataCommand, REQUEST, throwable);

    verify(commandInterceptor, times(1)).afterFailedExecute(dataCommand, REQUEST, throwable);
  }

  @Test
  public void testAfterFailedExecuteError() {
    doThrow(new CommandRuntimeException()).when(commandInterceptor).afterFailedExecute(dataCommand, REQUEST, throwable);
    InterceptorUtil.afterFailedExecute(commandInterceptors, dataCommand, REQUEST, throwable);

    verify(commandInterceptor, times(1)).afterFailedExecute(dataCommand, REQUEST, throwable);
  }

  @Test
  public void testGetCommandInterceptorFromNullMap() {
    List<CommandInterceptor> interceptors = InterceptorUtil.getCommandInterceptors(applicationContext);
    assertTrue(interceptors.isEmpty());

    verify(applicationContext).getBeansOfType(CommandInterceptor.class);
  }

  @Test
  public void testGetCommandInterceptorFromEmptyMap() {
    mockApplicationContextGetBeanCommandInterceptor();
    List<CommandInterceptor> interceptors = InterceptorUtil.getCommandInterceptors(applicationContext);
    assertTrue(interceptors.isEmpty());

    verify(applicationContext).getBeansOfType(CommandInterceptor.class);
  }

  private void mockApplicationContextGetBeanCommandInterceptor() {
    when(applicationContext.getBeansOfType(CommandInterceptor.class))
        .thenReturn(commandInterceptorMap);
  }

  @Test
  public void testGetCommandInterceptor() {
    mockApplicationContextGetBeanCommandInterceptor();
    mockCommandInterceptorMap();

    List<CommandInterceptor> interceptors = InterceptorUtil.getCommandInterceptors(applicationContext);
    assertSame(commandInterceptor3, interceptors.get(0));
    assertSame(commandInterceptor2, interceptors.get(1));
    assertSame(commandInterceptor1, interceptors.get(2));

    verify(commandInterceptor1).getOrder();
    verify(commandInterceptor2, times(2)).getOrder();
    verify(commandInterceptor3).getOrder();
    verify(applicationContext).getBeansOfType(CommandInterceptor.class);
  }

  private void mockCommandInterceptorMap() {
    when(commandInterceptor1.getOrder()).thenReturn(3);
    when(commandInterceptor2.getOrder()).thenReturn(2);
    when(commandInterceptor3.getOrder()).thenReturn(1);
    commandInterceptorMap.put("1", commandInterceptor1);
    commandInterceptorMap.put("2", commandInterceptor2);
    commandInterceptorMap.put("3", commandInterceptor3);
  }

  private static interface DataCommand extends Command<String, String> {

  }
}