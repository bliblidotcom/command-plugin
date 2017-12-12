package com.blibli.oss.command.tuple;

/**
 * @author Eko Kurniawan Khannedy
 */
public class Tuple2<FIRST, SECOND> {

  private FIRST first;

  private SECOND second;

  public Tuple2(FIRST first, SECOND second) {
    this.first = first;
    this.second = second;
  }

  public FIRST getFirst() {
    return first;
  }

  public SECOND getSecond() {
    return second;
  }
}
