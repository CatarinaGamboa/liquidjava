## Rules for Liquid Type Checking in Java

**Grammar for Definition of Refinements**

```
S :: = G | F;

G :: =   \v BOLP E
		| x BOLP E;

E :: = c
      | x
      | E BINOP E
	  | E BOLP E;

F ::= "{" G "}" F'

F'::= "->" F 
      | ;
      
BINOP ::= + | - | * | / | %  ; 			  
BOLP ::= > | >= | < | <= |== | != ;
```

**Examples inside @Refinement**

```
\v == 23
a > 10
a > 10 && a < 50
a == 10+3 || a == 50
{a > 0} -> { \v < 0}
```



​	**Context**

```
Γ ::= empty
	| Γ,  x: {B | exp}
	//TODO add functions params and returns

e: expressions; 
s: statements; 
c: constants; 
x: variables
```



**Rules**

**Constant**

```
 -------------------------------------------  	type = {int, boolean, String}
    	Γ|- c : {type | \v == c}			
```



**Variable**

```
     x : T in Γ 
-----------------------------------------------    
    Γ|-  x :  T
```



**Variable Declaration**

If @R is ommited, the Refinement of the variable is True

```
 Γ|- e1: T			  Γ|-T <: {U | e2 [v/x]}      Γ , x: {U | e2 [v/x]} |- s valid
----------------------------------------------------------------------------------------
                    Γ |- @R( e2 ) U x = e1; s  valid	
```





**Assignment**

```
   Γ|- e1 : T 	    x:{U | e2} in Γ 	   Γ|- T <:{U | e2}       Γ|- s valid
--------------------------------------------------------------------------------------
                             Γ|- x = e1; s valid
```

  Example

```java
@Refinement(a > 0)
int a = 10;
a = 5;
5:{int | \v == 5} <: {a > 0}
```



**Arithmetic Operations**

```
        Γ|- e1 : {U | e1' }	        Γ|- e2: {U | e2' }		 
------------------------------------------------------------ p={+,-,*,/, %, ||, &&}
    Γ|- e1 p e2 :  {U | e1' && e2' && (\v == e1 p e2)}				
```

Example:

```java
@Refinement("a > 10")
int a = 10;
@Refinement("b < 50")
int b = 20;

@Refinement(e1)
int c = a + b
"(c == a + b && a > 10 && b < 50) <: e1"
```



**If** 

```
Γ |- e: boolean	      Γ , e|- S valid	
---------------------------------------
     Γ |-  if (e) S  valid
```



**If-Else**

```
Γ|- e: boolean	      Γ , e|- S1 valid        Γ, !e|- S2 valid	
-----------------------------------------------------------------
              Γ |-  if (e) S1 else S2  valid
```



**Function Declaration** - ???????????????????????????

```
Ex:
@Refinement({a > 0} -> {b < 5} -> {x > 10})
public int foo(int a, int b){
...
}

a: {int | a > 0}
b: {int | b < 5 && a > 0} //So the parameters can dependend on the previous ones
x: {int | x > 10 && a > 0 && b > 0}// The same for the return of the function
```

```
Γ |- e1: T   Γ|-  p : U	 ...
-------------------------------------------------------------------------------------- 
                    Γ|-  @R(e1)  public  U  f (p) {s}  : U
```

