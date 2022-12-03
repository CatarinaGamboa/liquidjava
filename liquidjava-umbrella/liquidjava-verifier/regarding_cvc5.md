# Notes on state of Separating logic support in SMT-solvers

## TL;DR

Sadly, as of the end of 2022 the the best one can get is `CVC5`, but even there it is only a small subset. There is no way to do `exists v. x -> v`. It requires to use `ALL` in `set-logic` command and gives `unknown` even for simpliest examples.

## Sources of information

Best on is the [sl-comp](https://sl-comp.github.io/), the should be more links to relevant sources there, but as usual, it is best one can get. There will be some usefull information about separation logic subsets supported and what one should expect from differenet tools. The only SMT-sovler participated there is CVC4 and it handles only `propositional separation logic`. No quantifiers and no recursive functions. Other pariticipants are standalone tools with no uniform API. Also, majority of them are only available as binaries and have no documenation. 

The best source of information is however the [repository of sl-comp18](https://github.com/sl-comp/SL-COMP18/), there are pieces of code and text which can be comprehended by common human being and they give said human an understanding of what is acutally possible to do with each tool. 

There are couple useful papers:
- [SMT-lib doc](https://smtlib.cs.uiowa.edu/papers/smt-lib-reference-v2.6-r2021-05-12.pdf)
- [SMT-lib tutorial](http://smtlib.github.io/jSMTLIB/SMTLIBTutorial.pdf)
- [SL-COMP syntax expanstion for smt-lib](https://hal.archives-ouvertes.fr/hal-02388022/document)

I CAN NOT STRESS THIS ENOUGH: SL-COMP uses custom expansion for smt-lib and custom separation logic subset naming, so there are no other sources except their repository. 

# Notes on integrating cvc5 to Liquid Java

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

## no cvc5jni in java.library.path

One should add this flag to JVM, for it to find jni files.

```
-Djava.library.path="/Path/to/cvc5/repository/cvc5/build/src/api/java"
```

Hard part is that there are tests which are running under `maven-surefire-plugin`.
Today's solution is not idiomatic, but a working one.

The pom file need to be changed. Line 
```xml
<argLine>-Dgumtree.match.gt.minh=1></argLine>
```
to

```xml
<argLine>-Dgumtree.match.gt.minh=1 -Djava.library.path="/Path/to/cvc5/repository/cvc5/build/src/api/java"</argLine>
```