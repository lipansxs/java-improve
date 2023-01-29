package com.lipanre.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DynamicCompilerClassLoader extends URLClassLoader {

    /**
     * 目标类类加载器
     */
    private ClassLoader targetClassClassLoader;

    /**
     * 目标类class
     */
    private Class<?> targetClass;

    private final Map<String, ClassJavaFileObject> classFileObjectCache = new HashMap<>();

    private final Map<String, Class<?>> classCache = new HashMap<>();

    /**
     * 空数组大小
     */
    private static final int EMPTY_ARRAY_SIZE = 0;

    /**
     * 开始offset
     */
    private static final int START_OFFSET = 0;

    /**
     * 创建一个自定义类加载器对象
     *
     * @param urls
     * @param parent
     */
    public DynamicCompilerClassLoader(List<URL> urls, ClassLoader parent) {
        super(Objects.isNull(urls) ? new URL[EMPTY_ARRAY_SIZE] : urls.toArray(new URL[EMPTY_ARRAY_SIZE]), parent);
    }

    public void addClassFileObject(String className, ClassJavaFileObject classJavaFileObject) {
        classFileObjectCache.put(className, classJavaFileObject);
    }

    public void setTargetClassClassLoader(ClassLoader targetClassClassLoader) {
        this.targetClassClassLoader = targetClassClassLoader;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {

        if (Objects.nonNull(classCache.get(className))) {
            return classCache.get(className);
        }

        ClassJavaFileObject classJavaFileObject = classFileObjectCache.get(className);
        if (Objects.nonNull(classJavaFileObject)) {
            byte[] classContent = classJavaFileObject.getClassContent();
            Class<?> resultClass = defineClass(className, classContent, START_OFFSET, classContent.length);
            classCache.put(className, resultClass);
            return resultClass;
        }

        if (Objects.nonNull(targetClass) && Objects.equals(className, targetClass.getName())) {
            return targetClassClassLoader.loadClass(className);
        }

        return super.findClass(className);
    }
}
