package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

public class ErrorHeapShrinkAssign {
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
        Object b = createObject();
        //a == #co5 && b == #co3 && [#co5 -> (), #co3 -> ()]
        a = b;
        //a#4 == a && b#2 == b && a#4 == b#2 && a == #co5 && b == #co3 && [#co5 -> (), #co3 -> ()]
        //a#4 == a && b#2 == b && a#4 == b#2 && a == #co5 && b == #co3 && [#co5 -> (), true] && [] =>  [] * #co5 -> ()

        int res = foo(a, b);
        System.out.println(res);
    }
}
