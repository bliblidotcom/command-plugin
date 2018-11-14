package com.blibli.oss.command.cache;

import reactor.core.publisher.Mono;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface CommandCache {

  Mono<String> get(String key);

  Mono<Boolean> cache(String key, String value);

  Mono<Long> evict(String... keys);

}
