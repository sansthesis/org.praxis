package org.praxis.jersey;

/**
 * This class defines some of the OSGi service property names for compatibility.
 * @author Jason Rose
 */
public final class JaxRSResourceConstants {
  /**
   * The path that the Resource should be registered under, or applied to.
   */
  public static final String PROPERTY_APPLICATION_PATH = "jaxrs.application.path";

  /**
   * A boolean that notifies the whiteboard listener to ignore this resource; default false.
   */
  public static final String PROPERTY_IGNORE_RESOURCE = "jaxrs.ignore.resource";

  private JaxRSResourceConstants() {
  }

}
