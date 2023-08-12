import org.apache.commons.io.FileUtils;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * 测试解析java源码
 *
 * @author lipanre
 * @date 2023/5/24 19:35
 */
public class Main {

    public static void main(String[] args) throws IOException {

        URL resourceUrl = Main.class.getClassLoader().getResource("Test.java");
        if (Objects.isNull(resourceUrl)) {
            return;
        }
        Launcher launcher = new Launcher();
        CtTypeReference<Object> iInterpreter = launcher.getFactory().createInterface().getReference();
        iInterpreter.setSimpleName("IInterpreter");

        CtModel ctModel = launcher.buildModel();
        String code = FileUtils.readFileToString(new File(resourceUrl.getPath()), "utf8");
        CtClass<?> ctClass = Launcher.parseClass(code);
        ctClass.addSuperInterface(iInterpreter);
        System.out.println(ctClass);
    }

}
