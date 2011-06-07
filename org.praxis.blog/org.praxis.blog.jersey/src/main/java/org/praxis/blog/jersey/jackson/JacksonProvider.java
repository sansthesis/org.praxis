package org.praxis.blog.jersey.jackson;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

public interface JacksonProvider {

  JacksonJsonProvider getJacksonJsonProvider();

}
