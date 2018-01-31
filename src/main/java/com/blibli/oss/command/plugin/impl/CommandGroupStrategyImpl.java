package com.blibli.oss.command.plugin.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.plugin.CommandGroupStrategy;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public class CommandGroupStrategyImpl implements CommandGroupStrategy {

  @Override
  public String getCommandGroup(Command<?, ?> command) {
    String group = command.group();

    if (group == null) {
      group = getPackageName(command);
    }

    return group;
  }

  private String getPackageName(Command<?, ?> command) {
    return command.getClass().getPackage().getName();
  }

}
