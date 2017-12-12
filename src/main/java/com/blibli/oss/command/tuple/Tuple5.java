package com.blibli.oss.command.tuple;

/**
 * @author Eko Kurniawan Khannedy
 */
public class Tuple5<FIRST, SECOND, THIRD, FORTH, FIFTH> {

  private FIRST first;

  private SECOND second;

  private THIRD third;

  private FORTH forth;

  private FIFTH fifth;

  public Tuple5(FIRST first, SECOND second, THIRD third, FORTH forth, FIFTH fifth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.forth = forth;
    this.fifth = fifth;
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

  public FIFTH getFifth() {
    return fifth;
  }
}
