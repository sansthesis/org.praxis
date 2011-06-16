package org.praxis.jersey.impl;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.praxis.jersey.JaxRSResource;

@RunWith(MockitoJUnitRunner.class)
public class JaxRSApplicationImplTest {
  
  private JaxRSApplicationImpl service;
  
  @Before
  public void setUp() throws Exception {
    service = new JaxRSApplicationImpl(null, null, null, null);
  }
  
  @Test
  public void testGetSingletonsReturnsResources() {
    assertNull(service.getSingletons());
    service.setResources(new HashSet<JaxRSResource>());
    assertEquals(new HashSet<JaxRSResource>(), service.getSingletons());
  }
}
