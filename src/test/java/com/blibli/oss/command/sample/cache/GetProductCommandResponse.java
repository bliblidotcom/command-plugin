package com.blibli.oss.command.sample.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Eko Kurniawan Khannedy
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductCommandResponse {

  private String id;

  private String name;
}
