package com.lipanre.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 自定义文件管理器，用于获取JavaFileObject、获取类加载器
 */
public class CustomFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {


    private DynamicCompilerClassLoader dynamicCompilerClassLoader;

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    protected CustomFileManager(StandardJavaFileManager fileManager, List<URL> urls, ClassLoader parent) {
        super(fileManager);
        dynamicCompilerClassLoader = new DynamicCompilerClassLoader(urls, parent);
    }

    public ClassLoader getCustomerClassLoader() {
        return dynamicCompilerClassLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        ClassJavaFileObject classJavaFileObject = new ClassJavaFileObject(className);

        // 将这个classJavaFileObject缓存起来
        dynamicCompilerClassLoader.addClassFileObject(className, classJavaFileObject);
        return classJavaFileObject;
    }
}
