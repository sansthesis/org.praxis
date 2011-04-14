package org.praxis.blog.jersey.hateoas.impl;

import java.util.Collections;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.BlogResource;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.BlogResourceRepresentation;

public class BlogResourceImpl extends AbstractController implements BlogResource {

  private long blogId;
  private UriInfo uriInfo;
  private BlogDao blogDao;

  public BlogResourceImpl() {
    super();
  }

  public BlogResourceImpl(final UriInfo uriInfo, final BlogDao blogDao, final long blogId) {
    this.uriInfo = uriInfo;
    this.blogId = blogId;
    this.blogDao = blogDao;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public BlogResourceRepresentation get() {
    return wrap(blogDao.get(blogId));
  }

  private BlogResourceRepresentation wrap(final Blog entity) {
    final BlogResourceRepresentation resource = new BlogResourceRepresentation(entity);
    final Link self = new Link(uriInfo.getRequestUriBuilder().build().toString(), "self", "application/json");
    final Link root = new Link(uriInfo.getBaseUriBuilder().path(BlogsResourceImpl.class).build().toString(), "root", "application/json");
    Collections.addAll(resource.getLinks(), self, root);
    return resource;
  }
}
