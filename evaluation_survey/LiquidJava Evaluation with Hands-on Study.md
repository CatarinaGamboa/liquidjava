# LiquidJava Evaluation with Hands-on Study

## Study questions:

- Are refinements easy to understand without a previous explanation about them? *(Part 1)*
- Is it faster to find semantic errors in LiquidJava programs than in plain Java programs? *(Part 3)*
- How hard is it to annotate a program with refinements? *(Part 4)*
- Would people that use Java add this type of verification when creating critical software? *(Final evaluation)*





## **Study Configuration**

<Unsynchronous>

#### [Part 0](part-0-:-registration-in-the-study) - Registration and getting demographic data

<Synchronous>

#### Part 1 - Understand the refinements without prior explanation

3 exercises to explain what the code "means" *(maybe it is hard to evaluate that part)* and select the correct and incorrect uses of the code.

- Simple variable refinement
- Method refinement
- Class: Parameters and State Refinement

#### Part 2 - Overview

Small explanation of the refinements using the examples of the first part. Inquiring doubts

#### Part 3 - Find the Bug

*Add time limit? Maybe only 2 - and merge both returns...?*

3 exercises in Plain Java to find the bug.

3 exercises in LiquidJava to find the bug.

For each write the line with the error, the error and how to fix it

- Incorrect return with ifs
- Incorrect return with recursion
- Incorrect class order invocation (with external libs)

#### Part 4 - Write the Refinements

3 exercises

- Simple variable refinement  - months
- Method refinement - ?
- Class: Parameters and State Refinement - trafficlight





## Details

### PART 0 : Registration in the study

Share registration survey

- Demographic data ~~ to 1st survey 
  - only accept people familiar/very familiar with Java
  - How long have you been programming in Java???
- State available date and time for 1h of synchronized study session, via zoom
- For the zoom session: vscode installed



### PART 1 : Understand the refinement

With no prior knowledge on LiquidJava can you understand what are the specifications of the method?

For each example:

1) Explain in simple words what you get from the written code. (What are the parameters and what is returned)

2) Which of the invocations would be valid and which would produce an error

- Variable Refinement *(Earth surface temperature according to Nasa https://earthobservatory.nasa.gov/global-maps/MOD_LSTD_M)*

  ```java
  @Refinement("-25 <= x && x <= 45")
  int x;
  
  x = 0;     //Correct
  x = 50;    //Incorrect
  x = 10-16; //Correct
  ```

  

- Method annotated - parameters and return. *(Average with parameters dependency)* 

  ```java
  @Refinement("_ >= 0")
  public static double function1(
  	@Refinement("a >= 0") double a, 
  	@Refinement("b >= a") double b){
  	return (a + b)/2;
  }
  
  function1(10, 52); 		   //Correct
  function1(50, -5);         //Incorrect
  function1(8*-5, 60);       //Incorrect
  ```
  
- Object State - Email *(example used in https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/)*

  *To hard on the beginning?*
  
  ```java
  @StateSet({"empty", "senderSet", "receiverSet", "bodySet", "sent"})
  public class Email {
      @StateRefinement(to="empty(this)")
  	public Email() {...}
  	
  	@StateRefinement(from="empty(this)", to="senderSet(this)")
      public void from(String contact) {...}
  	
  	@StateRefinement(from="(senderSet(this) || receiverSet(this))", 					                 to="receiverSet(this)")
  	public void to(String contact) {...}
  	
  	public void subject(String sub) {...}
      
      @StateRefinement(from="receiverSet(this)", to="bodySet(this)")
  	public void body(String s) {...}
      
      @StateRefinement(from="bodySet(this)", to="sent(this)")
      public void send(){...}
  }
  
  //Incorrect
  Email e = new Email();
  e.to("Bob");
  e.from("Alice");
  e.subject("Welcome!");
  e.body("Welcome to this survey!");
  e.send();
  
  //Incorrect
  Email e = new Email();
  e.from("Alice");
  e.to("Bob");
  e.subject("Welcome!");
  e.send();
  
  //Correct
  Email e = new Email();
  e.from("Alice");
  e.to("Bob");
  e.to("Carol");
  e.body("Welcome to this survey!");
  e.send();
  
```
  
  



### PART 2 : Overview

Brief introduction to LiquidJava and how to use Refinements in Java.

- Motivation
- Explaining the examples above and ask for doubts



### PART 3 : Find the Bug

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
- Time spent handling the error (maybe the interviewer can measure it)



#### Exercises

This exercises will be grouped in pairs. For each participant, one of the exercises will be used in part of 2.1 and the other in 2.2, changing the order with each participant. This gives us a baseline to compare the time spent handling the error on the annotated program versus the non-annotated program.



* **1** - Inside Method, Return incorrect, ifs

  * Pair 1/2 - *Absolute*

  ```java
  public class Test1 {
      	/**
      	Absolute value
      	@param n the value for which the absolute is computed
      	@return the absolute value of n
      	*/
          @Refinement("(n < 0) ? (_ == -n) : (_ == n)")
          public static int absolute(int n) {
              if(0 <= n)
                  return -n;//correct: remove signal
              else
                  return 0 - n;
          }
  }
  ```

  * Pair 2/2 - *Maximum*

    ```java
    public class Test1 {
        /**
        Maximum value
        @param a first value
        @param b second value
        @return the maximum between a and b
        */
    	@Refinement("(a < b)? (_ == b) : (_ == a)")
        public static int max(int a, int b){
            if(a > b) //correct: change signal
                return b;
            else
                return a; 
        }
    }
    ```

    

* **2** - Return incorrect - recursion + Alias

  * Pair 1/2 *Sum (in tutorial refinement types)*

    ```java
    @RefinementAlias("Nat(int x) {x >= 0}")
    public class Test2 {
        /**
        * The sum of all numbers between 0 and n
        * @param n
        * @return a positive value that represents the sum of all numbers between 0 and n
        */
        @Refinement("Nat(_) && _ >= n")
    	public static int sum(int n) {
    		if(n <= 0)//1
    			return 0;
    		else {
    			int t1 = sum(n-1);
    			return n + t1;
    		}
    	}
        
    }
    ```

    

  * Pair 2/2 - *Fibonacci*

    ```java
    @RefinementAlias("GreaterEqualThan(int x, int y) {x >= y}")
    public class Test2 {
        /**
        * Computes the fibonacci of index n
        * @param n The index of the required fibonnaci number
        * @return The fibonacci nth number. The fibonacci sequence follows the formula Fn = Fn-1 + Fn-2 and has the starting values of F0 = 1 and F1 = 1 
        */
        @Refinement( "_ >= 1 && GreaterEqualThan(_, n)")
        public static int fibonacci(@Refinement("Nat(n)") int n){
            if(n <= 1)
                return n;//correct: change to 1
            else
                return n *  fibonacci(n-1);
        }
    }
    ```

    

* **3** - State error (using external libs annotated)

  * Pair 1/2 - InputStreamReader, closing and trying to read again  https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html

    ```java
    class Test3 {
    
  	public static void main(String[] args) throws IOException{
            InputStreamReader is = new InputStreamReader(System.in);
  		is.read();
    		is.read();
  		is.close();
    		is.read();//error here
        }
    }
    ```
  
    ```java
    @ExternalRefinementsFor("java.io.InputStreamReader")
    public interface InputStreamReaderRefs {
    	
    	@RefinementPredicate("boolean open(InputStreamReader i)")
    	@StateRefinement(to="open(this)")
    	public void InputStreamReader(InputStream in);
    
    	@StateRefinement(from="open(this)", to="open(this)")
    	@Refinement("(_ >= -1) && (_ <= 127)")
    	public int read();
    	
    	@StateRefinement(from="open(this)", to="!open(this)")
    	public void close();
    }
    ```
  
    
  
  * Pair 2/2 - Socket, creating object but not connecting/bind https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
  
    ```java
    class Test3 {
    
    	public static void main(String[] args) throws IOException{
    		int port = 5000;
    		InetAddress inetAddress = InetAddress.getByName("localhost");    
    		
    		Socket socket = new Socket();
    		socket.bind(new InetSocketAddress(inetAddress, port));
    //		socket.connect(new InetSocketAddress(inetAddress, port));
    		socket.sendUrgentData(90);
    		socket.close();
    	}
    }
    ```
  
    ```java
    @ExternalRefinementsFor("java.net.Socket")
    @StateSet({"unconnected", "binded", "connected", "closed"})
    public interface SocketRefinements {
        
    	@StateRefinement(to="unconnected(this)")
    	public void Socket();
    	
    	@StateRefinement(from="unconnected(this)",to="binded(this)")
    	public void bind(SocketAddress add);
    	
    	@StateRefinement(from="binded(this)", to="connected(this)")
    	public void connect(SocketAddress add);
    	
    	@StateRefinement(from="connected(this)")
    	public void sendUrgentData(int n);
    	
    	@StateRefinement(to="closed(this)")
    	public void close();
    
    }
    ```
  
    
  



### PART 4 - Annotate a Java Program with Refinement Types

Open the project with Java code already implemented but not annotated.

Each package contains a program to annotate and 2 files with tests (one that should be correct and one that should produce an error)

- Simple variable annotation

  ```java
  /* A month needs to have a value between 1 and 12*/
  int currentMonth;
  
  currentMonth = 13;
  currentMonth = 5;
  ```

  

- Annotate a method parameters and return

  ```
  
  ```

  

- Annotate the class TrafficLight that uses rgb values (between 0 and 255) to define the color of the light and follows the protocol defined by the following image

  ![image-20210520135153606](C:\Users\Catarina Gamboa\AppData\Roaming\Typora\typora-user-images\image-20210520135153606.png)

  ```java
  public class TrafficLight {
  	
  	int r; 	
  	int g; 	
  	int b;
  	
  	public TrafficLight() {
  		r = 255; g = 0; b = 0; 
  	}
  	
  	public void transitionToAmber() {
  		r = 255; g = 120; b = 0;
  	}
  	
  	public void transitionToGreen() {
  		r = 76; g = 187; b = 23; 
  	}
  	
  	public void transitionToRed() {
  		r = 230; g = 0; b = -1; 
  	}
  
  }
  ```

  







EXTRA

* **1** - Incorrect Invocation Simple Arithmetics

  * Part 1/2 - *Division by 0*

    ```java
    public class Test3 {
        public static double divide(double numerator, 
                       @Refinement("denominator != 0")double denominator){
    		return numerator/denominator;
    	}
    
    	public static void main(String[] args) {
    		double a; 
    		a = divide(10, 5);
    		a = divide(50, -10+5);
    		a = divide(800, 2*30-60);
            a = divide(1952*2, 20-10);
    		a = divide(5*5*5, -5*-1);
    	}
    }
    ```

    

  * Part 2/2 - *Average Price - only positives*

    ```java
    public class Test3 {
    	public static double averagePrice(
    		@Refinement("price1 >= 0") double price1, 
    		@Refinement("price2 >= 0") double price2){
    		return (price1 + price2)/2;
    	}
    	public static void main(String[] args) {
    		double b; 
    		b = averagePrice(10, 5);
    		b = averagePrice(50, -10+15);
    		b = averagePrice(800, 2*30-60);
            b = averagePrice(1952*-2, 20-10);
    		b = averagePrice(5*5*5, -5*-1);	
    	}
    }
    ```

    