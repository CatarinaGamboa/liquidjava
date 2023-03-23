# Separation logic in Liquid Java

TLDR;
* Proposition separation logic
* No recursive functions/data
* No records

But it is still usefull. As an example a permission system is implemented.

## Constraints of SMT solver

The only SMT solver supporting separation logic is `CVC5` and it supports only a subset of SL, called `propositional separation logic`. 

The restriction is enforced by making logic quantifier free. Later it will be shown how restrictive that is.

## Why there are no recursive data structures

```java
@Predicate("
    isListSegment(l, e) <=> 
           (l == e && sep.emp) 
        || (l != e && ∃y. l -> [next : y] * isListsegment(y, e))
                      ^
                      |
        impossible ---+ as logic must be QF
")
@Predicate("
    isList(l) <=> isListSegment(l, sep.nil)
")
class List{
    Object data;
    List next;

    @isList("_") // <- postcondition
    static List empty(){
        return null; //isList(null) = true
    }

    @isList("_")
    static List append(@isList("l") List l,
                       Object x){
        var newHead = new Node(); // heap: newHead -> - 
        // => hewHead -> - * isList(l) by frame rule
        newHead.datat = x; 
        newHead.next = l;
        return newHead; // isList(newHead) = true
    }
}

```

## Why there are no records

It is very often that we are trying to state that two pointer are disjoint, but 
we do not now what are precise valeus inside. 

```java
class Main{
    static int main(...){

    }
    //postcondition
    @Predicate("x -> ? * y -> ?") // = ∃ x_v, y_v. x -> x_v * y -> y_v
    //                                 ^
    //                                 |
    // impossible as logic must be QF -+
    static void foo(Object x, 
                    @Predicte("x -> ? * y -> ?") //precondition
                    Object y){...}
}

```

With this constraint the only way two judge even the simpliest case, is two make all values the same. The `x_v` and `y_v` must be of type unit and share the same value `()`.

```java
class Main{
    static int main(...){

    }
    //postcondition
    @Predicate("x -> () * y -> ()") // = x -> () * y -> ()
    static void foo(Object x, 
                    @Predicte("x -> () * y -> ()") //precondition
                    Object y){...}
}
```
As fields are implemented as uninterpreted functions, this move makes it impossible to distinguish between values and impossible to look inside objects, even if they have fields, making all of them of type `Object`. 

-------------------

## Propositional separation logic

Here I will show some examples where separation logic might be useful. 

## Stream example

```java
class Stream{
    @HeapPostcondition("_ -> ()") //repest consumes nothing and returns new stream
    static Stream repeat(Object x, int n){...}

    @HeapPrecondition("left -> () * right -> ()")
    @HeapPostcondition("_") //zip consumes two streams, returns new one
    static Stream zip(Stream left, Stream right){...}

    @HeapPrecondition("s -> ()") //"consume" consumes one stream
    static void consume(Steam s){...}
}

class Main{
    static void main(int argc, String[] argv){
        var l = Stream::repest(new Object(), 10);
        //heap: [l -> ()]

        var r = Stream::repest(new Object(), 10);
        //heap: [l -> (), r -> ()]

        //Stream::consume(r);  ---------+
        //                              |
        //                              v
        var z = Stream::zip(l, r);// compile error
        //heap: [z -> ()]

        //Stream::consume(l); //compile error as l is consumed by zip

        Stream::consume(z);
    }
}   
```

## List example

```java

class List{
    Node head;

    @HeapPostcondition("_ -> ()")
    static List ListOf(....){}

    @HeapPrecondition("left -> () * right -> ()")
    @HeapPostcondition("left -> ()") //right is appended to the tail of left
    static List concat(List left, List right){...}
}


class Main{
    static void main(int argc, int argv){
        var x = ListOf(1, 2, 3, 4);
        //heap: x -> ()
        var y = ListOf(1, 2, 3, 4, 5);
        //heap: x -> () * y -> ()

        //x = concat(x, x) - compile error as x is not disjoin with itself

        var z = concat(x, y); 
        //heap: x == z && x -> ()

        // var p = concat(z, y); - compile error
    }
}

```


