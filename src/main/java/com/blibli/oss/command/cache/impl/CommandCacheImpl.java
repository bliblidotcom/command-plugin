package com.blibli.oss.command.cache.impl;

import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.properties.CommandProperties;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheImpl implements CommandCache {

  private CommandProperties commandProperties;

  private StringRedisTemplate stringRedisTemplate;

  public CommandCacheImpl(StringRedisTemplate stringRedisTemplate,
                          CommandProperties commandProperties) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.commandProperties = commandProperties;
  }

  @Override
  public String get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

  @Override
  public void cache(String key, String value) {
    stringRedisTemplate.opsForValue().set(
        key, value,
        commandProperties.getCache().getTimeout(),
        commandProperties.getCache().getTimeoutUnit()
    );
  }

  @Override
  public void evict(String key) {
    stringRedisTemplate.delete(key);
  }
}
