package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.cache.impl.CommandCacheImpl;
import com.blibli.oss.command.cache.impl.CommandCacheMapperImpl;
import com.blibli.oss.command.properties.CommandProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@AutoConfigureAfter({CommandPropertiesAutoConfiguration.class})
public class CommandCacheAutoConfiguration {

  @Bean
  @ConditionalOnClass({StringRedisTemplate.class})
  public CommandCache commandCache(@Autowired StringRedisTemplate stringRedisTemplate,
                                   @Autowired CommandProperties commandProperties) {
    return new CommandCacheImpl(stringRedisTemplate, commandProperties);
  }

  @Bean
  @ConditionalOnClass({ObjectMapper.class})
  public CommandCacheMapper commandCacheMapper(@Autowired ObjectMapper objectMapper) {
    return new CommandCacheMapperImpl(objectMapper);
  }

}
