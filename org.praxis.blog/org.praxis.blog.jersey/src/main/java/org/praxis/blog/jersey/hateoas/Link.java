package org.praxis.blog.jersey.hateoas;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@XmlRootElement
public class Link {
  private String href;
  private String rel;
  private String type;
  private String charset;

  public Link() {
    super();
  }

  public Link(final String href, final String rel, final String type) {
    this();
    this.href = href;
    this.rel = rel;
    this.type = type;
  }

  public Link(final String href, final String rel, final String type, final String charset) {
    this(href, rel, type);
    this.charset = charset;
  }

  public String getCharset() {
    return charset;
  }

  public String getHref() {
    return href;
  }

  public String getRel() {
    return rel;
  }

  public String getType() {
    return type;
  }

  public void setCharset(final String charset) {
    this.charset = charset;
  }

  public void setHref(final String href) {
    this.href = href;
  }

  public void setRel(final String rel) {
    this.rel = rel;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
