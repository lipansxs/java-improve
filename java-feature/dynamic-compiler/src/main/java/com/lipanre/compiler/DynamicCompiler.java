package com.lipanre.compiler;


import cn.hutool.core.util.ZipUtil;
import com.lipanre.compiler.common.Constants;
import com.lipanre.compiler.util.CompilerFactory;

import javax.tools.*;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 动态编译器，用于动态编译源代码
 */
public class DynamicCompiler {

    /**
     * extensionClassLoader，防止类冲突
     */
    private final static ClassLoader EXTENSION_CLASS_LOADER = ClassLoader.getSystemClassLoader().getParent();

    private static File BASE_JAR_FILE = new File("./lib/yhlz-base.jar");

    @SuppressWarnings("unchecked")
    public static <T> Class<T> compileAndLoadClass(String classFullPath,
                                                   String sourceCode,
                                                   List<URL> dependencies,
                                                   Class<T> targetClass) throws ClassNotFoundException {
        DynamicCompilerClassLoader dynamicCompilerClassLoader = compile(classFullPath, sourceCode, dependencies, targetClass);
        if (Objects.nonNull(targetClass)) {
            dynamicCompilerClassLoader.setTargetClass(targetClass);
            dynamicCompilerClassLoader.setTargetClassClassLoader(targetClass.getClassLoader());
        }
        return (Class<T>) dynamicCompilerClassLoader.loadClass(classFullPath);
    }

    /**
     * 编译源码，线程安全
     *
     * @param sourceCode   源代码
     * @param dependencies 依赖包
     * @return
     */
    public static <T> DynamicCompilerClassLoader compile(String classFullPath,
                                       String sourceCode,
                                       List<URL> dependencies, Class<?> targetClass) {

        // 创建一个新的java编译器
        JavaCompiler systemJavaCompiler = CompilerFactory.createCompiler();

        // 创建自定义文件管理器
        StandardJavaFileManager standardFileManager =
                systemJavaCompiler.getStandardFileManager(null, Locale.CHINA, StandardCharsets.UTF_8);

        // 父类加载器要用ExtClassLoader
        CustomFileManager customFileManager = new CustomFileManager(standardFileManager, dependencies, EXTENSION_CLASS_LOADER);

        // 创建待编译的JavaFileObject对象
        StringJavaFileObject stringJavaFileObject = new StringJavaFileObject(classFullPath, sourceCode);

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        // 编译的时候添加依赖包扫描
        List<String> options = getOptions(dependencies, targetClass);

        JavaCompiler.CompilationTask task = systemJavaCompiler.getTask(null, customFileManager,
                diagnosticCollector, options, null, Collections.singletonList(stringJavaFileObject));

        if (task.call()) {
            // 编译成功后将编译成功的类加载器返回回去
            return (DynamicCompilerClassLoader) customFileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
        } else {
            // 如果编译出错，就将异常抛出去
            StringBuilder stringBuilder = new StringBuilder();
            List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
            diagnostics.forEach(diagnostic -> {
                stringBuilder.append(diagnostic.toString());
                stringBuilder.append(Constants.NEW_LINE);
            });
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    /**
     * 获取操作参数
     *
     * @param dependencies
     * @return
     */
    private static List<String> getOptions(List<URL> dependencies, Class<?> targetClass) {
        List<String> options = new ArrayList<>();

        options.add("-cp");

        StringBuilder cp = new StringBuilder();
        if (Objects.nonNull(dependencies)) {
            dependencies.forEach(url -> {
                // 这里根据不同操作系统不同，linux是":" , windows是";"
                cp.append(url.getPath());
                cp.append(File.pathSeparator);
            });
        }
        if (!BASE_JAR_FILE.exists()) {
            String targetClassPath = getTargetClassPath(targetClass);
            BASE_JAR_FILE = ZipUtil.zip(BASE_JAR_FILE, targetClassPath,
                    targetClass.getClassLoader().getResourceAsStream(targetClassPath));
        }
        cp.append(BASE_JAR_FILE.getAbsolutePath());
        options.add(cp.toString());
        return options;
    }

    private static String getTargetClassPath(Class<?> targetClass) {
        return targetClass.getName().replace(".", "/") + ".class";
    }

}
