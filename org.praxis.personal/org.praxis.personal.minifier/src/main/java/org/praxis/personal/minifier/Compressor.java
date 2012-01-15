package org.praxis.personal.minifier;

import java.net.URI;
import java.util.List;

public interface Compressor {

  List<URI> generateCompressedFiles(String baseUri, String... inputs);
}
