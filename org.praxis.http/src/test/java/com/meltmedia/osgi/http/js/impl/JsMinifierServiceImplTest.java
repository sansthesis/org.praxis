package com.meltmedia.osgi.http.js.impl;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsMinifierServiceImplTest {

  private JsMinifierServiceImpl service;

  @Before
  public void before() throws Exception {
    service = new JsMinifierServiceImpl();
  }

  @Test
  public void testMinify() throws Exception {
    final String inputLocation = "uncompressed.js";
    final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputLocation);
    final String inputString = IOUtils.toString(in);
    IOUtils.closeQuietly(in);
    final String outputString = service.minify(inputString);
    final int inputLength = inputString.length();
    final int outputLength = outputString.length();
    Assert.assertTrue(inputLength > outputLength);
  }
}
