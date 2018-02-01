package com.blibli.oss.command.cache;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface CommandCacheMapper {

  <T> String toString(T object);

  <T> T fromString(String value, Class<T> clazz);

}
