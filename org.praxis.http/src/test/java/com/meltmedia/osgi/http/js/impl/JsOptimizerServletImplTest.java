package com.meltmedia.osgi.http.js.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsOptimizerServletImplTest {

  private JsOptimizerServletImpl service;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  private PrintWriter output;

  private StringWriter buffer;

  private JsStorageImpl cache;

  @Before
  public void before() throws Exception {
    cache = Mockito.spy(new JsStorageImpl(new JsMinifierServiceImpl()));
    cache.activate();
    service = new JsOptimizerServletImpl(cache);
    buffer = new StringWriter();
    output = new PrintWriter(buffer);
    BDDMockito.given(request.getMethod()).willReturn("GET");
    BDDMockito.given(request.getProtocol()).willReturn("HTTP/1.1");
    BDDMockito.given(response.getWriter()).willReturn(output);
  }

  @Test
  public void testNoPropertiesFails() throws Exception {
    service.service(request, response);
    cache.deactivate();
    Mockito.verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
    output.close();
  }

  @Test
  public void testPathSucceeds() throws Exception {
    BDDMockito.given(request.getParameter("js")).willReturn("uncompressed.js");
    BDDMockito.given(request.getParameterValues("js")).willReturn(new String[] { "uncompressed.js" });
    service.service(request, response);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    final String output = buffer.toString();
    Mockito.verify(cache).getMinifiedJs("uncompressed.js");
    Assert.assertNotNull(output);
  }

  @Test
  public void testPathSucceedsNoMinification() throws Exception {
    BDDMockito.given(request.getParameter("js")).willReturn("uncompressed.js");
    BDDMockito.given(request.getParameterValues("js")).willReturn(new String[] { "uncompressed.js" });
    BDDMockito.given(request.getParameter("compress")).willReturn("false");
    service.service(request, response);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    Mockito.verify(cache).getRawJs("uncompressed.js");
    final String output = buffer.toString();
    Assert.assertNotNull(output);
  }
}
