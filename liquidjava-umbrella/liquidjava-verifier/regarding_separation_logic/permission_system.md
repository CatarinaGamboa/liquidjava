

<!-- ## Structural rules

# Affine types

Weakening
```
   Γ |- Σ
---------- 
Γ, A |- Σ

```
Contraction

```
Γ, A, A |- Σ                Γ |- A, A, Σ
--------------             --------------- 
   Γ, A |- Σ                Γ |- A, Σ

```
Permutation
```
Γ₁, A, Γ₂, B |- Σ          Γ |- Σ₁, A, Σ₂, B
-------------------      -----------------------
Γ₁, B, Γ₂, A |- Σ          Γ |- Σ₁, B, Σ₂, A

``` -->

## Ownership system

The idea is to remove contruction and factoring from available rules, thus removing the ability to duplicate variables. It sounds good on paper, but the problem is that liquid java is built on top of Java's typesystem. 

Solution is to add annotations to mimic such typesystem.

## Possible annotations

1. `Consumes` - forbids usage of object `o` after `foo` function call
```java
int bar(Object o){//Should be infered that `bar` also consumes `o`
    return foo(o);
}

int boo(Object o){
    Object o1 = o;
    int res = foo(o);
    int res1 = foo(o1);//should be infered that o and o1 are aliases
    return res + res1
}

int far(Object o){
    Object o1 = o.clone();
    int res = foo(o);
    int res1 = foo(o1);//should pass
    return res + res1;
}

int foo(@Consumes Object o){
    //...
}


```
The question is how to fight aliasing and how to infer that caller consumes argument if it calles consuming function underneath

2. `Consume` - consumes a variable from a callee persepctive

```java


int foo(Object o){//should be infered that `foo` consumes `o`
    @Consume("o")//Maybe `Dispose`?
    Object o1 = o; 
    return 1;
}

```


3. Partial consume - fields can be consumed independantly

```java
class A{
    public Object a;
    public int x;
    public Object b;
}

int foo(@Consumes Object x){
    //...
}

int bar(A y){//should typecheck and infer that `bar` consumes `a`
    return foo(y.a) + foo(y.b); 
}

```

4. Consumtion of aliased pointers

```java
int foo(Object left, Object right){
    //should somehow proof that 
    //left and right they are not aliases
    //....
}
```

5. Mutability without consumption

```java
int foo(@Mutates Object left, @Mutates Object right){
    //should proof that left and right are anailased, but
    //should not consume them
    //...
}
```



## Interaction with existing parts of Liquid Java

The simpliest way to implement this annotaitons is to introduce an uninterpreted function wich will signal when the value is consumed. It is possible to propagate it through function calls, but it is hard to handle aliasing.

## Random thoughts

* Is it true that I need to track only assignements and function calls?

* I feel like I do not care if pointer is mutated internally, I just care that it still is an alias with other pointers.

* I feel like it is possible to derive heap precondition and heap postcondition depending on ownership annotations

* The next question is "Is it worth to use smt solver for this?". If I am using it, then I need to encode disjointness of pointers in solver's API.

* Maybe I can use limited support of separation logic to encode some simple disjointness conditions?

```java

//without this annotation it is hard
//to encode pointer disjointness
@HeapPrecondition("left * right")
int foo(@Consumes Object left,
        @Consumes Object right){
    //...
}

@HeapPrecondition("left * right")
@HeapPostcondition("left * right")
int fooPrime(@Mutates Object left,
             @Mutates Object right){
    //...
}

int bar(){
    Object l = new Object();//heap: [l ⟶ -]
    Object r = new Object();//heap: [r ⟶ -]

    //by framerule, heap: [l ⟶ - * r ⟶ -]
    int res = 0;
    res += fooPrime(l, r); //OK
    res += foo(l, r);//OK
    
    //heap: emp

    return res;
}

//The greatest part is that it is completely possible to feed this to cvc5

```

* Will it work?

```java
@HeapPrecondition("left * right")
int foo(@Consumes Object left,
        @Consumes Object right){
    //...
}

int bar(){
    Object l = new Object();//heap: [l ⟶ -]
    Object r = new Object();//heap: [r ⟶ -]

    //by framerule, heap: [l ⟶ - * r ⟶ -]

    Object tmp = l;
    l = r;
    r = tmp;

    //l#1 == r && r#1 == tmp && tmp == l
    

    //can cvc5 proof this? No!
    // ((l ⟶ o1 * r ⟶ o2) &&
    //     ((l#1 == r && r#1 == tmp && tmp == l) 
    //  \/  (l#1 == r && r#1 == tmp && tmp == l))
    //) 
    // => exists o1', o2' : [l#1 ⟶ o1', r#1 ⟶ o2']

    int res = foo(l, r);

    return res;
}

```

```lisp
;without runtime condition, just swap
(set-logic QF_ALL)
(set-info :smt-lib-version 2.6)
(set-info :status unsat)

(set-option :produce-models true)
(set-option :produce-proofs true)
(set-option :produce-unsat-cores true)

(declare-sort Data 0)

(declare-heap (Int Data))

(declare-const left Int)
(declare-const right Int)

; ------- for verbosity

(assert (= (as sep.nil Int) 0))
(assert (>= left 0))
(assert (>= right 0))

; ------ precondition
(declare-const left_v Data)
(declare-const right_v Data)

(assert (sep
    (pto left left_v)
    (pto right right_v)
))

; ------ swap

(declare-const tmp Int)
(assert (= tmp left))

(declare-const left1 Int)
(declare-const right1 Int)

(assert (= left1 right))
(assert (= right1 tmp))

(declare-const left1_v Data)
(declare-const right1_v Data)

(assert  (not (sep 
    (pto left1 right_v) 
    (pto right1 left_v) 
)))
; ^ here is the problem, as we need to know the values
; which pointers should point to

(check-sat)
(get-model)
```

Unless! There is single value for the pointers.
Essentially all of them pointers are `void*`.
It makes it trivial to make annotations in form they are now.

```lisp
(set-logic QF_ALL)
(set-info :smt-lib-version 2.6)
(set-info :status unsat)

(set-option :produce-models true)
(set-option :produce-proofs true)
(set-option :produce-unsat-cores true)

(declare-sort Data 0)

(declare-heap (Int Data))

; single value for every pointer
(declare-const unit Data)

(declare-const left Int)
(declare-const right Int)



(assert (= (as sep.nil Int) 0))
(assert (>= left 0))
(assert (>= right 0))

; ------ precondition


(assert (sep
    (pto left unit)
    (pto right unit)
))

; ------ swap

(declare-const tmp Int)
(assert (= tmp left))

(declare-const left1 Int)
(declare-const right1 Int)

(assert (= left1 right))
(assert (= right1 tmp))

(assert  (not (sep 
    (pto left1 unit) 
    (pto right1 unit) 
)))

(check-sat)
(get-model)
```

The general form for the symbolyc heap entailement in SMT, provided one needs to check if  $\Sigma | \Pi$ entails $\Sigma' | \Pi'$

```lisp
(set-logic QF_ALL)
(set-info :smt-lib-version 2.6)

(declare-sort Loc 0)
(declare-sort Data 0)

(declare-const unit Data)

(declare-heap (Loc Data))

for v in FV(Σ∣Π and Σ'∣Π'): (declare-const v Loc)

; Σ∣Π
(assert (and 
    for b in Π: b    
))

(assert (sep
    for h in Σ: h
))

; Σ'∣Π'
(assert (not 
    (and 
        (and 
            for b' in Π': b'
        )
        (sep 
            for h' in Σ': h'
        )
    )
))

```


* Should streams be a test ground for these annotations?

* Example with mutation of a list while iterating it on it.

```
//copy should work

ys = xs

for (i, x) in enumerate(xs):
   ys.remove(x);//error!

it = xs.iterator();
for x in it:
  it.remove();
```

```

isList(l : Int, end : Int){
    if (l == 0){
        return True;
    }else{
        isList(l.next); 
    }
}

containsInList(n : Data, l : Int) : Bool = {
    if (l == 0){
        return False;
    }
    if (value(l) == n){
        return True;
    }
    if (isList(l.next)){
        return containsInList(n, l.next);
    }else{
        return False;
    }
}

```





