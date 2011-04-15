package org.praxis.blog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Comment;
import org.praxis.blog.dao.AbstractDao;
import org.praxis.blog.dao.CommentDao;

@Component(metatype = true, immediate = true)
@Service
public class CommentDaoImpl extends AbstractDao<Comment> implements CommentDao {

  public CommentDaoImpl() {
    super();
    entityClass = Comment.class;
  }

  @Override
  public List<Comment> listByRelation(final String relationName, final long relationId) {
    final List<Comment> all = list();
    final List<Comment> list = new ArrayList<Comment>();
    if( "story".equals(relationName) ) {
      for( final Comment entity : all ) {
        if( entity.getStory().getId() == relationId ) {
          list.add(entity);
        }
      }
    } else {
      throw new UnsupportedOperationException();
    }
    return list;
  }
}
