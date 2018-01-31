package com.blibli.oss.command.plugin;

import com.blibli.oss.command.Command;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public interface CommandKeyStrategy {

  /**
   * Get command key from command
   *
   * @param command command object
   * @return command key
   */
  String getCommandKey(Command<?, ?> command);

}
