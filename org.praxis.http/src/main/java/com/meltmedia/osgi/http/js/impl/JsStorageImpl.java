package com.meltmedia.osgi.http.js.impl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.meltmedia.osgi.http.js.JsMinifierService;
import com.meltmedia.osgi.http.js.JsStorage;

@Component
@Service
public class JsStorageImpl implements JsStorage {

  /**
   * The built-in java Callable class is annoying because its call() method doesn't allow parameters.
   * @author Jason Rose
   * 
   * @param <T> The type to return from invoke()
   */
  private abstract class Callable<T> {
    /**
     * The closure body.
     * @param context Any arguments you might want.
     * @return Hopefully something you'd expect.
     */
    public abstract T invoke(Object... context);
  }

  private Cache cache;
  private CacheManager cacheManager;

  @Reference
  private JsMinifierService jsMinifier;

  public JsStorageImpl() {
  }

  public JsStorageImpl(final JsMinifierService jsMinifier) {
    this.jsMinifier = jsMinifier;
  }

  @Activate
  public void activate() {
    cacheManager = new CacheManager();
    cache = new Cache("js_cache", 500, MemoryStoreEvictionPolicy.LRU, false, null, true, 0, 0, false, 0, null);
    cacheManager.addCache(cache);
  }

  @Deactivate
  public void deactivate() {
    cacheManager.removalAll();
    cacheManager.shutdown();
  }

  @Override
  public String getMinifiedJs(final String resourcePath) {
    final Callable<String> minifierCallable = new Callable<String>() {
      @Override
      public String invoke(final Object... context) {
        final String input = (String) context[0];
        return jsMinifier.minify(input);
      }
    };
    if( isInCache(cache, resourcePath, true) ) {
      return (String) cache.get(generateKey(resourcePath, true)).getValue();
    } else if( isUri(resourcePath) ) {
      return getJsFromURI(resourcePath, true, minifierCallable);
    } else {
      return getJsFromResourcePath(resourcePath, true, minifierCallable);
    }
  }

  @Override
  public String getRawJs(final String resourcePath) {
    final Callable<String> callable = new Callable<String>() {
      @Override
      public String invoke(final Object... context) {
        final String input = (String) context[0];
        return input;
      }
    };
    if( isInCache(cache, resourcePath, false) ) {
      return (String) cache.get(generateKey(resourcePath, false)).getValue();
    } else if( isUri(resourcePath) ) {
      return getJsFromURI(resourcePath, false, callable);
    } else {
      return getJsFromResourcePath(resourcePath, false, callable);
    }
  }

  @Override
  public boolean hasRawJs(final String resourcePath) {
    return isInCache(cache, resourcePath, false);
  }

  protected String getJsFromResourcePath(final String resourcePath, final boolean isMinified, final Callable<String> postProcessor) {
    final InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
    return getJsFromInputStream(resourcePath, in, isMinified, postProcessor);
  }

  private String generateKey(final String resourcePath, final boolean isMinified) {
    return isMinified + ":" + resourcePath;
  }

  private String getJsFromInputStream(final String path, final InputStream in, final boolean isMinified, final Callable<String> postProcessor) {
    final String output;

    try {
      final String input = IOUtils.toString(in);
      IOUtils.closeQuietly(in);
      output = postProcessor.invoke(input);
    } catch( final RuntimeException re ) {
      throw re;
    } catch( final Exception e ) {
      throw new RuntimeException(e);
    }
    cache.put(new Element(generateKey(path, isMinified), output));
    return output;
  }

  private String getJsFromURI(final String resourcePath, final boolean isMinified, final Callable<String> postProcessor) {
    try {
      final URI uri = new URI(resourcePath);
      return getJsFromInputStream(resourcePath, uri.toURL().openStream(), isMinified, postProcessor);
    } catch( final RuntimeException re ) {
      throw re;
    } catch( final Exception e ) {
      throw new RuntimeException(e);
    }
  }

  private boolean isInCache(final Cache theCache, final String resourcePath, final boolean isMinified) {
    final String key = generateKey(resourcePath, isMinified);
    return theCache.isKeyInCache(key);
  }

  private boolean isUri(final String resourcePath) {
    try {
      new URL(resourcePath);
      return true;
    } catch( final MalformedURLException use ) {
      return false;
    }
  }
}
