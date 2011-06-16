package org.praxis.jersey;

import org.osgi.framework.ServiceReference;

/**
 * The JaxRSApplicationManager is a service that manages registration of JaxRSResources into their contexts specified by their context path.
 * @author Jason Rose
 * 
 */
public interface JaxRSApplicationManager {

  /**
   * This method binds a new JaxRSResource to the application specified in its path service property.
   * @param resource The new JaxRSResource to bind.
   * @param reference The ServiceReference to pull registration properties from.
   */
  void bindResource(final JaxRSResource resource, final ServiceReference reference);

  /**
   * This method removes a JaxRSResource from the application specified in its path service property.
   * @param resource The new JaxRSResource to remove.
   * @param reference The ServiceReference to pull registration properties from.
   */
  void unbindResource(final JaxRSResource resource, final ServiceReference reference);
}
