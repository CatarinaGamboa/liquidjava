package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

public class ErrorSimpleFunctionCall {
    @HeapPrecondition("x |-> sep.() |* y |-> sep.()")
    @HeapPostcondition("sep.emp")
    int foo(Object x,
            Object y){
        return 0;
    }
    //true && sep.emp && !(x -> sep.())
    @HeapPrecondition("sep.emp")
    @HeapPostcondition("_ |-> sep.()")
    static Object createObject(){
        long x = System.currentTimeMillis() % 100;
        return Long.toString(x);
    }

    void execution(){
        Object a = createObject();

        int res = foo(a, a);
        System.out.println(res);
    }
}
