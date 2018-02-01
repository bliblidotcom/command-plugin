package com.blibli.oss.command.cache.impl;

import com.blibli.oss.command.cache.CommandCacheMapper;
import com.blibli.oss.command.exception.CommandRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheMapperImpl implements CommandCacheMapper {

  private ObjectMapper objectMapper;

  public CommandCacheMapperImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> String toString(T object) {
    if (object instanceof String) {
      return (String) object;
    }

    try {
      return objectMapper.writeValueAsString(object);
    } catch (Throwable e) {
      throw new CommandRuntimeException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T fromString(String value, Class<T> clazz) {
    if (clazz.equals(String.class)) {
      return (T) value;
    }

    try {
      return objectMapper.readValue(value, clazz);
    } catch (Throwable e) {
      throw new CommandRuntimeException(e);
    }
  }
}
