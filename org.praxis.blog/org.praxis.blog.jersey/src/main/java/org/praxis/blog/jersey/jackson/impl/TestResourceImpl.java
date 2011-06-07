package org.praxis.blog.jersey.jackson.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.jackson.EntityRepresentationBuilder;
import org.praxis.blog.jersey.jackson.Link;
import org.praxis.blog.jersey.jackson.ResourceRepresentation;
import org.praxis.blog.jersey.jackson.om.Foo;
import org.praxis.blog.jersey.jackson.om.FooImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(metatype = true, immediate = true)
@Service
@Path("/test")
public class TestResourceImpl implements TestResource {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Reference
  private BlogDao blogDao;

  @Reference
  private EntityRepresentationBuilder builder;

  @GET
  @Path("/detail")
  @Produces(MediaType.APPLICATION_JSON)
  public Foo getDetail() throws Exception {
    for( final Blog entity : blogDao.list() ) {
      final Foo view = builder.buildEntityRepresentation(new FooImpl());
      ((ResourceRepresentation) view).setLinks(Arrays.asList(new Link("http://www.google.com", "google", "text/html")));
      BeanUtils.copyProperties(view, entity);
      return view;
    }
    return null;
  }

  @GET
  @Path("/list")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Foo> getList() throws Exception {
    final List<Foo> list = new ArrayList<Foo>();
    for( final Blog entity : blogDao.list() ) {
      final Foo foo = new FooImpl();
      BeanUtils.copyProperties(foo, entity);
      list.add(foo);
    }
    return list;
  }
}
