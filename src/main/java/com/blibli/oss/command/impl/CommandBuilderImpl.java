package com.blibli.oss.command.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.CommandBuilder;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandBuilderImpl<REQUEST, RESPONSE> implements CommandBuilder<REQUEST, RESPONSE> {

  private REQUEST request;

  private Class<? extends Command<REQUEST, RESPONSE>> commandClass;

  public CommandBuilderImpl(REQUEST request, Class<? extends Command<REQUEST, RESPONSE>> commandClass) {
    this.request = request;
    this.commandClass = commandClass;
  }

  @Override
  public REQUEST getRequest() {
    return request;
  }

  @Override
  public Class<? extends Command<REQUEST, RESPONSE>> getCommandClass() {
    return commandClass;
  }
}
