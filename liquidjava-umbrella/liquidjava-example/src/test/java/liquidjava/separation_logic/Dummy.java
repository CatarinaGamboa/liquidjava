package liquidjava.separation_logic;

import liquidjava.specification.Refinement;

public class Dummy {
    Object x;
    Object y;

    int foo(Object x,
            @Refinement("x |-> sep.() |* y |-> sep.()")
            Object y){
        return 0;
    }
    //true && sep.emp && !(x -> sep.())
    @Refinement("_ |-> sep.()")
    static Object createObject(){
        long x = System.currentTimeMillis() % 100;
        return Long.toString(x);
    }

    @Refinement("sep.emp")
    int bar(){
        return 0;
    }

    void execution(){
        @Refinement("_ > 0")
        int a = bar();

        this.x = createObject();
        this.y = createObject();

        @Refinement("_ > 0")
        int b = foo(this.x, this.y);
    }
}
