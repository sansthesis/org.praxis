package org.praxis.jersey;

import java.util.Dictionary;
import java.util.Set;

import org.osgi.service.http.HttpContext;

public interface JaxRSApplication {

  String getPath();
  HttpContext getHttpContext();
  Set<JaxRSResource> getResources();
  Dictionary<String, String> getInitParams();
}
