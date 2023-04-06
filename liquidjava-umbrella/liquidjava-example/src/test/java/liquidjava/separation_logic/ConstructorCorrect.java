package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

public class ConstructorCorrect {

    static class MyObject {
        @HeapPostcondition("_ -> ?")
        MyObject(){
        }
    }
    @HeapPrecondition("x |-> ?")
    static void f(MyObject x){
    }

    public static void main(String[] args) {
        MyObject x = new MyObject();
        f(x);
    }
}
