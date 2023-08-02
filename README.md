# qdox parses bytecode without generic information
Hello, I encountered an issue while using qdox. When qdox parses bytecode, the returned JavaClass does not contain generic information

### maven
```
   <dependency>
      <groupId>com.thoughtworks.qdox</groupId>
      <artifactId>qdox</artifactId>
      <version>2.0.0</version>
    </dependency>
```
### Reproducible Code
```
public static void main(String[] args) throws IOException, ClassNotFoundException {
        //run on jdk1.8
        String projectPath = "project path here";

        // read .java
        JavaProjectBuilder sourceBuiler = new JavaProjectBuilder();
        sourceBuiler.addSource(new File(projectPath + "/qdox-gereric-demo/src/main/java/demo/dto/SourceDto.java"));
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
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new File(projectPath + "/qdox-gereric-demo/target/").toURL() },
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
```

### Printed Output

```
sourceDto getTypeParameters >> [T]
sourceDto.data getGenericValue >> T
sourceDto.datas getGenericValue >> List<T>
sourceDto.actualDatas getGenericValue >> List<String>
-------------------
classDto getTypeParameters >> []
classDto.data getGenericValue >> java.lang.Object
classDto.datas getGenericValue >> java.util.List
classDto.actualDatas getGenericValue >> java.util.List
-------------------
```

The printed output above does not include generic information T for classDto；

### How can I retrieve the generic information from the bytecode?
This is my demo project. https://github.com/growmuye/qdox-generic-demo.git
