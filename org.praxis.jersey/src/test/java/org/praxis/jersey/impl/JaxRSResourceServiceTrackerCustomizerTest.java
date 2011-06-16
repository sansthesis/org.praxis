package org.praxis.jersey.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.praxis.jersey.JaxRSApplicationManager;
import org.praxis.jersey.JaxRSResource;

@RunWith(MockitoJUnitRunner.class)
public class JaxRSResourceServiceTrackerCustomizerTest {
  private JaxRSResourceServiceTrackerCustomizer service;

  @Mock
  private BundleContext bundleContext;

  @Mock
  private JaxRSApplicationManager manager;

  @Mock
  private JaxRSResource resource;

  @Mock
  private ServiceReference reference;

  @Before
  public void setUp() throws Exception {
    service = new JaxRSResourceServiceTrackerCustomizer(bundleContext, manager);
    given(bundleContext.getService(eq(reference))).willReturn(resource);
  }

  @Test
  public void testAddingServiceBindsResource() throws Exception {
    final Object rs = service.addingService(reference);
    verify(manager).bindResource(eq(resource), eq(reference));
    assertEquals(rs, resource);
  }

  @Test
  public void testModifiedServiceUnbindsResource() throws Exception {
    service.modifiedService(reference, resource);
    verify(manager).unbindResource(eq(resource), eq(reference));
    verify(manager).bindResource(eq(resource), eq(reference));
  }

  @Test
  public void testRemovedServiceBindsResource() throws Exception {
    service.removedService(reference, resource);
    verify(manager).unbindResource(eq(resource), eq(reference));
  }
}
