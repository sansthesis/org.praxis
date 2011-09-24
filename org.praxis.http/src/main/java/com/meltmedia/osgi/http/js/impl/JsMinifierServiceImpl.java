package com.meltmedia.osgi.http.js.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.meltmedia.osgi.http.js.JsMinifierService;

@Component
@Service
public class JsMinifierServiceImpl implements JsMinifierService {

  @Override
  public String minify(final String inputString) {
    final Compiler compiler = new Compiler();

    final CompilerOptions options = new CompilerOptions();
    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

    // To get the complete set of externs, the logic in
    // CompilerRunner.getDefaultExterns() should be used here.
    final JSSourceFile extern = JSSourceFile.fromCode("externs.js", "function alert(x) {}");

    // The dummy input name "input.js" is used here so that any warnings or
    // errors will cite line numbers in terms of input.js.
    final JSSourceFile input = JSSourceFile.fromCode("input.js", inputString);

    // compile() returns a Result, but it is not needed here.
    compiler.compile(extern, input, options);

    // The compiler is responsible for generating the compiled code; it is not
    // accessible via the Result.
    return compiler.toSource();
  }
}
