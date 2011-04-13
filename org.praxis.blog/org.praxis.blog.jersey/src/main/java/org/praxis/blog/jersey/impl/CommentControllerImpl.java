package org.praxis.blog.jersey.impl;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Comment;
import org.praxis.blog.dao.CommentDao;
import org.praxis.blog.jersey.CommentController;

@Component(metatype = true, immediate = true)
@Service
@Path("/comments")
public class CommentControllerImpl implements CommentController {

  @Reference
  private CommentDao commentDao;

  @Override
  public Comment get(final long id) {
    return commentDao.get(id);
  }

  @Override
  public List<Comment> list() {
    return commentDao.list();
  }

}
