package com.blibli.oss.command.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author Eko Kurniawan Khannedy
 * @since 31/01/18
 */
@Data
@ConfigurationProperties("command")
public class CommandProperties {

  private CacheProperties cache = new CacheProperties();

  @Data
  public static class CacheProperties {

    private boolean enabled = false;

    private Long timeout = 10L;

    private ChronoUnit timeoutUnit = ChronoUnit.MINUTES;

    public Duration getTimeoutDuration() {
      return Duration.of(
        timeout, timeoutUnit
      );
    }

  }

}
