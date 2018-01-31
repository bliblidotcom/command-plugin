package com.blibli.oss.command.sample.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author Eko Kurniawan Khannedy
 * @since 30/01/18
 */
public class HelloCommandTest {

  @Before
  public void setUp() throws Exception {
    HystrixRequestContext.initializeContext();
  }

  @Test
  public void testHello() {
    IntStream.of(10).forEach(i -> {
      String response = new HelloCommand("Eko").execute();
      assertEquals("Hello EKO", response);
    });
  }

  @Test
  public void testFallback() {
    String response = new HelloCommand(null).execute();
    assertEquals("Hello FALLBACK", response);
  }

  @After
  public void tearDown() throws Exception {
    HystrixRequestContext.getContextForCurrentThread().shutdown();
  }
}