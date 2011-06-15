package org.praxis.jersey.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.praxis.jersey.JaxRSResource;
import org.praxis.jersey.JaxRSResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(metatype = true, immediate = true)
@Service
public class TestResource implements JaxRSResource {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Property(value = "/foobar")
  public static final String PROPERTY_APPLICATION_PATH = JaxRSResourceConstants.PROPERTY_APPLICATION_PATH;
  
  @Activate
  protected void activate() {
    log.info("Activating.");
  }
}
