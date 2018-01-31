package com.blibli.oss.command.sample.cache;

import lombok.Builder;
import lombok.Data;

/**
 * @author Eko Kurniawan Khannedy
 */
@Data
@Builder
public class GetProductCommandResponse {

  private String id;

  private String name;
}
