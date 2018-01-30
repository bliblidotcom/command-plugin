package com.blibli.oss.command.sample.upper;

import com.blibli.oss.command.CommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Eko Kurniawan Khannedy
 * @since 30/01/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UpperCommandImplTest.Application.class)
public class UpperCommandImplTest {

  @Autowired
  private CommandExecutor commandExecutor;

  private UpperCommandRequest request;

  @Test
  public void testSuccess() {
    request = UpperCommandRequest.builder().name("Eko Kurniawan Khannedy").build();
    String response = commandExecutor.execute(UpperCommand.class, request).toBlocking().value();

    assertEquals("EKO KURNIAWAN KHANNEDY", response);
  }

  @Test
  public void testFallback() {
    request = UpperCommandRequest.builder().build();
    String response = commandExecutor.execute(UpperCommand.class, request).toBlocking().value();

    assertEquals("FALLBACK", response);
  }

  @SpringBootApplication
  public static class Application {

  }

}