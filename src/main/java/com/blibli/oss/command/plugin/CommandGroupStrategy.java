package com.blibli.oss.command.plugin;

import com.blibli.oss.command.Command;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public interface CommandGroupStrategy {

  /**
   * Get command group
   *
   * @param command command object
   * @return command group
   */
  String getCommandGroup(Command<?, ?> command);

}
