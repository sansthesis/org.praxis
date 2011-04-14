package org.praxis.blog.jersey.hateoas;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Link {
  private String uri;
  private String rel;
  private String type;

  public Link() {
    super();
  }

  public Link(final String uri, final String rel, final String type) {
    this();
    this.uri = uri;
    this.rel = rel;
    this.type = type;
  }

  public String getRel() {
    return rel;
  }

  public String getType() {
    return type;
  }

  public String getUri() {
    return uri;
  }

  public void setRel(final String rel) {
    this.rel = rel;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }
}
