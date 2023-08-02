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
### Code for reproducing the issue
```
public static void main(String[] args) throws IOException, ClassNotFoundException {
        //run on jdk1.8
        String projectPath = "/Users/gmy/Documents/openProjects";

        System.out.println("------parse source↓↓↓↓-------");
        // read .java
        JavaProjectBuilder sourceBuiler = new JavaProjectBuilder();
        sourceBuiler.addSource(new File(projectPath + "/qdox-generic-demo/src/main/java/demo/dto/SourceDto.java"));
        JavaClass sourceDto = sourceBuiler.getClassByName("demo.dto.SourceDto");
        System.out.println("sourceDto getTypeParameters >> " + sourceDto.getTypeParameters());
        for (JavaField field : sourceDto.getFields()) {
            System.out.println(
                    "sourceDto." + field.getName() + " getGenericValue >> " + field.getType().getGenericValue());
        }

        System.out.println("------parse bytecode↓↓↓↓-------");
        // but parse class Missing generic information
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
    }
```

### Printed Output

```
------parse source↓↓↓↓-------
sourceDto getTypeParameters >> [T]
sourceDto.data getGenericValue >> T
sourceDto.datas getGenericValue >> List<T>
sourceDto.actualDatas getGenericValue >> List<String>
------parse bytecode↓↓↓↓-------
classDto getTypeParameters >> []
classDto.data getGenericValue >> java.lang.Object
classDto.datas getGenericValue >> java.util.List
classDto.actualDatas getGenericValue >> java.util.List
```

The printed output above does not include generic information T for classDto，The information is included in the bytecode.

How can I retrieve the generic information from the bytecode? Like This：
```
classDto getTypeParameters >> [T]
classDto.data getGenericValue >> T
classDto.datas getGenericValue >> List<T>
classDto.actualDatas getGenericValue >> List<String>
```



This is my demo project. https://github.com/growmuye/qdox-generic-demo.git

Please refer to the code above in demo.Example.
