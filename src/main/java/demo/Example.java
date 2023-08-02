package demo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.OrderedClassLibraryBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

public class Example {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //run on jdk1.8
        String projectPath = "/Users/gmy/Documents/openProjects";

        // read .java
        JavaProjectBuilder sourceBuiler = new JavaProjectBuilder();
        sourceBuiler.addSource(new File(projectPath + "/qdox-generic-demo/src/main/java/demo/dto/SourceDto.java"));
        JavaClass sourceDto = sourceBuiler.getClassByName("demo.dto.SourceDto");
        System.out.println("sourceDto getTypeParameters >> " + sourceDto.getTypeParameters());
        for (JavaField field : sourceDto.getFields()) {
            System.out.println(
                    "sourceDto." + field.getName() + " getGenericValue >> " + field.getType().getGenericValue());
        }

        System.out.println("-------------------");

        // but read class Missing generic information
        JavaProjectBuilder classBuilder = new JavaProjectBuilder(
                new OrderedClassLibraryBuilder().appendClassLoader(ClassLoader.getSystemClassLoader()));
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new File(projectPath + "/qdox-generic-demo/target/").toURL() },
                ClassLoader.getSystemClassLoader());
        classBuilder.addClassLoader(urlClassLoader);
        JavaClass classDto = classBuilder.getClassByName("demo.dto.SourceDto");

        System.out.println("classDto getTypeParameters >> " + classDto.getTypeParameters());
        for (JavaField field : classDto.getFields()) {
            System.out.println(
                    "classDto." + field.getName() + " getGenericValue >> " + field.getType().getGenericValue());
        }

        System.out.println("-------------------");

        // the Class object in Java contains this information.
        Class<?> aClass = urlClassLoader.loadClass("demo.dto.SourceDto");
        //.....
    }
}
