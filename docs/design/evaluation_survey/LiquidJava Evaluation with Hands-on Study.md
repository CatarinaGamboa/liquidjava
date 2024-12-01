# LiquidJava Evaluation with Hands-on Study

## Study questions:

- Are refinements easy to understand without a previous explanation about them? *(Part 1)*
- Is it faster to find semantic errors in LiquidJava programs than in plain Java programs? *(Part 3)*
- How hard is it to annotate a program with refinements? *(Part 4)*
- Would people that use Java add this type of verification when creating critical software? *(Final evaluation)*


## **Study Configuration**

<Unsynchronous>

### [Part 0](part-0-:-registration-in-the-study) - Registration and getting demographic data

<Synchronous>

### Part 1 - Find the Error in Plain Java

2 programs in Plain Java with an error. (Use as baseline for LiquidJava results)

For each write the line with the error and how to fix it

- Incorrect method return
- Incorrect class order invocation (with external libs)

### Part 2 - Understand the refinements without prior explanation

3 exercises to implement code that use the refinements correctly and incorrectly

- Simple variable refinement
- Method refinement
- Class: State Refinement

### Part 3 - Overview

Video and website - Small explanation of the refinements using the examples of the first part. Inquiring doubts.

### Part 4 - Find the Error in LiquidJava

2 exercises in LiquidJava to find the bug.

For each write the line with the error, the error and how to fix it

- Incorrect method return
- Incorrect class order invocation (with external libs)

### Part 5 - Write the Refinements

3 exercises

- Simple variable refinement  - months
- Method refinement - ?
- Class: Parameters and State Refinement - trafficlight

### Part 6 - Final overview
- Final questions



## Details

## PART 0 : Registration in the study

Share registration survey

- Background data ~~ to 1st survey
  - only accept people familiar/very familiar with Java
  - Did you contact with LiquidJava before?
- Send available date and time for 1h of synchronized study-session, via zoom
- For the zoom session: vscode installed



## PART 1 : Understand the refinement

With no prior knowledge on LiquidJava can you understand what are the specifications of the method?
(Only for the ones that answered as not having previous contact with LiquidJava)

For each example:
Make a correct and an incorrect use of the annotated code.

- Variable Refinement *(Earth surface temperature according to Nasa https://earthobservatory.nasa.gov/global-maps/MOD_LSTD_M)*
  Assign a correct value and an incorrect value to x.

  ```java
  @Refinement("-25 <= x && x <= 45")
  int x;
  ```



- Method annotated - parameters and return. *(Average with parameters dependency)*
  Write a correct and an incorrect invocation of function1.

  ```java
  @Refinement("_ >= 0")
  public static double function1(
  	@Refinement("a >= 0") double a,
  	@Refinement("b >= a") double b){
  	return (a + b)/2;
  }
  ```

- Object State - Vending Machine idea: showing products -> product selected -> pay -> show products
Write a correct and incorrect sequence of invocations on the object MyObj

  ```java
  @StateSet({"sX", "sY", "sZ"})
  public class MyObj {

   	@StateRefinement(to="sY(this)")
		public MyObj() {}

		@StateRefinement(from="sY(this)", to="sX(this)")
		public void select(int number) {}

		@StateRefinement(from="sX(this)", to="sZ(this)")
		public void pay(int account) {	}

		@StateRefinement(from="sY(this)", to="sX(this)")
		@StateRefinement(from="sZ(this)", to="sX(this)")
		public void show() { }
  }

  ```


## PART 2 : Overview

Brief introduction to LiquidJava and how to use Refinements in Java.
Video of 4 minutes:
- Motivation
- Explaining the examples above

Off-video:
- Doubts
- Website


## PART 3 : Find the Bug

### 3.1 Plain Java Code

Open project without liquid Java.

Each file contains a bug, try to locate it without running the code.

For each file answer:

- Where was the bug (line)
- What produced the bug
- How can you fix it

### 3.2 Liquid Java Code

Open project that contains the liquid-java-api jar and the files already annotated.

Each file contains a bug, try to locate it without running the code.

For each file answer:

- Where was the bug (line)
- What produced the bug
- How can you fix it
- Time spent handling the error (measured by the interviewer)



#### Exercises

This exercises will be grouped in pairs. For each participant, one of the exercises will be used in part of 2.1 and the other in 2.2, changing the order with each participant. This gives us a baseline to compare the time spent handling the error on the annotated program versus the non-annotated program.


* **1** - Method Return incorrect

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
    		  if(n <= 1)//correct: 0
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
                return 0;//correct: change to 1
            else
                return fibonacci(n-1) + fibonacci(n-2);
        }
    }
    ```



* **3** - State error (using external libs annotated)

  * Pair 1/2 - Socket, creating object but not connecting/bind https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html

    ```java
    class Test3 {

      public void createSocket(InetSocketAddress addr) throws IOException{
            int port = 5000;
            InetAddress inetAddress = InetAddress.getByName("localhost");

            Socket socket = new Socket();
            socket.bind(new InetSocketAddress(inetAddress, port));
            //missing socket.connect(addr);
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

   * Pair 2/2 - ArrayDeque - To use ghost field size to model the object state (https://docs.oracle.com/javase/7/docs/api/java/util/ArrayDeque.html)

    ```java
    class Test3 {

      public static void main(String[] args) throws IOException{
          ArrayDeque<Integer> p = new ArrayDeque<>();
          p.add(2);
          p.remove();
          p.offerFirst(6);
          p.getLast();
          p.remove();
          p.getLast();
          p.add(78);
          p.add(8);
          p.getFirst();
      }
    }
    ```

    ```java

  @ExternalRefinementsFor("java.util.ArrayDeque")
  @Ghost("int size")
  public interface ArrayDequeRefinements<E> {

  	public void ArrayDeque();

  	@StateRefinement(to="size(this) == (size(old(this)) + 1)")
  	public boolean add(E elem);

  	@StateRefinement(to="size(this) == (size(old(this)) + 1)")
  	public boolean offerFirst(E elem);

  	@StateRefinement(from="size(this) > 0", to = "size(this) == (size(old(this)))")
  	public E getFirst();

  	@StateRefinement(from="size(this) > 0", to = "size(this) == (size(old(this)))")
  	public E getLast();

  	@StateRefinement(from="size(this)> 0", to="size(this) == (size(old(this)) - 1)")
  	public void remove();

  	@StateRefinement(from="size(this)> 0", to="size(this) == (size(old(this)) - 1)")
  	public E pop();

  	@Refinement("_ == size(this)")
  	public int size();

  	@Refinement("_ == (size(this) <= 0)")
  	public boolean isEmpty();

  }

    ```






## PART 4 - Annotate a Java Program with Refinement Types

Open the project with Java code already implemented but not annotated.

Each package contains a program to annotate and 2 files with tests (one that should be correct and one that should produce an error)

- Simple variable annotation

  ```java
  /* A month needs to have a value between 1 and 12*/
  int currentMonth;

  currentMonth = 13;
  currentMonth = 5;
  ```

- Method annotation

  ```java
  public class Method {
      /**
       * Returns a value within the range
       * @param a The minimum border
       * @param b The maximum border, greater than a
       * @return A value in the interval [a, b] (including the border values)
       */
      public static int inRange(int a, int b){
          return a + 1;
      }

      public static void main(String[] args) {
          inRange(10, 11); //Correct
          inRange(10, 9); //Error
      }
  }
  ```

- Annotate the class TrafficLight that uses rgb values (between 0 and 255) to define the color of the light and follows the protocol defined by the following image

  ![traffic light protocol](https://github.com/pcanelas/regen/blob/antlr/evaluation_survey/trafficLight.png)

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

## Part 5 - Final Overview

  - [Optional] What did you enjoy the most while using LiquidJava?
  - [Optional] What did you dislike the most while using LiquidJava?
  - Would you use LiquidJava in your projects?




## EXTRA METHODS - NOT USED

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
