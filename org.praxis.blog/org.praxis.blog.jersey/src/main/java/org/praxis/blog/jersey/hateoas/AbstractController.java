package org.praxis.blog.jersey.hateoas;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(componentAbstract = true)
public class AbstractController {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  @Reference
  private ApplicationConfiguration configuration;

  protected ApplicationConfiguration getApplicationConfiguration() {
    return configuration;
  }
}
