package org.praxis.jersey.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Dictionary;
import java.util.Map;

import javax.servlet.Servlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.praxis.jersey.JaxRSResource;
import org.praxis.jersey.JaxRSResourceConstants;

@RunWith(MockitoJUnitRunner.class)
public class JaxRSApplicationManagerImplTest {

  private JaxRSApplicationManagerImpl service;

  @Mock
  private HttpService httpService;

  @Mock
  private JaxRSResource resource;
  
  @Mock
  private Map<String, Object> map;

  @Before
  public void setUp() throws Exception {
    service = new JaxRSApplicationManagerImpl(httpService);
    given(map.get(eq(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH))).willReturn("/test");
    given(map.get(eq(JaxRSResourceConstants.PROPERTY_IGNORE_RESOURCE))).willReturn(false);
  }

  @Test
  public void testBindResourceCreatesServlet() throws Exception {
    service.bindResource(resource, map);
    verify(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
  }

  @Test
  public void testBindResourceExceptionDoesNothing() throws Exception {
    BDDMockito.willThrow(new IllegalArgumentException()).given(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    service.bindResource(resource, map);
    verify(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
  }

  @Test
  public void testBindResourcesCreatesAndDestroysServlet() throws Exception {
    service.bindResource(resource, map);
    final JaxRSResource resource2 = mock(JaxRSResource.class);
    final Map<String, Object> map2 = mock(Map.class);
    given(map2.get(eq(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH))).willReturn("/test");
    given(map2.get(eq(JaxRSResourceConstants.PROPERTY_IGNORE_RESOURCE))).willReturn(false);
    service.bindResource(resource2, map2);
    verify(httpService, times(2)).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    verify(httpService, atLeast(2)).unregister("/test");
  }

  @Test
  public void testDeactivateUnbindsApplication() throws Exception {
    service.bindResource(resource, map);
    service.deactivate();
    verify(httpService, atLeast(1)).unregister("/test");
  }

  @Test
  public void testUnbindContextEmptyContextExceptionDoesNothing() throws Exception {
    BDDMockito.willThrow(new IllegalArgumentException()).given(httpService).unregister(anyString());
    service.bindResource(resource, map);
    service.unbindResource(resource, map);
    verify(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    verify(httpService, atLeast(1)).unregister("/test");
  }

  @Test
  public void testUnbindContextExceptionDoesNothing() throws Exception {
    BDDMockito.willThrow(new RuntimeException()).given(httpService).unregister(anyString());
    service.bindResource(resource, map);
    service.unbindResource(resource, map);
  }

  @Test
  public void testUnbindEmptyContextDoesNothing() throws Exception {
    BDDMockito.willThrow(new IllegalArgumentException()).given(httpService).unregister(anyString());
    service.unbindResource(resource, map);
    verify(httpService).unregister(eq("/test"));
  }

  @Test
  public void testUnbindResourcesWorksWithMultipleResources() throws Exception {
    service.bindResource(resource, map);
    final JaxRSResource resource2 = mock(JaxRSResource.class);
    final JaxRSResource resource3 = mock(JaxRSResource.class);
    service.bindResource(resource2, map);
    service.bindResource(resource3, map);
    service.unbindResource(resource3, map);
    verify(httpService, times(4)).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    verify(httpService, times(4)).unregister("/test");
  }
}
