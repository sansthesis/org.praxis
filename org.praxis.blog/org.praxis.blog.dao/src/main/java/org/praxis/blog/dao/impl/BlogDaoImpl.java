package org.praxis.blog.dao.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.AbstractDao;
import org.praxis.blog.dao.BlogDao;

@Component(metatype = true, immediate = true)
@Service
public class BlogDaoImpl extends AbstractDao<Blog> implements BlogDao {

  public BlogDaoImpl() {
    super();
    entityClass = Blog.class;
  }
}
