package com.blibli.oss.command.cache;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.plugin.CommandInterceptor;
import reactor.core.publisher.Mono;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
public class CommandCacheInterceptor implements CommandInterceptor {

  private CommandCache commandCache;

  private CommandCacheMapper commandCacheMapper;

  public CommandCacheInterceptor(CommandCache commandCache,
                                 CommandCacheMapper commandCacheMapper) {
    this.commandCache = commandCache;
    this.commandCacheMapper = commandCacheMapper;
  }

  @Override
  public <R, T> Mono<T> beforeExecute(Command<R, T> command, R request) {
    return Mono.fromCallable(() -> command.cacheKey(request))
      .flatMap(key -> commandCache.get(key))
      .map(json -> commandCacheMapper.fromString(json, command.responseClass()));
  }

  @Override
  public <R, T> Mono<Void> afterSuccessExecute(Command<R, T> command, R request, T response) {
    return Mono.zip(
      evictCommandResponse(command, request),
      cacheCommandResponse(command, request, response)
    ).flatMap(objects -> Mono.empty());
  }

  private <R, T> Mono<Boolean> cacheCommandResponse(Command<R, T> command, R request, T response) {
    return Mono.zip(
      Mono.fromCallable(() -> command.cacheKey(request)),
      Mono.fromCallable(() -> commandCacheMapper.toString(response))
    ).flatMap(tuple -> commandCache.cache(tuple.getT1(), tuple.getT2()))
      .switchIfEmpty(Mono.just(false));
  }

  private <R, T> Mono<Long> evictCommandResponse(Command<R, T> command, R request) {
    return Mono.fromCallable(() -> command.evictKeys(request))
      .map(keys -> keys.toArray(new String[0]))
      .flatMap(keys -> commandCache.evict(keys))
      .switchIfEmpty(Mono.just(0L));
  }
}
