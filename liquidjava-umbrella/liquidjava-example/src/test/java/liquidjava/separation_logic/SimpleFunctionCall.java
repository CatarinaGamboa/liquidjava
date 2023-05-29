package liquidjava.separation_logic;

public class SimpleFunctionCall {

    int foo(Object x,
            Object y){
        return 0;
    }

    static Object createObject(){
        long x = System.currentTimeMillis() % 100;
        return Long.toString(x);
    }

    void execution(){
        Object a = createObject();
        Object b = createObject();

        int res = foo(a, b);
        System.out.println(res);
    }
}
