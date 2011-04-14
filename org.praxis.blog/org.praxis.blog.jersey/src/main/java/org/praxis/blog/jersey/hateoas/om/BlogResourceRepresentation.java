package org.praxis.blog.jersey.hateoas.om;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.BeanUtils;
import org.praxis.blog.Blog;
import org.praxis.blog.Story;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.ResourceRepresentation;

@XmlRootElement(name = "blog")
public class BlogResourceRepresentation extends Blog implements ResourceRepresentation {
  private List<Link> links = new ArrayList<Link>();

  public BlogResourceRepresentation() {
    super();
  }

  public BlogResourceRepresentation(final Blog entity) {
    this();
    try {
      BeanUtils.copyProperties(this, entity);
    } catch( final Exception e ) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public List<Link> getLinks() {
    return links;
  }

  @Override
  @XmlTransient
  public Set<Story> getStories() {
    return super.getStories();
  }

  @Override
  public void setLinks(final List<Link> links) {
    this.links = links;
  }

}
