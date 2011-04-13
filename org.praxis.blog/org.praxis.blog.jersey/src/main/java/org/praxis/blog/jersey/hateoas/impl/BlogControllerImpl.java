package org.praxis.blog.jersey.hateoas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.BlogController;
import org.praxis.blog.jersey.hateoas.BlogResource;
import org.praxis.blog.jersey.hateoas.Link;

@Component(metatype = true, immediate = true)
@Service
@Path("/blogs")
public class BlogControllerImpl extends AbstractController implements BlogController {

  @Context
  UriInfo uriInfo;

  @Reference
  private BlogDao blogDao;

  @Override
  public BlogResource get(final long id) {
    return wrap(blogDao.get(id));
  }

  @Override
  public List<BlogResource> list() {
    log.info("UriInfo: {}", uriInfo);
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
    final UriBuilder baseBuilder = uriInfo.getBaseUriBuilder();
    final BlogResource resource = new BlogResource(entity);
    final Link self = new Link(baseBuilder.path(getClass()).path("" + entity.getId()).build().toString(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }

}
