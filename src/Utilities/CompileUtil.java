package Utilities;

import javafx.scene.Group;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CompileUtil {

    public static String className = "AutoPathsUtil";

    public static BasePathsUtil reloadPathsUtil(Group pathsGroup) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException {

        File sourceFile = new File("src/Utilities/"+className+".java");
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        javaCompiler.run(null, null, null, sourceFile.getAbsolutePath());

        Class<?> dynamicClass = new PathUtilClassLoader().loadClass("Utilities."+className);
        Constructor<?> constructor = dynamicClass.getConstructor(Group.class);
        return (BasePathsUtil) constructor.newInstance(pathsGroup);
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
