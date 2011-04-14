package org.praxis.blog.jersey.hateoas;

import org.praxis.blog.Entity;

public interface EntityResource<E extends Entity> extends Resource {

  E unwrap();

  EntityResource<E> wrap(E entity);

}
