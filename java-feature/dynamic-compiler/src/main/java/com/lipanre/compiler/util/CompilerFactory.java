package com.lipanre.compiler.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompilerFactory {

    /**
     * 创建一个新的java编译器对象
     * @return
     */
    public static JavaCompiler createCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

}
