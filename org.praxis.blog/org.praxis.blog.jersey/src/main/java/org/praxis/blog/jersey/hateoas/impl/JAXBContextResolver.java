package org.praxis.blog.jersey.hateoas.impl;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.BlogResourceListRepresentation;
import org.praxis.blog.jersey.hateoas.om.BlogResourceRepresentation;
import org.praxis.blog.jersey.hateoas.om.StoryResourceListRepresentation;
import org.praxis.blog.jersey.hateoas.om.StoryResourceRepresentation;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

  private final JAXBContext context;
  private final Class<?>[] types = { BlogResourceRepresentation.class, BlogResourceListRepresentation.class, StoryResourceListRepresentation.class, StoryResourceRepresentation.class, Link.class };

  public JAXBContextResolver() throws Exception {
    this.context = new JSONJAXBContext(JSONConfiguration.natural().rootUnwrapping(true).build(), types);
  }

  @Override
  public JAXBContext getContext(final Class<?> objectType) {
    for( final Class<?> type : types ) {
      if( type == objectType ) {
        return context;
      }
    }
    return null;
  }
}