package Utilities;

import javax.tools.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class Test {

    static class TestClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name.equals("Utilities.Test1")) {
                try {
                    InputStream is = new FileInputStream("src/Utilities/Test1.class");
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

    public static void main(String[] args) throws Exception {

        Test1 test1 = new Test1(); test1.hello();

        while (true) {
            Class<?> dynamicClass = new TestClassLoader().loadClass("Utilities.Test1");
            Constructor<?> constructor = dynamicClass.getConstructor();
            BaseTest1 newTest1 = (BaseTest1) constructor.newInstance();
            newTest1.hello();

            File sourceFile = new File("src/Utilities/Test1.java");
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            javaCompiler.run(null, null, null, sourceFile.getAbsolutePath());

            Thread.sleep(5000);
        }
    }
}