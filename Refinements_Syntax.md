# Java Refinements 

## Refinements in Java - Syntax
## Introduction
This survey aims to assess the best syntax for the implementation of Refinement Types in Java.
Refinement types have been proposed as an incremental approach for program verification that is directly embedded in the programming language. We propose the usage of Refinement Types within the Java programming language not only as a means for program verification but also for fault localization and efficient mutation in the context of software repair.

This survey is being conducted in the context of the master thesis "Æon: Extending Java with Refinements" by Catarina Gamboa, advised by professor Alcides Fonseca, at Lasige, Faculdade de Ciências da Universidade de Lisboa.

All responses to this questionnaire will be anonymous and the treatment of the information obtained will be used only and exclusively in the context of the refered thesis.

It takes about X minutes to complete this survey.
We thank you in advance for your precious collaboration.


### Familiarity with tools
This section serves as assessment of the knowledge ground on the different technologies used in the following sections.

**1.** How familiar are you with Java?
- [ ] Very familiar
- [ ] Vaguely familiar
- [ ] Not familiar

**2.** How familiar are you with Functional Languages (ex:Haskell, Scala)?

- [ ] Very familiar
- [ ] Vaguely familiar
- [ ] Not familiar

**3.** How familiar are you with JML (Java Modeling Language)?

- [ ] Very familiar
- [ ] Vaguely familiar
- [ ] Not familiar

**4.** How familiar are you with Refinement Types?
- [ ] Very familiar
- [ ] Vaguely familiar
- [ ] Not familiar

(If vaguely or not familiar with Refinement Types goes to next section, otherwise steps to the following section)

### Introduction to Refinement Types
Refinement Types extend a language with predicates (boolean expressions) over the basic types. 
A popular syntax is {v : T | p(v) }, of which {x : Integer | x > 0} is an instance.

The example bellow represents a way to apply refinements in Java, where the variable y has the type int and a refinement that only allows y to have positive values which are lesser than 50. When the variable is assigned the value 10 no errors will be shown, but if the assigned value is 100 the compiler will send a refinement type error to the developer.

```java
@Refinement("y > 0 && y < 50")
int y = 10; //okay
int y = 100; //okay in Java, refinement type error
```
In the following sections you will be presented with several syntax options for the implementation of refinements in Java.

### Wildcard Variables

To represent a variable without using its complete name, we can use a wildcard in the refinement that will be associated with the variable value.

Analyse the following syntax possibilities for wildcard variables.

**A**

```java
@Refinement("\\v > 0")
int biggerThanZero = 10;
```

**B**

```java
@Refinement("? > 0")
int biggerThanZero = 10;
```

**C**

```java
@Refinement("_ > 0")
int biggerThanZero = 10;
```

Which of the above syntaxes would you use? (possibility for multiple answers)

- [ ] A
- [ ] B
- [ ] C

For the following sections we will be using "\\\v" as the wildcard variable.



### Variables

In this section we present syntax examples for refinements in variables.
The refinements in this example are related to a grading system from 0 to 20, where the refinements express the following conditions:

- positiveGrade is an int greater or equal to 10;
- excellentGrade is an int equal to 19 or to 20;
- goodGrade is an int with a value between positive and excellentGrade.

Analyse the following examples with syntax possibilities.

**A**

```java
@Refinement("positiveGrade >= 10")
int positiveGrade = 10;
@Refinement("excellentGrade == 19 || excellentGrade == 20")
int excellentGrade = 19;
@Refinement("goodGrade > positiveGrade && goodGrade < excellentGrade")
int goodGrade = 17;
```

**B**

```java
@Refinement("{x | x >= 10}")
int positiveGrade = 10;
@Refinement("{y | y == 19 || y == 20}")
int excellentGrade = 19;
@Refinement("{x | x > positiveGrade && x < excellentGrade}")
int goodGrade = 17;
```

Which of the above syntaxes would you use? (possibility for multiple answers)

- [ ] A
- [ ] B



### Methods

In this section we present syntax examples for refinements in methods, which includes refinements for the parameters and the return.
These refinements express the following conditions:

- grade, the first parameter, is an int greater than 0;
- scale, the second parameter, is a positive int;
- the return value must be an int between 0 and 100.

Analyse each of the examples bellow.

**A**

```java
@Refinement("\\v >= 0 && \\v <= 100")
public static int percentageFromGrade(@Refinement("grade >= 0") int grade, 
                                      @Refinement("scale > 0")  int scale){...}
```

**B**

```java
@Refinement("{grade >= 0} -> {scale > 0} -> {\\v >= 0 && \\v <= 100}")
public static int percentageFromGrade(int grade, int scale){...}
```

Which of the above syntaxes would you use? (possibility for multiple answers)

- [ ] A
- [ ] B



### **Alias**

Several implementations of refinement types include alias for a group of predicates. 
In this section we present possible syntaxes for the creation of the alias and their usage in variable refinements.

PtGrade is a refinement alias that describes an int between 0 and 20 - grade range used in the Portuguese higher education system.

Analyse the following syntax examples.

**A**

```java
@Refinement("PtGrade refines Integer | \\v >= 0 && \\v <= 20")
class MyClass{
    ...
    @Refinement("positiveGrade == PtGrade && positiveGrade >= 10")
    int positiveGrade = 12;
}
```

**B**

```java
@Refinement("PtGrade refines Integer where (\\v >= 0 && \\v <= 20)")
class MyClass{
    ...
    @Refinement("positiveGrade == PtGrade && positiveGrade >= 10")
    int positiveGrade = 12;
}
```



**C**

```java
@Refinement("type PtGrade(int x) { x >= 0 && x <= 20}")
class MyClass{
    ...
    @Refinement("PtGrade(positiveGrade) && positiveGrade >= 10")
    int positiveGrade = 12;
}
```

Which of the above syntaxes would you use? (possibility for multiple answers)

- [ ] A
- [ ] B

### Uninterpreted Functions

To invoke functions inside the Refinements, these functions must be declared in the program as ghost functions, that are only relevant for the specification of the program properties. These functions work as uninterpreted functions, which means that only their signature is needed and not their implementation. 

In this section we present possible syntaxes for the declaration and usage of ghost functions inside the class MyList.

len is the ghost function, that receives a List, and returns an int value. This ghost function is then used inside the refinements of the following Java functions:

	-	createList() : the refinement ensures that the len of the returned list is equal to 0
	-	append(): the refinement ensures that the returned List has the same len as the given list, plus one

Analyse the following syntax examples.

**A**

```java
@Refinement("ghost int len(List xs)")
class MyList{
    static final int MAX_VALUE = 50;
    
    @Refinement("len(\\v) == 0")
    public List createList(){...}
    
    @Refinement("len(\\v) == 1 + len(xs)")
    public List append(List xs, int k){...}
}
```

**B**

```java
class MyList{
    static final int MAX_VALUE = 50;
    
    @Refinement("ghost int len(List xs)")
    @Refinement("len(\\v) == 0")
    public List createList(){...}
    
    @Refinement("len(\\v) == 1 + len(xs)")
    public List append(List xs, int k){...}
}

```

**C**

```java
class MyList{
    @Refinement("ghost int len(List xs)")
    static final int MAX_VALUE = 50;
    
    @Refinement("len(\\v) == 0")
    public List createList(){...}
    
    @Refinement("len(\\v) == 1 + len(xs)")
    public List append(List xs, int k){...}
}
```

Which of the above syntaxes would you use? (possibility for multiple answers)

- [ ] A
- [ ] B
- [ ] C
