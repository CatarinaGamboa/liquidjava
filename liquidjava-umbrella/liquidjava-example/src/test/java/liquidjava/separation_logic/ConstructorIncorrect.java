package liquidjava.separation_logic;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

class ConstructorIncorrect{
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
        f(x);//should fail here
    }
}