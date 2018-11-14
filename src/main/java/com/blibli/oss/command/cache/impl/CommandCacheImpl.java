package com.blibli.oss.command.cache.impl;

import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.properties.CommandProperties;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheImpl implements CommandCache {

  private CommandProperties commandProperties;

  private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

  public CommandCacheImpl(ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                          CommandProperties commandProperties) {
    this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    this.commandProperties = commandProperties;
  }

  @Override
  public Mono<String> get(String key) {
    return reactiveStringRedisTemplate.opsForValue().get(key);
  }

  @Override
  public Mono<Boolean> cache(String key, String value) {
    return reactiveStringRedisTemplate.opsForValue()
      .set(key, value, commandProperties.getCache().getTimeoutDuration());
  }

  @Override
  public Mono<Long> evict(String... keys) {
    return reactiveStringRedisTemplate.delete(Flux.fromArray(keys));
  }
}
