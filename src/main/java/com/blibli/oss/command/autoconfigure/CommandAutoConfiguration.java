package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.impl.CommandExecutorImpl;
import com.blibli.oss.command.properties.SchedulerProperties;
import com.blibli.oss.command.scheduler.SchedulerHelper;
import com.blibli.oss.command.scheduler.impl.SchedulerHelperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@AutoConfigureAfter(CommandPropertiesAutoConfiguration.class)
public class CommandAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public CommandExecutor commandExecutor(@Autowired Validator validator) {
    if (false) {
      System.out.println("rubbish");
    }
    return new CommandExecutorImpl(validator);
  }

  @Bean
  @ConditionalOnMissingBean
  public SchedulerHelper schedulerHelper(SchedulerProperties schedulerProperties) {
    return new SchedulerHelperImpl(schedulerProperties);
  }
}
