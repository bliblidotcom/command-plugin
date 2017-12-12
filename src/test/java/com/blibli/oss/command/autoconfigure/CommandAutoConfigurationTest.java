package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.CommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Eko Kurniawan Khannedy
 */
@SpringBootTest(classes = CommandAutoConfigurationTest.TestConfiguration.class)
@RunWith(SpringRunner.class)
public class CommandAutoConfigurationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void testCommandExecutorMustExists() throws Exception {
    CommandExecutor bean = applicationContext.getBean(CommandExecutor.class);
    assertNotNull(bean);
  }

  @SpringBootApplication
  public static class TestConfiguration {

  }

}