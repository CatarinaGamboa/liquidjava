package liquidjava.separation_logic;

import liquidjava.specification.Refinement;

public class SimpleBaseline {
    int foo(@Refinement("x > 0")int x,
            @Refinement("x > 0")int y){
        return 0;
    }

    @Refinement("_ > 0")
    static int createObject(){
        return 1;
    }

    void execution(){
        int a = createObject();
        int b = createObject();

        int res = foo(a, b);
        System.out.println(res);
    }
}
