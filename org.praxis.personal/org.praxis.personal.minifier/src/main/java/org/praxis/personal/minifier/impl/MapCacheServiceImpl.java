package org.praxis.personal.minifier.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.praxis.personal.minifier.CacheService;

@Component
@Service
public class MapCacheServiceImpl implements CacheService {

  private Map<String, String> delegate = new HashMap<String, String>();

  @Override
  public boolean containsKey(final String key) {
    return delegate.containsKey(key);
  }

  @Override
  public String get(final String key) {
    return delegate.get(key);
  }

  @Override
  public void put(final String key, final String value) {
    delegate.put(key, value);
  }

  @Activate
  public void activate() {
    delegate = new HashMap<String, String>();
  }

  @Deactivate
  public void deactivate() {
    delegate = new HashMap<String, String>();
  }

}
