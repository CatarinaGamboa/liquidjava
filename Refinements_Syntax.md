# Java Refinements 

## Syntax Examples

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
@Refinement("goodGrade > positive && goodGrade < excellentGrade")
int goodGrade = 17;
```

**B**

```java
@Refinement("{x | x >= 10}")
int positiveGrade = 10;
@Refinement("{y | y == 19 || y == 20}")
int excellentGrade = 19;
@Refinement("{x | x > positive && x < excellentGrade}")
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
@Refinement("type PtGrade = (\\v >= 0 && \\v <= 20)")
class MyClass{
    ...
    @Refinement(positiveGrade == PtGrade && positiveGrade >= 10)
    int positiveGrade = 12;
}
```

**B**

```java
@Refinement("type PtGrade(int x) { x >= 0 && x <= 20}")
class MyClass{
    ...
    @Refinement(PtGrade(positiveGrade) && positiveGrade >= 10)
    int positiveGrade = 12;
}
```



### Uninterpreted Functions (???)

```java
@Refinement("function int f (int x)")
class MyClass{
    ...
    @Refinement("b == 3")
    int b = 3;
    @Refinement("a != f(1)")
    int a = f(b);
}
```



...