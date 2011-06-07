package org.praxis.blog.jersey.jackson.impl;

import java.lang.reflect.Proxy;

import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Entity;
import org.praxis.blog.jersey.jackson.EntityRepresentationBuilder;
import org.praxis.blog.jersey.jackson.ResourceRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, metatype = true)
@Service
public class EntityRepresentationBuilderImpl implements EntityRepresentationBuilder {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public <E extends Entity> E buildEntityRepresentation(final E entity) {
    final E representation = createProxy(entity);
    return representation;
  }

  private <E> E createProxy(final E entity) {
    final Class<?>[] interfaces = (Class<?>[]) ArrayUtils.add(entity.getClass().getInterfaces(), ResourceRepresentation.class);
    final E proxy = (E) Proxy.newProxyInstance(entity.getClass().getClassLoader(), interfaces, new EntityRepresentationInvocationHandler(entity, new ResourceRepresentationImpl()));
    return proxy;
  }

}
