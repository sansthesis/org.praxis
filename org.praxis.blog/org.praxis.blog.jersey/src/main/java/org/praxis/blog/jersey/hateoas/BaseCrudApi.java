package org.praxis.blog.jersey.hateoas;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface BaseCrudApi<E extends ResourceRepresentation> {
  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  E get(@PathParam("id") long id);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<E> list();
}
