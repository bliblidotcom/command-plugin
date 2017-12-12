package com.blibli.oss.command.tuple;

/**
 * @author Eko Kurniawan Khannedy
 */
public class Tuple4<FIRST, SECOND, THIRD, FORTH> {

  private FIRST first;

  private SECOND second;

  private THIRD third;

  private FORTH forth;

  public Tuple4(FIRST first, SECOND second, THIRD third, FORTH forth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.forth = forth;
  }

  public FIRST getFirst() {
    return first;
  }

  public SECOND getSecond() {
    return second;
  }

  public THIRD getThird() {
    return third;
  }

  public FORTH getForth() {
    return forth;
  }
}
