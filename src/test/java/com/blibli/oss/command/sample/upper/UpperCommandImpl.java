package com.blibli.oss.command.sample.upper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Eko Kurniawan Khannedy
 * @since 30/01/18
 */
@Slf4j
@Component
public class UpperCommandImpl implements UpperCommand {

  @Override
  public Mono<String> execute(UpperCommandRequest request) {
    return mono(() -> request.getName().toUpperCase());
  }

  @Override
  public Mono<String> fallback(Throwable throwable, UpperCommandRequest request) {
    log.warn("Error while invoking upper command", throwable);
    return Mono.just("FALLBACK");
  }
}
