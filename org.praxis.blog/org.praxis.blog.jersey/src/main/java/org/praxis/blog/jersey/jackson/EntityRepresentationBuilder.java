package org.praxis.blog.jersey.jackson;

import org.praxis.blog.Entity;

public interface EntityRepresentationBuilder {

  <E extends Entity> E buildEntityRepresentation(E entity);
}
