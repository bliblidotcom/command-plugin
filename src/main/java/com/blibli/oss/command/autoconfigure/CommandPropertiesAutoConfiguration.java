package com.blibli.oss.command.autoconfigure;

import com.blibli.oss.command.properties.CommandProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@EnableConfigurationProperties({
    CommandProperties.class
})
public class CommandPropertiesAutoConfiguration {

}
