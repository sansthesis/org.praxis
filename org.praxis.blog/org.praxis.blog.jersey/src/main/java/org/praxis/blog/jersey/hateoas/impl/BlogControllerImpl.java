package org.praxis.blog.jersey.hateoas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.hateoas.BlogController;
import org.praxis.blog.jersey.hateoas.BlogResource;
import org.praxis.blog.jersey.hateoas.Link;

@Component(metatype = true, immediate = true)
@Service
@Path("/blogs")
public class BlogControllerImpl implements BlogController {

  @Reference
  private BlogDao blogDao;

  @Override
  public BlogResource get(final long id) {
    return wrap(blogDao.get(id));
  }

  @Override
  public List<BlogResource> list() {
    return wrap(blogDao.list());
  }

  private List<BlogResource> wrap(final List<Blog> list) {
    final List<BlogResource> output = new ArrayList<BlogResource>();
    for( final Blog entity : list ) {
      output.add(wrap(entity));
    }
    return output;
  }

  private BlogResource wrap(final Blog entity) {
    final BlogResource resource = new BlogResource(entity);
    final Link self = new Link("/hateoas/blogs/" + entity.getId(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }

}
