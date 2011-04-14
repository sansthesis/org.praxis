package org.praxis.blog.jersey.hateoas;

import java.lang.reflect.Method;

import org.apache.felix.scr.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(componentAbstract = true)
public abstract class AbstractController {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  protected Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
    try {
      return clazz.getMethod(methodName, parameterTypes);
    } catch( final Exception e ) {
      throw new RuntimeException(e);
    }
  }
}
