package org.praxis.blog.jersey.basic.impl;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.basic.BlogController;

@Component(metatype = true, immediate = true)
@Service
@Path("/blogs")
public class BlogControllerImpl implements BlogController {

  @Reference
  private BlogDao blogDao;

  @Override
  public Blog get(final long id) {
    return blogDao.get(id);
  }

  @Override
  public List<Blog> list() {
    return blogDao.list();
  }

}
