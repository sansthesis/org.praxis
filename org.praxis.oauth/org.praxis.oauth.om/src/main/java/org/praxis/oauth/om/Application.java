package org.praxis.oauth.om;

import java.net.URL;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Application {
  private URL url;
  private String title;

  public String getTitle() {
    return title;
  }

  public URL getUrl() {
    return url;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public void setUrl(final URL url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
