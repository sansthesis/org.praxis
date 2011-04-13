package org.praxis.blog.dao;

import java.util.List;

import org.praxis.blog.Entity;

public interface Dao<E extends Entity> {

  E create(E entity);

  boolean delete(E entity);

  E get(long id);

  List<E> list();

  E update(E entity);
}
