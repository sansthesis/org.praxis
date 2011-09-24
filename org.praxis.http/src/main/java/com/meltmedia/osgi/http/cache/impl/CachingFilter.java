package com.meltmedia.osgi.http.cache.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

@Component
@Service
@Property(name = "pattern", value = ".*.(js|css|html|png|gif|jpg)")
public class CachingFilter implements Filter {

  public static final long TTL = 60 * 60 * 24 * 7;

  private static final DateFormat HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
    final HttpServletResponse response = (HttpServletResponse) resp;
    if( !response.containsHeader("Cache-Control") && !response.containsHeader("Expires") ) {
      response.setHeader("Cache-Control", String.format("max-age=%s, public", TTL));
      response.setHeader("Expires", HTTP_DATE_FORMAT.format(new Date(System.currentTimeMillis() + TTL * 1000)));
    }
    chain.doFilter(request, resp);
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }

}
