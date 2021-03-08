package main.Utilities;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class CompileUtil {

    public static String className = "Paths";

    public static BasePaths reloadPaths()
            throws ReflectiveOperationException {

        File sourceFile = new File("src/main/" + className + ".java");
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        javaCompiler.run(null, null, null, sourceFile.getAbsolutePath());

        Class<?> dynamicClass = new PathsClassLoader().loadClass("main." + className);
        Constructor<?> constructor = dynamicClass.getConstructor();
        return (BasePaths) constructor.newInstance();
    }

    static class PathsClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name.equals("main." + className)) {
                try {
                    InputStream is = new FileInputStream("src/main/" + className + ".class");
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
