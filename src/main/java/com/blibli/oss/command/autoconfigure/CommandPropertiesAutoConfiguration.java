package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.properties.CommandProperties;
import com.blibli.oss.command.properties.SchedulerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@EnableConfigurationProperties({
  CommandProperties.class,
  SchedulerProperties.class
})
public class CommandPropertiesAutoConfiguration {

}
