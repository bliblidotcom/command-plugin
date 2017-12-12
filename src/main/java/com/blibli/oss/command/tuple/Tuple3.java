package com.blibli.oss.command.tuple;

/**
 * @author Eko Kurniawan Khannedy
 */
public class Tuple3<FIRST, SECOND, THIRD> {

  private FIRST first;

  private SECOND second;

  private THIRD third;

  public Tuple3(FIRST first, SECOND second, THIRD third) {
    this.first = first;
    this.second = second;
    this.third = third;
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
}
