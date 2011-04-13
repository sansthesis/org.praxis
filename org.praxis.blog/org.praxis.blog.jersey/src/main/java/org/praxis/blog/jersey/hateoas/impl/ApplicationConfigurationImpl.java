package org.praxis.blog.jersey.hateoas.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.praxis.blog.jersey.hateoas.ApplicationConfiguration;

@Component(metatype = true, immediate = true)
@Service
public class ApplicationConfigurationImpl implements ApplicationConfiguration {

  @Property(label = "Jersey Context Path", value = "/hateoas")
  public static final String PROPERTY_CONTEXT_PATH = "context.path";
  private String contextPath;

  @Override
  public String getContextPath() {
    return contextPath;
  }

  @Activate
  protected void activate(final ComponentContext context) {
    contextPath = (String) context.getProperties().get(PROPERTY_CONTEXT_PATH);
  }

}
