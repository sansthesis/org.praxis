// ========================================================================
// Copyright (c) 2007-2009 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================
package com.meltmedia.osgi.http.cache.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* ------------------------------------------------------------ */
/**
 * GZIP Filter This filter will gzip the content of a response iff:
 * <ul>
 * <li>The filter is mapped to a matching path</li>
 * <li>The response status code is >=200 and <300
 * <li>The content length is unknown or more than the <code>minGzipSize</code> initParameter or the minGzipSize is 0(default)</li>
 * <li>The content-type is in the comma separated list of mimeTypes set in the <code>mimeTypes</code> initParameter or if no mimeTypes are defined the content-type is not "application/gzip"</li>
 * <li>No content-encoding is specified by the resource</li>
 * </ul>
 * 
 * <p>
 * Compressing the content can greatly improve the network bandwidth usage, but at a cost of memory and CPU cycles. If this filter is mapped for static content, then use of efficient direct NIO may be prevented, thus use of the gzip mechanism of the {@link org.eclipse.jetty.servlet.DefaultServlet} is advised instead.
 * </p>
 * <p>
 * This filter extends {@link UserAgentFilter} and if the the initParameter <code>excludedAgents</code> is set to a comma separated list of user agents, then these agents will be excluded from gzip content.
 * </p>
 * 
 */
@Component
@Service
@Properties({ @Property(name = "pattern", value = ".*"), @Property(name = "service.ranking", intValue = 3000) })
public class JettyGzipFilter extends JettyUserAgentFilter {
  private final Logger log = LoggerFactory.getLogger(getClass());
  protected Set<String> _mimeTypes;
  protected int _bufferSize = 8192;
  protected int _minGzipSize = 256;
  protected Set<String> _excluded;

  /* ------------------------------------------------------------ */
  /**
   * @see org.eclipse.jetty.servlets.UserAgentFilter#destroy()
   */
  @Override
  public void destroy() {
  }

  /* ------------------------------------------------------------ */
  /**
   * @see org.eclipse.jetty.servlets.UserAgentFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  @Override
  public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;

    final String ae = request.getHeader("accept-encoding");
    if( ae != null && ae.indexOf("gzip") >= 0 && !response.containsHeader("Content-Encoding") && !"HEAD".equalsIgnoreCase(request.getMethod()) ) {
      if( _excluded != null ) {
        final String ua = getUserAgent(request);
        if( _excluded.contains(ua) ) {
          super.doFilter(request, response, chain);
          return;
        }
      }

      final JettyGzipResponseWrapper wrappedResponse = newGzipResponseWrapper(request, response);

      boolean exception = false;
      try {
        super.doFilter(request, wrappedResponse, chain);
        exception = false;
      } catch( final Exception e ) {
        exception = true;
      } finally {
        if( exception && !response.isCommitted() ) {
          wrappedResponse.resetBuffer();
          wrappedResponse.noGzip();
        } else {
          wrappedResponse.finish();
        }
      }
    } else {
      super.doFilter(request, response, chain);
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see org.eclipse.jetty.servlets.UserAgentFilter#init(javax.servlet.FilterConfig)
   */
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    super.init(filterConfig);

    String tmp = filterConfig.getInitParameter("bufferSize");
    if( tmp != null ) {
      _bufferSize = Integer.parseInt(tmp);
    }

    tmp = filterConfig.getInitParameter("minGzipSize");
    if( tmp != null ) {
      _minGzipSize = Integer.parseInt(tmp);
    }

    tmp = filterConfig.getInitParameter("mimeTypes");
    if( tmp != null ) {
      _mimeTypes = new HashSet<String>();
      final StringTokenizer tok = new StringTokenizer(tmp, ",", false);
      while( tok.hasMoreTokens() ) {
        _mimeTypes.add(tok.nextToken());
      }
    }

    tmp = filterConfig.getInitParameter("excludedAgents");
    if( tmp != null ) {
      _excluded = new HashSet<String>();
      final StringTokenizer tok = new StringTokenizer(tmp, ",", false);
      while( tok.hasMoreTokens() ) {
        _excluded.add(tok.nextToken());
      }
    }
  }

  /**
   * Allows derived implementations to replace ResponseWrapper implementation.
   * 
   * @param request the request
   * @param response the response
   * @return the gzip response wrapper
   */
  protected JettyGzipResponseWrapper newGzipResponseWrapper(final HttpServletRequest request, final HttpServletResponse response) {
    return new JettyGzipResponseWrapper(request, response) {
      {
        _mimeTypes = JettyGzipFilter.this._mimeTypes;
        _bufferSize = JettyGzipFilter.this._bufferSize;
        _minGzipSize = JettyGzipFilter.this._minGzipSize;
      }

      @Override
      protected PrintWriter newWriter(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
        return JettyGzipFilter.this.newWriter(out, encoding);
      }
    };
  }

  /**
   * Allows derived implementations to replace PrintWriter implementation.
   * 
   * @param out the out
   * @param encoding the encoding
   * @return the prints the writer
   * @throws UnsupportedEncodingException
   */
  protected PrintWriter newWriter(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
    return encoding == null ? new PrintWriter(out) : new PrintWriter(new OutputStreamWriter(out, encoding));
  }
}
