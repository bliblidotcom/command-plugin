package com.blibli.oss.command.sample.upper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rx.Single;

/**
 * @author Eko Kurniawan Khannedy
 * @since 30/01/18
 */
@Slf4j
@Component
public class UpperCommandImpl implements UpperCommand {

  @Override
  public Single<String> execute(UpperCommandRequest request) {
    return single(() -> request.getName().toUpperCase());
  }

  @Override
  public Single<String> fallback(Throwable throwable, UpperCommandRequest request) {
    log.warn("Error while invoking upper command", throwable);
    return Single.just("FALLBACK");
  }
}
