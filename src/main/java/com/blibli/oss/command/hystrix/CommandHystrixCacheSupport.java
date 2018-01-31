package com.blibli.oss.command.hystrix;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.cache.CacheUtil;
import com.blibli.oss.command.cache.CommandCache;
import com.blibli.oss.command.cache.CommandCacheMapper;
import rx.Observable;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
public class CommandHystrixCacheSupport<R, T> extends CommandHystrix<R, T> {

  private CommandCache commandCache;

  private CommandCacheMapper commandCacheMapper;

  public CommandHystrixCacheSupport(Command<R, T> command, R request, String commandKey, String commandGroup,
                                    CommandCache commandCache, CommandCacheMapper commandCacheMapper) {
    super(command, request, commandKey, commandGroup);

    this.commandCache = commandCache;
    this.commandCacheMapper = commandCacheMapper;
  }

  @Override
  protected Observable<T> construct() {
    return command.execute(request)
        .doOnSuccess(response -> {
          CacheUtil.evictCommandResponse(commandCache, command, request);
          CacheUtil.cacheCommandResponse(commandCache, commandCacheMapper, command, request, response);
        })
        .toObservable();
  }

}
