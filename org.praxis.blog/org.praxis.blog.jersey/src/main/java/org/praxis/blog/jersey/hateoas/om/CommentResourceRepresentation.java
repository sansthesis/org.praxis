package org.praxis.blog.jersey.hateoas.om;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.BeanUtils;
import org.praxis.blog.Comment;
import org.praxis.blog.Story;
import org.praxis.blog.jersey.hateoas.Link;

@XmlRootElement(name = "comment")
public class CommentResourceRepresentation extends Comment implements ResourceRepresentation {
  private List<Link> links = new ArrayList<Link>();

  public CommentResourceRepresentation() {
    super();
  }

  public CommentResourceRepresentation(final Comment entity) {
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
  public Story getStory() {
    return super.getStory();
  }

  @Override
  public void setLinks(final List<Link> links) {
    this.links = links;
  }
}
