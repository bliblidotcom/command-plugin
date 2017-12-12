package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.impl.CommandExecutorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@ConditionalOnClass({Validator.class})
public class CommandAutoConfiguration {

  @Bean
  @Autowired
  public CommandExecutor commandExecutor(Validator validator) {
    return new CommandExecutorImpl(validator);
  }

}
