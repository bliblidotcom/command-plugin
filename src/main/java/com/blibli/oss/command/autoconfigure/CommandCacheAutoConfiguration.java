package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheInterceptor;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.cache.impl.CommandCacheImpl;
import com.blibli.oss.command.cache.impl.CommandCacheMapperImpl;
import com.blibli.oss.command.properties.CommandProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@AutoConfigureAfter(CommandPropertiesAutoConfiguration.class)
@ConditionalOnProperty(name = "command.cache.enabled")
public class CommandCacheAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public CommandCache commandCache(@Autowired ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                                   @Autowired CommandProperties commandProperties) {
    return new CommandCacheImpl(reactiveStringRedisTemplate, commandProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandCacheMapper commandCacheMapper(@Autowired ObjectMapper objectMapper) {
    return new CommandCacheMapperImpl(objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandCacheInterceptor commandCacheInterceptor(@Autowired(required = false) CommandCache commandCache,
                                                         @Autowired(required = false) CommandCacheMapper commandCacheMapper) {
    return new CommandCacheInterceptor(commandCache, commandCacheMapper);
  }

}
