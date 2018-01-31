package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.impl.CommandExecutorImpl;
import com.blibli.oss.command.plugin.CommandGroupStrategy;
import com.blibli.oss.command.plugin.CommandKeyStrategy;
import com.blibli.oss.command.plugin.impl.CommandGroupStrategyImpl;
import com.blibli.oss.command.plugin.impl.CommandKeyStrategyImpl;
import com.blibli.oss.command.properties.CommandProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@EnableConfigurationProperties({
    CommandProperties.class
})
@ConditionalOnClass({Validator.class})
public class CommandAutoConfiguration {

  @Bean
  @Autowired
  public CommandExecutor commandExecutor(Validator validator,
                                         CommandKeyStrategy commandKeyStrategy,
                                         CommandGroupStrategy commandGroupStrategy,
                                         CommandProperties commandProperties) {
    return new CommandExecutorImpl(validator, commandKeyStrategy,
        commandGroupStrategy, commandProperties);
  }

  @Bean
  @ConditionalOnMissingBean(CommandKeyStrategy.class)
  public CommandKeyStrategy commandKeyStrategy() {
    return new CommandKeyStrategyImpl();
  }

  @Bean
  @ConditionalOnMissingBean(CommandGroupStrategy.class)
  public CommandGroupStrategy commandGroupStrategy() {
    return new CommandGroupStrategyImpl();
  }

}
