package com.blibli.oss.command.cache;

/**
 * @author Eko Kurniawan Khannedy
 */
public interface CommandCache {

  String get(String key);

  void cache(String key, String value);

  void evict(String key);

}
