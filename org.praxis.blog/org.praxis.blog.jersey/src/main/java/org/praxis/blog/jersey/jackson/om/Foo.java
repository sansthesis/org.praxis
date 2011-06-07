package org.praxis.blog.jersey.jackson.om;

import java.util.Set;

import org.praxis.blog.Entity;
import org.praxis.blog.Story;

public interface Foo extends Entity {
  Set<Story> getStories();

  String getTitle();

  void setStories(final Set<Story> stories);

  void setTitle(final String title);

}
