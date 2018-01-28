package com.blibli.oss.command.tuple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Eko Kurniawan Khannedy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tuple2<FIRST, SECOND> {

  private FIRST first;

  private SECOND second;
}
