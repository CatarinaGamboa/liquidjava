package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

public class ErrorSimpleIf {
    @HeapPrecondition("sep.emp")
    @HeapPostcondition("_ |-> sep.()")
    static Object createObject(){
        long x = System.currentTimeMillis() % 100;
        return Long.toString(x);
    }

    @HeapPrecondition("x |-> sep.() |* y |-> sep.()")
    @HeapPostcondition("sep.emp")
    int foo(Object x,
            Object y){
        return 0;
    }

    void execution(){
        long x = System.currentTimeMillis() % 2;

        Object a = createObject();
        Object b = createObject();

        if (x == 0){
            // a * b | a, b
            Object tmp = a;
            // tmp == a && a * b | a, b, tmp
            a = b;
            // tmp == a && b
            b = tmp;
            // tmp == b && b
        }else{
            System.out.println(String.valueOf(a) + b);
        }

        Object res = foo(a, b);
        System.out.println(res);
    }

}
