package com.blibli.oss.command.plugin.impl;

import com.blibli.oss.command.Command;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx.Single;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandGroupStrategyImplTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private MyCommand command;

  @InjectMocks
  private CommandGroupStrategyImpl strategy;

  @Test
  public void testGetGroupFromCommand() {
    when(command.group()).thenReturn("group");
    String commandGroup = strategy.getCommandGroup(command);
    assertEquals("group", commandGroup);
  }

  @Test
  public void testGetGroupFromPacakge() {
    String commandGroup = strategy.getCommandGroup(command);
    assertEquals(command.getClass().getPackage().getName(), commandGroup);
  }

  private static class MyCommand implements Command<String, String> {

    @Override
    public Single<String> execute(String request) {
      return null;
    }
  }

}