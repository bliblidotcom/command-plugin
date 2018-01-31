package com.blibli.oss.command.plugin.impl;

import com.blibli.oss.command.Command;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;
import rx.Single;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandKeyStrategyImplTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private MyCommand command;

  @InjectMocks
  private CommandKeyStrategyImpl strategy;

  @Before
  public void setUp() throws Exception {
    strategy.setApplicationContext(applicationContext);
  }

  @Test
  public void testGetKeyFromCommand() {
    when(command.key()).thenReturn("key");
    String commandKey = strategy.getCommandKey(command);
    assertEquals("key", commandKey);
  }

  @Test
  public void testGetKeyFromApplicationContext() {
    when(applicationContext.getBeanNamesForType(Matchers.<Class<?>>any())).thenReturn(new String[]{"beanName"});
    String commandKey = strategy.getCommandKey(command);
    assertEquals("beanName", commandKey);
  }

  @Test
  public void testGetKeyFromClassName() {
    String commandKey = strategy.getCommandKey(command);
    assertEquals(command.getClass().getSimpleName(), commandKey);
  }

  private static class MyCommand implements Command<String, String> {

    @Override
    public Single<String> execute(String request) {
      return null;
    }
  }
}