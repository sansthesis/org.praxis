package org.praxis.blog.jersey.hateoas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import org.praxis.blog.jersey.hateoas.BlogsResource;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.BlogResourceListRepresentation;

@Component(metatype = true, immediate = true)
@Service
@Path("/blogs")
public class BlogsResourceImpl extends AbstractController implements BlogsResource {

  @Context
  UriInfo uriInfo;

  @Reference
  private BlogDao blogDao;

  @Path("/{id}")
  public BlogResourceImpl get(@PathParam("id") final long id) {
    return new BlogResourceImpl(uriInfo, blogDao, id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<BlogResourceListRepresentation> list() {
    return wrap(blogDao.list());
  }

  private BlogResourceListRepresentation wrap(final Blog entity) {
    final BlogResourceListRepresentation resource = new BlogResourceListRepresentation(entity);
    final Link self = new Link(uriInfo.getBaseUriBuilder().path(getClass()).path(getMethod(getClass(), "get", long.class)).build(entity.getId()).toString(), "self", "application/json");
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
