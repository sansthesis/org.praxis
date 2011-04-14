package org.praxis.blog.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.praxis.blog.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(componentAbstract = true)
public abstract class AbstractDao<E extends Entity> implements Dao<E> {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  protected Class<E> entityClass;

  @Override
  public E create(final E entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean delete(final E entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E get(final long id) {
    final E entity = createEntity();
    entity.setId(id);
    return entity;
  }

  @Override
  public List<E> list() {
    final List<E> list = new ArrayList<E>();
    for( int i = 0; i < 5; i++ ) {
      final E entity = createEntity();
      entity.setId(i);
      list.add(entity);
    }
    return list;
  }

  @Override
  public List<E> listByRelation(final String relationName, final long relationId) {
    return list();
  }

  @Override
  public E update(final E entity) {
    throw new UnsupportedOperationException();
  }

  private E createEntity() {
    E entity;
    try {
      entity = entityClass.newInstance();
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
    return entity;
  }

}
