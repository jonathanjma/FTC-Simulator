package Utilities;

import javafx.scene.Group;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompileUtil {

    public static void main(String[] args) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InterruptedException {
//        Thread.sleep(5000);
        reloadPathsUtil(new Group());
    }

    static String className = "AutoPathsUtil";
    public static AutoPathsUtil reloadPathsUtil(Group pathsGroup) throws IOException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException {

        File parent = new File(System.getProperty("user.dir")+"/src/Utilities/");
        File sourceFile = new File(parent, className + ".java");
        File parentDir = sourceFile.getParentFile();

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        javaCompiler.run(null, null, null, sourceFile.getAbsolutePath());

        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {parentDir.toURI().toURL()});
        Class<?> dynamicClass = urlClassLoader.loadClass("Utilities."+className);
        Constructor<?> constructor = dynamicClass.getConstructor(Group.class);

        return (AutoPathsUtil) constructor.newInstance(pathsGroup);
    }
}
