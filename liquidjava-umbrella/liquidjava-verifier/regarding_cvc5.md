# Notes on integraing cvc5 to Liquid Java:

## Overview

One need to clone and build cvc5 somewhere in the system. I did it using [official guide](https://cvc5.github.io/docs/cvc5-1.0.0/installation/installation.html).
Then use absolute path to jar file in `liquidjava-umbrella/liquidjava-verifier/pom.xml`. It is not the best way, but it is easy to do and reproduce. So it will do for now.

## Path variables

Error looked like this:
```
Found Java: /usr/local/Cellar/openjdk/19/bin/java (found version "19.0.0") found components: Development 
CMake Error at /usr/local/Cellar/cmake/3.24.2/share/cmake/Modules/FindPackageHandleStandardArgs.cmake:230 (message):
  Could NOT find JNI (missing: AWT JVM)
Call Stack (most recent call first):
  /usr/local/Cellar/cmake/3.24.2/share/cmake/Modules/FindPackageHandleStandardArgs.cmake:594 (_FPHSA_FAILURE_MESSAGE)
  /usr/local/Cellar/cmake/3.24.2/share/cmake/Modules/FindJNI.cmake:562 (find_package_handle_standard_args)
  src/api/java/CMakeLists.txt:133 (find_package)
```

Solution is to find existing jvm installation or install openjdk. For me it was not enough, there were some path variables to be set. In my case the following did the trick (passing through configuation stage).

```bash
export JAVA_HOME=/usr/local/Cellar/openjdk/19/libexec/openjdk.jdk/Contents/Home/
export JAVA_INCLUDE_PATH="${JAVA_HOME}/include"
export JVM="${JAVA_HOME}/lib/server/libjvm.so"        
export AWT="${JAVA_HOME}/lib/libjawt.so"
```

## Dependencies to maven project

The easiest and the dirtiest way to do it.

```xml
<dependency>
    <groupId>io.github.cvc5</groupId>
    <artifactId>cvc5</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>absolute/path/to/cvc/repo/build/install/share/java/cvc5.jar</systemPath>
</dependency>
```

## Better dependencies to maven project

After building cvc one needs to install jar to maven local repository.

```bash
 mvn install:install-file \
      -Dfile=absolute/path/to/cvc/repo/build/install/share/java/cvc5.jar \ 
      -DgroupId=io.github.cvc5 -DartifactId=cvc5 \
      -Dversion=1.0 \
      -Dpackaging=jar

```

```xml
<dependency>
    <groupId>io.github.cvc5</groupId>
    <artifactId>cvc5</artifactId>
    <version>1.0</version>
</dependency>
```

Then run building process for liquid java

## Symblos not found/loaded

One should compile Liquid Java project with the same JVM as cvc5!