package com.blibli.oss.command.tuple;

/**
 * @author Eko Kurniawan Khannedy
 */
public class Tuple {

  public static <FIRST, SECOND> Tuple2<FIRST, SECOND> of(FIRST first, SECOND second) {
    return new Tuple2<>(first, second);
  }

  public static <FIRST, SECOND, THIRD> Tuple3<FIRST, SECOND, THIRD> of(FIRST first, SECOND second, THIRD third) {
    return new Tuple3<>(first, second, third);
  }

  public static <FIRST, SECOND, THIRD, FORTH> Tuple4<FIRST, SECOND, THIRD, FORTH> of(FIRST first, SECOND second, THIRD third, FORTH forth) {
    return new Tuple4<>(first, second, third, forth);
  }

  public static <FIRST, SECOND, THIRD, FORTH, FIFTH> Tuple5<FIRST, SECOND, THIRD, FORTH, FIFTH> of(FIRST first, SECOND second, THIRD third, FORTH forth, FIFTH fifth) {
    return new Tuple5<>(first, second, third, forth, fifth);
  }

}
