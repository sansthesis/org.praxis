package org.praxis.blog.jersey.hateoas;

import org.praxis.blog.Entity;

public interface EntityResource<E extends Entity> extends Resource {

  EntityResource<E> wrap(E entity);

  E unwrap();

}
