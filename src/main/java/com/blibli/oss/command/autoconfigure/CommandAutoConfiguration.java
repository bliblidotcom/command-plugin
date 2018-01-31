package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.CommandProcessor;
import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.impl.CommandExecutorImpl;
import com.blibli.oss.command.impl.CommandProcessorImpl;
import com.blibli.oss.command.plugin.CommandGroupStrategy;
import com.blibli.oss.command.plugin.CommandKeyStrategy;
import com.blibli.oss.command.plugin.impl.CommandGroupStrategyImpl;
import com.blibli.oss.command.plugin.impl.CommandKeyStrategyImpl;
import com.blibli.oss.command.properties.CommandProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@AutoConfigureAfter({CommandPropertiesAutoConfiguration.class})
public class CommandAutoConfiguration {

  @Bean
  @ConditionalOnClass({Validator.class})
  public CommandExecutor commandExecutor(@Autowired Validator validator,
                                         @Autowired CommandProcessor commandProcessor) {
    return new CommandExecutorImpl(validator, commandProcessor);
  }

  @Bean
  public CommandProcessor commandProcessor(@Autowired CommandProperties commandProperties,
                                           @Autowired CommandKeyStrategy commandKeyStrategy,
                                           @Autowired CommandGroupStrategy commandGroupStrategy) {
    return new CommandProcessorImpl(commandProperties, commandKeyStrategy, commandGroupStrategy);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandKeyStrategy commandKeyStrategy() {
    return new CommandKeyStrategyImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandGroupStrategy commandGroupStrategy() {
    return new CommandGroupStrategyImpl();
  }

}
