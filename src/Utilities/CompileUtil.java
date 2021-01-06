package Utilities;

import javafx.scene.Group;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class CompileUtil {

    public static String className = "AutoPathsUtil";

    public static BasePathsUtil reloadPathsUtil(Group pathsGroup, int startingColorValue, double colorInterval)
            throws ReflectiveOperationException {

        File sourceFile = new File("src/Utilities/"+className+".java");
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        javaCompiler.run(null, null, null, sourceFile.getAbsolutePath());

        Class<?> dynamicClass = new PathUtilClassLoader().loadClass("Utilities."+className);
        Constructor<?> constructor = dynamicClass.getConstructor(Group.class, int.class, double.class);
        return (BasePathsUtil) constructor.newInstance(pathsGroup, startingColorValue, colorInterval);
    }

    static class PathUtilClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name.equals("Utilities."+className)) {
                try {
                    InputStream is = new FileInputStream("src/Utilities/"+className+".class");
                    byte[] buf = new byte[10000];
                    int len = is.read(buf);
                    return defineClass(name, buf, 0, len);
                } catch (IOException e) {
                    throw new ClassNotFoundException("", e);
                }
            }
            return getParent().loadClass(name);
        }
    }
}
