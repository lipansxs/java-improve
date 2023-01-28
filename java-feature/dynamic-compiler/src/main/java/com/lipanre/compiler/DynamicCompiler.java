package com.lipanre.compiler;


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

    private final static String JAVA_HOME_PATH = System.getProperty("java.home");

    /**
     * 编译源码，线程安全
     *
     * @param sourceCode   源代码
     * @param dependencies 依赖包
     * @return
     */
    public static Class<?> compile(String classFullPath,
                                   String sourceCode,
                                   List<URL> dependencies)
            throws ClassNotFoundException {

        // 创建一个新的java编译器
        JavaCompiler systemJavaCompiler = CompilerFactory.createCompiler();

        // 创建自定义文件管理器
        StandardJavaFileManager standardFileManager =
                systemJavaCompiler.getStandardFileManager(null, Locale.CHINA, StandardCharsets.UTF_8);

        // 父类加载器要用ExtClassLoader
        CustomFileManager customFileManager = new CustomFileManager(standardFileManager,
                dependencies, EXTENSION_CLASS_LOADER);

        // 创建待编译的JavaFileObject对象
        StringJavaFileObject stringJavaFileObject = new StringJavaFileObject(classFullPath, sourceCode);

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        // 编译的时候添加依赖包扫描
        List<String> options = getOptions(dependencies);

        JavaCompiler.CompilationTask task = systemJavaCompiler.getTask(null, customFileManager,
                diagnosticCollector, options, null, Collections.singletonList(stringJavaFileObject));

        if (task.call()) {
            // 编译成功后将编译成功的类的class对象返回回去
            ClassLoader classLoader = customFileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
            return classLoader.loadClass(classFullPath);
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
     * @param dependencies
     * @return
     */
    private static List<String> getOptions(List<URL> dependencies) {
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

        cp.append(JAVA_HOME_PATH + "/lib/rt.jar");
        options.add(cp.toString());
        return options;
    }

}
