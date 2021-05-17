# LiquidJava Evaluation with Hands-on Study

Study questions:

- Is it faster to find semantic errors in LiquidJava programs than in plain Java programs? *(Part 2)*
- How hard is it to annotate a program with refinements? *(Part 3)*
- Would people that use Java add this type of verification when creating critical software? *(Final evaluation)*



### PART 1 - Introduction

Brief introduction to LiquidJava and how to use Refinements in Java.

- Motivation
- Simple Example - *Positive value*
- Example of method refinements and invocation - *maybe Percentage and addBonus (poster)*
- Example of object state refinement - *Traffic Lights*

***Idea:*** Create a small 5 min video and ask participants to watch the video before the session. In the beginning of the session they can watch the video if they couldn't do it before and ask questions/doubts.



### PART 2 - Find the Bug

#### 2.1 Plain Java Code

Open project without liquid Java. 

Each file contains a bug, try to locate it without running the code.

For each file answer:

- Where was the bug (line)
- What produced the bug
- How can you fix it

#### 2.2 Liquid Java Code

Open project that contains the liquid-java-api jar and the files already annotated. 

Each file contains a bug, try to locate it without running the code.

For each file answer:

- Where was the bug (line)
- What produced the bug
- How can you fix it







### PART 3 - Annotate a Java Program with Refinement Types

Open the project with Java code already implemented but not annotated.

Each package contains a program to annotate and 2 files with tests (one that should be correct and one that should produce an error)

- Annotate a method parameters and return
- Annotate a class following a protocol