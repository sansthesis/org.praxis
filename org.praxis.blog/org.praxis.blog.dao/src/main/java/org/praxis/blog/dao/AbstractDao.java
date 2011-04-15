package org.praxis.blog.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.praxis.blog.Blog;
import org.praxis.blog.Comment;
import org.praxis.blog.Entity;
import org.praxis.blog.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(componentAbstract = true)
public abstract class AbstractDao<E extends Entity> implements Dao<E> {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  private static final Set<Entity> entities = new HashSet<Entity>();
  static {
    long idSeq = 0;
    for( int i = 0; i < 10; i++ ) {
      final Blog blog = new Blog();
      blog.setId(idSeq++);
      blog.setTitle("Blog title " + blog.getId());
      blog.setDescription("Blog description.");
      entities.add(blog);
      for( int j = 0; j < 10; j++ ) {
        final Story story = new Story();
        story.setId(idSeq++);
        story.setBlog(blog);
        story.setCreatedBy("Person " + story.getId());
        story.setDate(new Date());
        story.setTitle("Story Title " + story.getId());
        story.setStory("This is the story...");
        blog.getStories().add(story);
        entities.add(story);
        for( int k = 0; k < 10; k++ ) {
          final Comment comment = new Comment();
          comment.setId(idSeq++);
          comment.setAuthor("Comment Author " + comment.getId());
          comment.setBody("This is the comment body.");
          comment.setDate(new Date());
          comment.setStory(story);
          story.getComments().add(comment);
          story.setTitle("Comment Title " + comment.getId());
          entities.add(comment);
        }
      }
    }
  }

  protected Class<E> entityClass;

  @Override
  public E create(final E entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean delete(final E entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E get(final long id) {
    E entity = null;
    for( final E e : list() ) {
      if( id == e.getId() ) {
        entity = e;
        break;
      }
    }
    return entity;
  }

  @Override
  public List<E> list() {
    final List<E> list = new ArrayList<E>();
    for( final Entity entity : entities ) {
      if( entityClass.isAssignableFrom(entity.getClass()) ) {
        list.add((E) entity);
      }
    }
    return list;
  }

  @Override
  public List<E> listByRelation(final String relationName, final long relationId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E update(final E entity) {
    throw new UnsupportedOperationException();
  }

}
