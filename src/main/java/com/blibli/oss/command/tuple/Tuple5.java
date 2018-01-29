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
public class Tuple5<FIRST, SECOND, THIRD, FORTH, FIFTH> {

  private FIRST first;

  private SECOND second;

  private THIRD third;

  private FORTH forth;

  private FIFTH fifth;
}
