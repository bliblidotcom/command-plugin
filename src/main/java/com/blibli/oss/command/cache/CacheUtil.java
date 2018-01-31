package com.blibli.oss.command.cache;

import com.blibli.oss.command.Command;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CacheUtil {

  public static <R, T> void cacheCommandResponse(CommandCache commandCache, CommandCacheMapper commandCacheMapper,
                                                 Command<R, T> command, R request, T response) {
    String cacheKey = command.cacheKey(request);
    if (cacheKey != null) {
      String result = commandCacheMapper.toString(response);
      commandCache.cache(cacheKey, result);
    }
  }

  public static <R, T> void evictCommandResponse(CommandCache commandCache, Command<R, T> command, R request) {
    String evictKey = command.evictKey(request);
    if (evictKey != null) {
      commandCache.evict(evictKey);
    }
  }

}
