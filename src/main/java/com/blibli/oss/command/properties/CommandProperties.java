package com.blibli.oss.command.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
@Data
@ConfigurationProperties("command")
public class CommandProperties {

  private HystrixProperties hystrix = new HystrixProperties();

  @Data
  public static class HystrixProperties {

    private boolean enabled = false;
  }

}
