package org.praxis.blog.jersey.jackson.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.praxis.blog.jersey.jackson.ResourceRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityRepresentationInvocationHandler implements InvocationHandler {
  private final Logger log = LoggerFactory.getLogger(getClass());
  private final Object entity;
  private final ResourceRepresentation links;

  public EntityRepresentationInvocationHandler(final Object entity, final ResourceRepresentation links) {
    this.entity = entity;
    this.links = links;
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    log.info("Invoking {} method.", method.getName());
    Object target = null;
    if( "getLinks".equals(method.getName()) || "setLinks".equals(method.getName()) ) {
      target = links;
    } else {
      target = entity;
    }
    final Object result = method.invoke(target, args);
    return result;
  }

}
