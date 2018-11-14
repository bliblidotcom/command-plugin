package com.blibli.oss.command.sample.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eko Kurniawan Khannedy
 */
@Slf4j
@Component
public class GetProductCommandImpl implements GetProductCommand {

  @Override
  public Mono<GetProductCommandResponse> execute(GetProductCommandRequest request) {
    return mono(() -> {
      log.info("Execute GetProductCommand");
      return GetProductCommandResponse.builder()
        .id(request.getId())
        .name(request.getId())
        .build();
    });
  }

  @Override
  public String cacheKey(GetProductCommandRequest request) {
    return request.getId();
  }

  @Override
  public Class<GetProductCommandResponse> responseClass() {
    return GetProductCommandResponse.class;
  }

  @Override
  public Collection<String> evictKeys(GetProductCommandRequest request) {
    return Collections.singletonList(request.getId());
  }
}
