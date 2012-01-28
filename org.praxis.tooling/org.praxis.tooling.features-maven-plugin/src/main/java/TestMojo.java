import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Says "Hi" to the user.
 * @goal sayhi
 */
public class TestMojo extends AbstractMojo {
  @Override
  public void execute() throws MojoExecutionException {
    getLog().info("Hello, world.");
  }
}
