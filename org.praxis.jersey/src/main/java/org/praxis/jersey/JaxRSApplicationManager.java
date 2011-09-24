package org.praxis.jersey;

import java.util.Map;

/**
 * The JaxRSApplicationManager is a service that manages registration of JaxRSResources into their contexts specified by their context path.
 * @author Jason Rose
 * 
 */
public interface JaxRSApplicationManager {

  /**
   * This method binds a new JaxRSResource to the application specified in its path service property.
   * @param resource The new JaxRSResource to bind.
   * @param serviceProperties The registration properties of the service.
   */
  void bindResource(final JaxRSResource resource, final Map<String, ?> serviceProperties);

  /**
   * This method removes a JaxRSResource from the application specified in its path service property.
   * @param resource The new JaxRSResource to remove.
   * @param serviceProperties The registration properties of the service.
   */
  void unbindResource(final JaxRSResource resource, final Map<String, ?> serviceProperties);
}
