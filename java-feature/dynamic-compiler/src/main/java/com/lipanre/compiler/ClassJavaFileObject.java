package com.lipanre.compiler;


import com.lipanre.compiler.common.Constants;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class ClassJavaFileObject extends SimpleJavaFileObject {


    private final ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     *
     * @param uri  the URI for this file object
     * @param kind the kind of this file object
     */
    protected ClassJavaFileObject(String classFullPath) {
        super(URI.create(Constants.STRING_PROTOCOL + classFullPath.replace(Constants.PACKAGE_SEPARATOR, Constants.URI_SEPARATOR) + Kind.CLASS.extension), Kind.CLASS);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return byteArrayOutputStream;
    }

    /**
     * 获取class的二进制内容
     * @return
     */
    public byte[] getClassContent() {
        return this.byteArrayOutputStream.toByteArray();
    }
}
