package com.blibli.oss.command.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
@Data
@ConfigurationProperties("command")
public class CommandProperties {

  private HystrixProperties hystrix = new HystrixProperties();

  private CacheProperties cache = new CacheProperties();

  @Data
  public static class HystrixProperties {

    private boolean enabled = false;
  }

  @Data
  public static class CacheProperties {

    private boolean enabled = false;

    private Long timeout = 10L;

    private TimeUnit timeoutUnit = TimeUnit.MINUTES;

  }

}
