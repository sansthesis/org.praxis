package org.praxis.jersey;

import java.util.Dictionary;
import java.util.Set;

import org.osgi.service.http.HttpContext;

/**
 * A JaxRSApplication is an interface for wrapping the Jersey Application type.
 * @author Jason Rose
 */
public interface JaxRSApplication {

  /**
   * The HttpContext to use for injecting application-specific contextual data.
   * @return The HttpContext to use.
   */
  HttpContext getHttpContext();

  /**
   * Returns the init parameters for use in the servlet context.
   * @return The init parameters for use in the servlet context.
   */
  Dictionary<String, String> getInitParams();

  /**
   * Returns the context path for all JaxRSResources in this Application.
   * @return The context path for all JaxRSResources in this Application.
   */
  String getPath();

  /**
   * Returns all the objects we want injected into the Jersey Application.
   * @return All the objects we want injected into the Jersey Application.
   */
  Set<JaxRSResource> getResources();
}
