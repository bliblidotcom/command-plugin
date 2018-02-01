package com.blibli.oss.command.sample.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rx.Single;

/**
 * @author Eko Kurniawan Khannedy
 */
@Slf4j
@Component
public class GetProductCommandImpl implements GetProductCommand {

  @Override
  public Single<GetProductCommandResponse> execute(GetProductCommandRequest request) {
    return Single.create(singleSubscriber -> {
      log.info("Execute GetProductCommand");
      singleSubscriber.onSuccess(GetProductCommandResponse.builder()
          .id(request.getId())
          .name(request.getId())
          .build());
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
}
