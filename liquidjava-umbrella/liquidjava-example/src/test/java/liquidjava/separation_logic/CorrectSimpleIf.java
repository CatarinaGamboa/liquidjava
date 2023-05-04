package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

public class CorrectSimpleIf {
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

    @HeapPrecondition("x |-> sep.() |* y |-> sep.()")
    @HeapPostcondition("x |-> sep.() |* y |-> sep.()")
    void swap(Object x, Object y){
        //???
    }

    void execution(){
        long x = System.currentTimeMillis() % 2;

        Object a = createObject();
        Object b = createObject();

        if (x == 0){
            swap(a, b);
        }else{
            System.out.println(String.valueOf(a) + b);
        }

        Object res = foo(a, b);
        System.out.println(res);
    }

}
