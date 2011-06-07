package org.praxis.blog.jersey.jackson.impl;

import java.io.IOException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.praxis.blog.jersey.jackson.JacksonProvider;
import org.praxis.blog.jersey.jackson.om.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, metatype = true)
@Service
public class JacksonProviderImpl implements JacksonProvider {

  public static class MySerializer extends JsonSerializer<Foo> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Class<Foo> handledType() {
      return Foo.class;
    }

    @Override
    public void serialize(final Foo value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
      log.info("Serializing {}.", value);
      jgen.writeRawValue("{" + "\"toString\":\"" + value.toString() + "\"}");
    }

    @Override
    public void serializeWithType(final Foo value, final JsonGenerator jgen, final SerializerProvider provider, final TypeSerializer typeSer) throws IOException, JsonProcessingException {
      log.info("Serializing with type {}", value);
      super.serializeWithType(value, jgen, provider, typeSer);
    }

  }

  @Override
  public JacksonJsonProvider getJacksonJsonProvider() {
    final ObjectMapper mapper = new ObjectMapper();
    final SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
    testModule.addSerializer(new MySerializer());
    mapper.registerModule(testModule);
    final JacksonJsonProvider provider = new JacksonJsonProvider(mapper);
    return provider;
  }

}
