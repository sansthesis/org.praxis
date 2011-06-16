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

import javax.servlet.Servlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
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
  private BundleContext bundleContext;

  @Mock
  private ComponentContext componentContext;

  @Mock
  private JaxRSResource resource;

  @Mock
  private ServiceReference reference;

  @Mock
  private Filter filter;

  @Before
  public void setUp() throws Exception {
    service = new JaxRSApplicationManagerImpl(httpService);
    given(componentContext.getBundleContext()).willReturn(bundleContext);
    given(bundleContext.createFilter(anyString())).willReturn(filter);
    given(reference.getProperty(eq(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH))).willReturn("/test");
    given(reference.getProperty(eq(JaxRSResourceConstants.PROPERTY_IGNORE_RESOURCE))).willReturn(false);
  }

  @Test
  public void testActivateBuildsServiceTracker() throws Exception {
    service.activate(componentContext);
    verify(componentContext).getBundleContext();
    verify(bundleContext).createFilter(anyString());
  }

  @Test
  public void testBindResourceCreatesServlet() throws Exception {
    service.activate(componentContext);
    service.bindResource(resource, reference);
    verify(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
  }

  @Test
  public void testBindResourceExceptionDoesNothing() throws Exception {
    BDDMockito.willThrow(new IllegalArgumentException()).given(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    service.activate(componentContext);
    service.bindResource(resource, reference);
    verify(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
  }

  @Test
  public void testBindResourcesCreatesAndDestroysServlet() throws Exception {
    service.activate(componentContext);
    service.bindResource(resource, reference);
    final JaxRSResource resource2 = mock(JaxRSResource.class);
    final ServiceReference reference2 = mock(ServiceReference.class);
    given(reference2.getProperty(eq(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH))).willReturn("/test");
    given(reference2.getProperty(eq(JaxRSResourceConstants.PROPERTY_IGNORE_RESOURCE))).willReturn(false);
    service.bindResource(resource2, reference2);
    verify(httpService, times(2)).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    verify(httpService, atLeast(2)).unregister("/test");
  }

  @Test
  public void testDeactivateUnbindsApplication() throws Exception {
    service.activate(componentContext);
    service.bindResource(resource, reference);
    service.deactivate();
    verify(httpService, atLeast(1)).unregister("/test");
  }

  @Test
  public void testUnbindContextEmptyContextExceptionDoesNothing() throws Exception {
    BDDMockito.willThrow(new IllegalArgumentException()).given(httpService).unregister(anyString());
    service.activate(componentContext);
    service.bindResource(resource, reference);
    service.unbindResource(resource, reference);
    verify(httpService).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    verify(httpService).unregister("/test");
  }

  @Test
  public void testUnbindContextExceptionDoesNothing() throws Exception {
    BDDMockito.willThrow(new RuntimeException()).given(httpService).unregister(anyString());
    service.activate(componentContext);
    service.bindResource(resource, reference);
    service.unbindResource(resource, reference);
  }

  @Test
  public void testUnbindEmptyContextDoesNothing() throws Exception {
    BDDMockito.willThrow(new IllegalArgumentException()).given(httpService).unregister(anyString());
    service.activate(componentContext);
    service.unbindResource(resource, reference);
    Mockito.verifyZeroInteractions(httpService);
  }

  @Test
  public void testUnbindResourcesWorksWithMultipleResources() throws Exception {
    service.activate(componentContext);
    service.bindResource(resource, reference);
    final JaxRSResource resource2 = mock(JaxRSResource.class);
    final JaxRSResource resource3 = mock(JaxRSResource.class);
    service.bindResource(resource2, reference);
    service.bindResource(resource3, reference);
    service.unbindResource(resource3, reference);
    verify(httpService, times(4)).registerServlet(eq("/test"), any(Servlet.class), any(Dictionary.class), any(HttpContext.class));
    verify(httpService, times(4)).unregister("/test");
  }
}
