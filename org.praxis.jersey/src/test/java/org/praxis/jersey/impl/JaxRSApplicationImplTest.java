package org.praxis.jersey.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.http.HttpContext;
import org.praxis.jersey.JaxRSResource;

@RunWith(MockitoJUnitRunner.class)
public class JaxRSApplicationImplTest {

  private JaxRSApplicationImpl service;

  @Mock
  private JaxRSResource resource;

  @Before
  public void setUp() throws Exception {
    service = new JaxRSApplicationImpl(null, null, new HashSet<JaxRSResource>(), null);
  }

  @Test
  public void testGetHttpContext() throws Exception {
    final HttpContext property = mock(HttpContext.class);
    final JaxRSApplicationImpl impl = new JaxRSApplicationImpl(null, property, null, null);
    final Object output = impl.getHttpContext();
    assertEquals(property, output);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetInitParams() throws Exception {
    final Dictionary<String, String> property = mock(Dictionary.class);
    final JaxRSApplicationImpl impl = new JaxRSApplicationImpl(null, null, null, property);
    final Object output = impl.getInitParams();
    assertEquals(property, output);
  }

  @Test
  public void testGetPath() throws Exception {
    final String property = "/aoeu";
    final JaxRSApplicationImpl impl = new JaxRSApplicationImpl(property, null, null, null);
    final Object output = impl.getPath();
    assertEquals(property, output);
  }

  @Test
  public void testGetSingletonsReturnsResources() {
    final Set<Object> singletons = service.getSingletons();
    assertEquals(0, singletons.size());
    service.getResources().add(resource);
    assertEquals(service.getResources(), service.getSingletons());
  }
}
