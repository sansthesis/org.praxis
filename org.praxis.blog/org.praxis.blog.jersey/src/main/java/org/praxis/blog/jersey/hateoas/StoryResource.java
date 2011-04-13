package org.praxis.blog.jersey.hateoas;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.praxis.blog.Story;

@XmlRootElement
public class StoryResource implements EntityResource<Story> {

  @Override
  public List<Link> getLinks() {
    return null;
  }

  @Override
  public void setLinks(final List<Link> links) {
  }

  @Override
  public EntityResource<Story> wrap(final Story entity) {
    return null;
  }

  @Override
  public Story unwrap() {
    return null;
  }

}
