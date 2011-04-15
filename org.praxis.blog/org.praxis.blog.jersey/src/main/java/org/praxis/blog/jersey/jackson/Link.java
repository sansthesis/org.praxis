package org.praxis.blog.jersey.jackson;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * A Link is a reference to a Resource. It follows the definition from the Atom specification (http://www.ietf.org/rfc/rfc4287.txt).
 * @author Jason Rose
 * 
 */
@XmlRootElement
public class Link {
  private String href;
  private String hreflang;
  private String length;
  private String rel;
  private String title;
  private String type;

  public Link() {
    super();
  }

  public Link(final String href, final String rel, final String type) {
    this();
    this.href = href;
    this.rel = rel;
    this.type = type;
  }

  public String getHref() {
    return href;
  }

  /**
   * The "hreflang" attribute's content describes the language of the resource pointed to by the href attribute. When used together with the rel="alternate", it implies a translated version of the entry. Link elements MAY have an hreflang attribute, whose value MUST be a language tag [RFC3066].
   * @return The hreflang attribute.
   */
  public String getHreflang() {
    return hreflang;
  }

  /**
   * The "length" attribute indicates an advisory length of the linked content in octets; it is a hint about the content length of the representation returned when the IRI in the href attribute is mapped to a URI and dereferenced. Note that the length attribute does not override the actual content length of the representation as reported by the underlying protocol. Link elements MAY have a length attribute.
   * @return The length attribute.
   */
  public String getLength() {
    return length;
  }

  /**
   * The value "related" signifies that the IRI in the value of the href attribute identifies a resource related to the resource described by the containing element. For example, the feed for a site that discusses the performance of the search engine at "http://search.example.com" might contain, as a child of atom:feed:
   * 
   * &lt;link rel="related" href="http://search.example.com/"/&gt;
   * 
   * An identical link might appear as a child of any atom:entry whose content contains a discussion of that same search engine.
   * @return The related attribute.
   */
  public String getRel() {
    return rel;
  }

  /**
   * The "title" attribute conveys human-readable information about the link. The content of the "title" attribute is Language-Sensitive. Entities such as "&amp;amp;" and "&amp;lt;" represent their corresponding characters ("&amp;" and "&lt;", respectively), not markup. Link elements MAY have a title attribute.
   * @return The title attribute.
   */
  public String getTitle() {
    return title;
  }

  /**
   * On the link element, the "type" attribute's value is an advisory media type: it is a hint about the type of the representation that is expected to be returned when the value of the href attribute is dereferenced. Note that the type attribute does not override the actual media type returned with the representation. Link elements MAY have a type attribute, whose value MUST conform to the syntax of a MIME media type [MIMEREG].
   * @return The type attribute.
   */
  public String getType() {
    return type;
  }

  public void setHref(final String href) {
    this.href = href;
  }

  public void setHreflang(final String hreflang) {
    this.hreflang = hreflang;
  }

  public void setLength(final String length) {
    this.length = length;
  }

  public void setRel(final String rel) {
    this.rel = rel;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
