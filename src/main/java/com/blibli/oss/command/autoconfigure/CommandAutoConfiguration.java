package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.impl.CommandExecutorImpl;
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
    return new CommandExecutorImpl(validator);
  }
}
