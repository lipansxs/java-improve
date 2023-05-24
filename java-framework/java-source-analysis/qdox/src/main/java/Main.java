import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import com.thoughtworks.qdox.type.TypeResolver;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author lipanre
 * @date 2023/5/24 22:01
 */
public class Main {

    public static void main(String[] args) throws IOException {

        URL resource = Main.class.getClassLoader().getResource("Test.java");

        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.setEncoding("utf8");


        JavaSource javaSource = javaProjectBuilder.addSource(resource);

        System.out.println(javaSource.getClasses());
        System.out.println(javaSource.getPackage());
        System.out.println(javaSource.getImports());

        // 添加import
        javaSource.getImports().add("com.lipanre.xxx.IInterpreter");

        // 添加实现接口
        List<JavaClass> interfaces = javaSource.getClasses().get(0).getInterfaces();
        TypeResolver typeResolver = TypeResolver.byPackageName(javaSource.getPackageName(), javaSource.getJavaClassLibrary(), javaSource.getImports());
        interfaces.add(new DefaultJavaParameterizedType("com.lipanre.xxx.IInterpreter", "IInterpreter", 0,  typeResolver));
        interfaces.add(new DefaultJavaParameterizedType("com.lipanre.xxx.IInterpreter2", "IInterpreter2", 0,  typeResolver));
        ((DefaultJavaClass) javaSource.getClasses().get(0)).setImplementz(interfaces);

        System.out.println(javaSource.getPackageName());
        System.out.println(javaSource.getClassNamePrefix());

        System.out.println(javaSource);
    }

}

