package com.blibli.oss.command.plugin.impl;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.plugin.CommandKeyStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public class CommandKeyStrategyImpl implements CommandKeyStrategy, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public String getCommandKey(Command<?, ?> command) {
    String key = command.key();

    if (key == null) {
      key = getBeanName(command);
    }

    if (key == null) {
      key = getClassName(command);
    }

    return key;
  }

  private String getClassName(Command<?, ?> command) {
    return command.getClass().getSimpleName();
  }

  private String getBeanName(Command<?, ?> command) {
    String[] names = applicationContext.getBeanNamesForType(command.getClass());
    if (names != null && names.length == 1) {
      return names[0];
    } else {
      return null;
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
