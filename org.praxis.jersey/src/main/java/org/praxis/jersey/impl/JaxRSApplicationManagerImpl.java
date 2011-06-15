package org.praxis.jersey.impl;

import java.util.HashMap;
import java.util.Map;

import org.praxis.jersey.JaxRSApplication;
import org.praxis.jersey.JaxRSApplicationManager;

public class JaxRSApplicationManagerImpl implements JaxRSApplicationManager {
  
  private Map<String, JaxRSApplication> applications = new HashMap<String, JaxRSApplication>();
  
  @Override
  public JaxRSApplication getApplication(String path) {
    return null;
  }

}
