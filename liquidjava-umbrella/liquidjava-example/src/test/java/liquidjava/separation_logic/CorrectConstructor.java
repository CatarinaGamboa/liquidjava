package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

public class CorrectConstructor {

    static class MyObject {
        MyObject(){

        }
    }
    @HeapPrecondition("x |-> sep.()")
    static void f(MyObject x){
        System.out.println(x);
    }

    public static void main(String[] args) {
        MyObject x = new MyObject();
        f(x);
    }
}
