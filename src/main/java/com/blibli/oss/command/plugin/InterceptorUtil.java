package com.blibli.oss.command.plugin;

import com.blibli.oss.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;

import java.util.*;

/**
 * @author Eko Kurniawan Khannedy
 * @since 01/02/18
 */
@Slf4j
public class InterceptorUtil {

  public static List<CommandInterceptor> getCommandInterceptors(ApplicationContext applicationContext) {
    List<CommandInterceptor> interceptors = Collections.emptyList();

    Map<String, CommandInterceptor> beans = applicationContext.getBeansOfType(CommandInterceptor.class);
    if (beans != null && !beans.isEmpty()) {
      interceptors = new ArrayList<>(beans.values());
    }

    OrderComparator.sort(interceptors);
    return interceptors;
  }

  public static <R, T> T beforeExecute(Collection<CommandInterceptor> commandInterceptors, Command<R, T> command, R request) {
    for (CommandInterceptor interceptor : commandInterceptors) {
      try {
        T result = interceptor.beforeExecute(command, request);
        if (result != null) {
          return result;
        }
      } catch (Throwable throwable) {
        log.warn("Error beforeExecute() interceptor " + interceptor.getClass().getName(), throwable);
      }
    }
    return null;
  }

  public static <R, T> void afterSuccessExecute(Collection<CommandInterceptor> commandInterceptors, Command<R, T> command, R request, T response) {
    commandInterceptors.forEach(interceptor -> {
      try {
        interceptor.afterSuccessExecute(command, request, response);
      } catch (Throwable throwable) {
        log.warn("Error afterSuccessExecute() interceptor " + interceptor.getClass().getName(), throwable);
      }
    });
  }

  public static <R, T> void afterFailedExecute(Collection<CommandInterceptor> commandInterceptors, Command<R, T> command, R request, Throwable throwable) {
    commandInterceptors.forEach(interceptor -> {
      try {
        interceptor.afterFailedExecute(command, request, throwable);
      } catch (Throwable error) {
        log.warn("Error afterFailedExecute() interceptor " + interceptor.getClass().getName(), error);
      }
    });
  }

}
