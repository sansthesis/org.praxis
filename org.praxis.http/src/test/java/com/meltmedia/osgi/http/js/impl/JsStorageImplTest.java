package com.meltmedia.osgi.http.js.impl;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.meltmedia.osgi.http.js.JsMinifierService;

@RunWith(MockitoJUnitRunner.class)
public class JsStorageImplTest {

  private JsStorageImpl service;

  @Mock
  private JsMinifierService minifier;

  @After
  public void after() throws Exception {
    service.deactivate();
    service = null;
  }

  @Before
  public void before() throws Exception {
    service = new JsStorageImpl(minifier);
    BDDMockito.given(minifier.minify(Matchers.anyString())).willReturn("tiny{}");
    service.activate();
  }

  @Test
  public void testGetMinifiedJsColdCache() throws Exception {
    final String minifiedJs = service.getMinifiedJs("uncompressed.js");
    Mockito.verify(minifier).minify(Matchers.anyString());
  }

  @Test
  public void testGetMinifiedJsWarmCache() throws Exception {
    String minifiedJs = service.getMinifiedJs("uncompressed.js");
    minifiedJs = service.getMinifiedJs("uncompressed.js");
    minifiedJs = service.getMinifiedJs("uncompressed.js");
    Mockito.verify(minifier, Mockito.times(1)).minify(Matchers.anyString());
  }

  @Test
  public void testGetRawAndCompressedJsWarmCache() throws Exception {
    String js = service.getRawJs("uncompressed.js");
    js = service.getRawJs("uncompressed.js");
    js = service.getRawJs("uncompressed.js");
    Mockito.verify(minifier, Mockito.never()).minify(Matchers.anyString());
    String minifiedJs = service.getMinifiedJs("uncompressed.js");
    minifiedJs = service.getMinifiedJs("uncompressed.js");
    minifiedJs = service.getMinifiedJs("uncompressed.js");
    Mockito.verify(minifier, Mockito.times(1)).minify(Matchers.anyString());
    Assert.assertFalse(minifiedJs.equals(js));
  }

  @Test
  public void testGetRawJsColdCache() throws Exception {
    final String js = service.getRawJs("uncompressed.js");
    Mockito.verify(minifier, Mockito.never()).minify(Matchers.anyString());
  }

  @Test
  public void testGetRawJsWarmCache() throws Exception {
    String js = service.getRawJs("uncompressed.js");
    js = service.getRawJs("uncompressed.js");
    js = service.getRawJs("uncompressed.js");
    Mockito.verify(minifier, Mockito.never()).minify(Matchers.anyString());
  }

  @Test
  public void testHasRawJsColdCache() throws Exception {
    final boolean hasRawJs = service.hasRawJs("uncompressed.js");
    Assert.assertFalse(hasRawJs);
  }

  @Test
  public void testHasRawJsWarmCache() throws Exception {
    final String js = service.getRawJs("uncompressed.js");
    final boolean hasRawJs = service.hasRawJs("uncompressed.js");
    Assert.assertTrue(hasRawJs);
  }
}
