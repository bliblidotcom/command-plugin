package com.blibli.oss.command.sample.cache;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Eko Kurniawan Khannedy
 */
@Data
@Builder
public class GetProductCommandRequest {

  @NotBlank(message = "NotBlank")
  private String id;
}
