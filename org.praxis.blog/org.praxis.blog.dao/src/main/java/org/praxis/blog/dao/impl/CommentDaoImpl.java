package org.praxis.blog.dao.impl;

import java.util.Date;
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
  public List<Comment> list() {
    final List<Comment> list = super.list();
    for( final Comment entity : list ) {
      entity.setAuthor("Author " + entity.getId());
      entity.setBody("This is the comment body.");
      entity.setDate(new Date());
      entity.setSubject("Comment " + entity.getId());
    }
    return list;
  }
}
