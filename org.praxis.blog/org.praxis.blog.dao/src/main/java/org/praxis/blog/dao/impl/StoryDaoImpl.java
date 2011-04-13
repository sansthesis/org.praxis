package org.praxis.blog.dao.impl;

import java.util.Date;
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
  public List<Story> list() {
    final List<Story> list = super.list();
    for( final Story entity : list ) {
      entity.setDate(new Date());
      entity.setStory("This is the story.");
      entity.setTitle("Story Title " + entity.getId());
    }
    return list;
  }
}
