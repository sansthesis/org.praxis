package org.praxis.personal.minifier.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.praxis.personal.minifier.CacheService;
import org.praxis.personal.minifier.Compressor;

@Component
public class AbstractCompressorImpl implements Compressor {

  @Reference
  private final CacheService cache;

  private final boolean combine;

  private final boolean compress;

  public AbstractCompressorImpl() {
    this(false, false);
  }

  protected AbstractCompressorImpl(final boolean combine, final boolean compress) {
    this(null, combine, compress);
  }

  protected AbstractCompressorImpl(final CacheService cache) {
    this(cache, false, false);
  }

  protected AbstractCompressorImpl(final CacheService cache, final boolean combine, final boolean compress) {
    this.cache = cache;
    this.combine = combine;
    this.compress = compress;
  }

  @Override
  public List<URI> generateCompressedFiles(final String base, final String... inputs) {
    try {
      return generateCompressedFiles(combine, compress, base, inputs);
    } catch( final URISyntaxException use ) {
      throw new RuntimeException(use);
    }
  }

  protected String generateAggregation(final boolean compressFiles, final String... uris) {
    try {
      final StringBuilder buffer = new StringBuilder();
      for( final String uri : uris ) {
        buffer.append("/*!" + uri + "*/\n");
        if( !cache.containsKey(uri) ) {
          String contents = IOUtils.toString(new URI(uri));
          if( compressFiles ) {
            contents = compress(uri, contents);
          }
          cache.put(uri, contents);
        }
        buffer.append(cache.get(uri));
        buffer.append("\n");
      }
      return buffer.toString();
    } catch( final URISyntaxException use ) {
      throw new RuntimeException(use);
    } catch( final IOException ioe ) {
      throw new RuntimeException(ioe);
    }
  }

  protected String compress(final String uri, final String contents) {
    return contents;
  }

  protected String generateKey(final String rawKey) {
    try {
      return DigestUtils.md5Hex(rawKey.getBytes("UTF-8"));
    } catch( final IOException e ) {
      throw new RuntimeException(e);
    }
  }

  private List<URI> generateCompressedFiles(final boolean combineFiles, final boolean compressFiles, final String base, final String... inputs) throws URISyntaxException {
    final List<URI> list;
    if( combineFiles ) {
      // Calculate key for aggregation.
      final StringBuilder keyBuffer = new StringBuilder();
      for( final String input : inputs ) {
        keyBuffer.append(generateURI(base, input)).append("|");
      }
      final String rawKey = keyBuffer.toString();
      final String key = generateKey(keyBuffer.toString());

      // Check cache for key.
      if( !cache.containsKey(key) ) {
        // If cache miss, generate new aggregation and store in cache.
        final String aggregation = generateAggregation(compressFiles, rawKey.split("[|]"));
        cache.put(key, aggregation);
      }

      // Return URI that will resolve to cache value.
      list = new ArrayList<URI>(1);
      list.add(new URI(key));
    } else {
      list = new ArrayList<URI>(inputs.length);
      for( final String input : inputs ) {
        final URI uri = generateURI(base, input);
        list.add(uri);
      }
    }
    return list;
  }

  private URI generateURI(final String base, final String path) throws URISyntaxException {
    final URI baseUri = new URI(base);
    final URI inputUri = new URI(path);
    final URI outputUri;
    if( inputUri.isAbsolute() ) {
      outputUri = inputUri;
    } else {
      outputUri = baseUri.resolve(inputUri);
    }
    return outputUri;
  }

}
