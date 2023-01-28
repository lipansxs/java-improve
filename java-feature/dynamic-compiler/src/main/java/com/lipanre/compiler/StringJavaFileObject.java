package com.lipanre.compiler;


import com.lipanre.compiler.common.Constants;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * 字符串形式的JavaFileObject
 */
public class StringJavaFileObject extends SimpleJavaFileObject {


    private String sourceCode;

    /**
     * 创建一个StringJavaFileObject对象
     * @param classFullPath 类路径，点分隔的类路径，比如：a.b.c.TestClass
     */
    public StringJavaFileObject(String classFullPath, String sourceCode) {
        super(URI.create(Constants.STRING_PROTOCOL + classFullPath.replace(Constants.PACKAGE_SEPARATOR, Constants.URI_SEPARATOR) + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    /**
     * 获取源代码
     * @param ignoreEncodingErrors ignore encoding errors if true
     * @return
     * @throws IOException
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.sourceCode;
    }
}
