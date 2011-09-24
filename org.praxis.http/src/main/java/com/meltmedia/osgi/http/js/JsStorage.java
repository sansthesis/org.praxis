package com.meltmedia.osgi.http.js;

public interface JsStorage {

  String getMinifiedJs(String resourcePath);

  String getRawJs(String resourcePath);

  boolean hasRawJs(String key);

}
