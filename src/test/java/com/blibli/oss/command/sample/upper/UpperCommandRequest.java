package com.blibli.oss.command.sample.upper;

import lombok.Builder;
import lombok.Data;

/**
 * @author Eko Kurniawan Khannedy
 * @since 30/01/18
 */
@Builder
@Data
public class UpperCommandRequest {

  private String name;
}
