package com.blibli.oss.command.cache;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.plugin.CommandInterceptor;
import com.blibli.oss.command.properties.CommandProperties;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
public class CommandCacheInterceptor implements CommandInterceptor {

  private CommandProperties commandProperties;

  private CommandCache commandCache;

  private CommandCacheMapper commandCacheMapper;

  public CommandCacheInterceptor(CommandProperties commandProperties,
                                 CommandCache commandCache,
                                 CommandCacheMapper commandCacheMapper) {
    this.commandProperties = commandProperties;
    this.commandCache = commandCache;
    this.commandCacheMapper = commandCacheMapper;
  }

  @Override
  public <R, T> T beforeExecute(Command<R, T> command, R request) {
    if (!commandProperties.getCache().isEnabled()) {
      return null;
    }

    String cacheKey = command.cacheKey(request);
    if (cacheKey == null) {
      return null;
    }

    String result = commandCache.get(cacheKey);
    if (result == null) {
      return null;
    }

    Class<T> responseClass = command.responseClass();
    return commandCacheMapper.fromString(result, responseClass);
  }

  @Override
  public <R, T> void afterSuccessExecute(Command<R, T> command, R request, T response) {
    if (commandProperties.getCache().isEnabled()) {
      evictCommandResponse(command, request);
      cacheCommandResponse(command, request, response);
    }
  }

  private <R, T> void cacheCommandResponse(Command<R, T> command, R request, T response) {
    String cacheKey = command.cacheKey(request);
    if (cacheKey != null) {
      String result = commandCacheMapper.toString(response);
      commandCache.cache(cacheKey, result);
    }
  }

  private <R, T> void evictCommandResponse(Command<R, T> command, R request) {
    String evictKey = command.evictKey(request);
    if (evictKey != null) {
      commandCache.evict(evictKey);
    }
  }
}
