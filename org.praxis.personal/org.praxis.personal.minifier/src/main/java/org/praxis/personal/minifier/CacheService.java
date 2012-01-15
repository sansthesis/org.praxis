package org.praxis.personal.minifier;

public interface CacheService {

  boolean containsKey(String key);

  void put(String key, String value);

  String get(String key);

}
