package org.praxis.jersey.impl;

import java.util.Dictionary;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.osgi.service.http.HttpContext;
import org.praxis.jersey.JaxRSApplication;
import org.praxis.jersey.JaxRSResource;

/**
 * This class serves as an abstraction for wrapping the Jersey Application type. I hope to later allow applications to provide their own JaxRSApplication objects instead of directly tracking the JaxRSResources themselves.
 * @author Jason Rose
 */
public class JaxRSApplicationImpl extends Application implements JaxRSApplication {
  private String path;
  private HttpContext httpContext;
  private Set<JaxRSResource> resources;
  private Dictionary<String, String> initParams;

  public JaxRSApplicationImpl(final String path, final HttpContext httpContext, final Set<JaxRSResource> resources, final Dictionary<String, String> initParams) {
    super();
    this.path = path;
    this.httpContext = httpContext;
    this.resources = resources;
    this.initParams = initParams;
  }

  @Override
  public HttpContext getHttpContext() {
    return httpContext;
  }

  @Override
  public Dictionary<String, String> getInitParams() {
    return initParams;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public Set<JaxRSResource> getResources() {
    return resources;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<Object> getSingletons() {
    return (Set<Object>) (Set<?>) getResources();
  }

  public void setHttpContext(final HttpContext httpContext) {
    this.httpContext = httpContext;
  }

  public void setInitParams(final Dictionary<String, String> initParams) {
    this.initParams = initParams;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public void setResources(final Set<JaxRSResource> resources) {
    this.resources = resources;
  }

}
