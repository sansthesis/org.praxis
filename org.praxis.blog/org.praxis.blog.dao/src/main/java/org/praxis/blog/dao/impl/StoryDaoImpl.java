package org.praxis.blog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Story;
import org.praxis.blog.dao.AbstractDao;
import org.praxis.blog.dao.StoryDao;

@Component(metatype = true, immediate = true)
@Service
public class StoryDaoImpl extends AbstractDao<Story> implements StoryDao {

  public StoryDaoImpl() {
    super();
    entityClass = Story.class;
  }

  @Override
  public List<Story> listByRelation(final String relationName, final long relationId) {
    final List<Story> all = list();
    final List<Story> list = new ArrayList<Story>();
    if( "blog".equals(relationName) ) {
      for( final Story entity : all ) {
        if( entity.getBlog().getId() == relationId ) {
          list.add(entity);
        }
      }
    } else {
      throw new UnsupportedOperationException();
    }
    return list;
  }
}
