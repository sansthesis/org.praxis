package com.meltmedia.osgi.http.js.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.meltmedia.osgi.http.js.JsStorage;

@Component
@Service
@Property(name = "alias", value = "/optimize.js")
public class JsOptimizerServletImpl extends HttpServlet {
  private static final long serialVersionUID = -5890683267235555576L;

  public static final String PARAMETER_JS = "js";
  public static final String PARAMETER_COMPRESS = "compress";

  @Reference
  private JsStorage cache;

  public JsOptimizerServletImpl() {

  }

  public JsOptimizerServletImpl(final JsStorage cache) {
    this.cache = cache;
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    final String output;
    final int statusCode;
    if( isMissingRequestParameters(request) ) {
      output = null;
      statusCode = HttpServletResponse.SC_BAD_REQUEST;
    } else if( shouldNotMinifyJs(request) ) {
      final StringBuilder buffer = new StringBuilder();
      for( final String js : request.getParameterValues(PARAMETER_JS) ) {
        buffer.append(cache.getRawJs(js));
        buffer.append("\n");
      }
      statusCode = HttpServletResponse.SC_OK;
      output = buffer.toString();
    } else {
      final StringBuilder buffer = new StringBuilder();
      for( final String js : request.getParameterValues(PARAMETER_JS) ) {
        buffer.append(cache.getMinifiedJs(js));
        buffer.append("\n");
      }
      statusCode = HttpServletResponse.SC_OK;
      output = buffer.toString();
    }
    if( output != null ) {
      response.setContentType("text/javascript");
      response.getWriter().print(output);
    }
    if( statusCode >= 100 && statusCode < 400 ) {
      response.setStatus(statusCode);
    } else {
      response.sendError(statusCode);
    }
  }

  private boolean isMissingRequestParameters(final HttpServletRequest request) {
    return request.getParameter(PARAMETER_JS) == null;
  }

  private boolean shouldNotMinifyJs(final HttpServletRequest request) {
    return "false".equals(request.getParameter(PARAMETER_COMPRESS));
  }

}
