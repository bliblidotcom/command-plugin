package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.impl.CommandExecutorImpl;
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
                                         @Autowired CommandKeyStrategy commandKeyStrategy,
                                         @Autowired CommandGroupStrategy commandGroupStrategy,
                                         @Autowired CommandProperties commandProperties,
                                         @Autowired(required = false) CommandCacheMapper commandCacheMapper,
                                         @Autowired(required = false) CommandCache commandCache) {
    return new CommandExecutorImpl(
        validator, commandKeyStrategy,
        commandGroupStrategy, commandProperties,
        commandCache, commandCacheMapper
    );
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
