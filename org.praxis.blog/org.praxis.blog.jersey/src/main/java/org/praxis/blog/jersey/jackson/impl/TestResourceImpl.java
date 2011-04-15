package org.praxis.blog.jersey.jackson.impl;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.jackson.EntityRepresentationView;
import org.praxis.blog.jersey.jackson.Views;
import org.praxis.blog.jersey.jackson.om.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(metatype = true, immediate = true)
@Service
@Path("/test")
public class TestResourceImpl implements TestResource {

  private final Logger log = LoggerFactory.getLogger(getClass());
  @Reference
  private BlogDao blogDao;

  @GET
  @Path("/detail")
  @Produces(MediaType.APPLICATION_JSON)
  public String getDetail() throws Exception {
    String output = null;
    final ObjectMapper mapper = new ObjectMapper();
    for( final Blog entity : blogDao.list() ) {
      final Foo foo = new Foo();
      BeanUtils.copyProperties(foo, entity);
      output = mapper.viewWriter(Views.Detail.class).writeValueAsString(foo);
      break;
    }
    return output;
  }

  @GET
  @Path("/list")
  @Produces(MediaType.APPLICATION_JSON)
  public String getList() throws Exception {
    String output = null;
    final ObjectMapper mapper = new ObjectMapper();
    final SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0, 0, "SNAPSHOT"));
    testModule.addSerializer(new MySerializer());
    mapper.registerModule(testModule);
    for( final Blog entity : blogDao.list() ) {
      final Foo foo = new Foo();
      BeanUtils.copyProperties(foo, entity);
      output = mapper.viewWriter(Views.List.class).writeValueAsString(new EntityRepresentationView<Foo>(foo));
      break;
    }
    return output;
  }

  public static class MySerializer extends JsonSerializer<EntityRepresentationView<Foo>> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void serializeWithType(final EntityRepresentationView<Foo> value, final JsonGenerator jgen, final SerializerProvider provider, final TypeSerializer typeSer) throws IOException, JsonProcessingException {
      log.info("Serializing with type {}", value);
      super.serializeWithType(value, jgen, provider, typeSer);
    }

    @Override
    public Class<EntityRepresentationView<Foo>> handledType() {
      return (Class<EntityRepresentationView<Foo>>) (Class<?>) EntityRepresentationView.class;
    }

    @Override
    public void serialize(final EntityRepresentationView<Foo> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
      log.info("Serializing {}.", value);
    }

  }
}
