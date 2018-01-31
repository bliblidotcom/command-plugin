package com.blibli.oss.command.sample.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author Eko Kurniawan Khannedy
 * @since 30/01/18
 */
public class HelloCommand extends HystrixCommand<String> {

  private String name;

  public HelloCommand(String name) {
    super(HystrixCommandGroupKey.Factory.asKey("Hello"));
    this.name = name;
  }

  @Override
  protected String run() {
    System.out.println("INVOKE THIS");
    return "Hello " + name.toUpperCase();
  }

  @Override
  protected String getFallback() {
    return "Hello FALLBACK";
  }

  @Override
  protected String getCacheKey() {
    return name;
  }
}
