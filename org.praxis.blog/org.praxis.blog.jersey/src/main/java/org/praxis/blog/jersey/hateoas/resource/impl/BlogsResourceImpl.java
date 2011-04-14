package org.praxis.blog.jersey.hateoas.resource.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.BlogResourceListRepresentation;
import org.praxis.blog.jersey.hateoas.resource.BlogResource;
import org.praxis.blog.jersey.hateoas.resource.BlogsResource;

@Component(metatype = true, immediate = true)
@Service
@Path("/blogs")
public class BlogsResourceImpl extends AbstractController implements BlogsResource {

  @Context
  UriInfo uriInfo;

  @Reference
  private BlogDao blogDao;

  @Reference
  private BlogResource blogResource;

  @Path("/{blogId: [0-9]+}")
  public BlogResource get() {
    return blogResource;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<BlogResourceListRepresentation> list() {
    return wrap(blogDao.list());
  }

  private BlogResourceListRepresentation wrap(final Blog entity) {
    final BlogResourceListRepresentation resource = new BlogResourceListRepresentation(entity);
    final Link self = new Link(uriInfo.getBaseUriBuilder().path(getClass()).path(getMethod(getClass(), "get")).build(entity.getId()).toString(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }

  private List<BlogResourceListRepresentation> wrap(final List<Blog> list) {
    final List<BlogResourceListRepresentation> output = new ArrayList<BlogResourceListRepresentation>();
    for( final Blog entity : list ) {
      output.add(wrap(entity));
    }
    return output;
  }
}
