package org.praxis.tooling.features;

import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sampullara.mustache.Mustache;
import com.sampullara.mustache.MustacheBuilder;

/**
 * Generates a single feature with all relevant dependencies inside of it.
 * @goal generate-features-xml
 */
public class GenerateFeaturesMojo extends AbstractMojo {
  /**
   * The Maven project to analyze.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  @SuppressWarnings("unchecked")
  @Override
  public void execute() throws MojoExecutionException {
    try {
      final String template = IOUtil.toString(getClass().getClassLoader().getResourceAsStream("features.mustache.xml"), "UTF-8");
      final Mustache mustache = new MustacheBuilder().parse(template, "features.mustache.xml");
      final StringWriter out = new StringWriter();

      // Build context.
      final Map<String, Object> context = convert(project.getArtifact());

      final List<Map<String, Object>> dependencies = Lists.newArrayList();
      for( final Artifact dependency : Iterables.filter((Collection<Artifact>) project.getDependencyArtifacts(), new ArtifactsWeWant()) ) {
        dependencies.add(convert(dependency));
      }
      context.put("dependencies", dependencies);

      // Render template.
      mustache.execute(out, context);

      getLog().info(out.toString());
    } catch( final Exception e ) {
      throw new MojoExecutionException("Unable to generate features.xml", e);
    }
  }

  private class ArtifactsWeWant implements Predicate<Artifact> {
    private final Set<String> valid = Sets.newHashSet("compile", "runtime");

    @Override
    public boolean apply(final Artifact input) {
      return valid.contains(input.getScope());
    }
  }

  private Map<String, Object> convert(final Artifact artifact) {
    final Map<String, Object> map = Maps.newHashMap();
    map.put("groupId", artifact.getGroupId());
    map.put("artifactId", artifact.getArtifactId());
    map.put("version", artifact.getVersion());
    map.put("hasClassifier", artifact.getClassifier() != null);
    map.put("classifier", artifact.getClassifier());
    map.put("hasType", artifact.getType() != null);
    map.put("type", artifact.getType());
    return map;
  }
}
