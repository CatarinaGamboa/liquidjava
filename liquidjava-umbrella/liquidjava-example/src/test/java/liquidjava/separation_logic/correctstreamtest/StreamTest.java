package liquidjava.separation_logic.correctstreamtest;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;
import liquidjava.specification.Refinement;

import java.util.stream.Stream;

@ExternalRefinementsFor("java.util.stream.Stream")
interface StreamRefinement<T>{
    @HeapPrecondition("this |-> sep.()")
    @HeapPostcondition("_ |-> sep.()")
    @Refinement("_ == this")
    Stream<T> filter(java.util.function.Predicate<? super T> predicate);

    @HeapPrecondition("this |-> sep.()")
    @HeapPostcondition("_ |-> sep.()")
    @Refinement("_ == this")
    Stream<T> map(java.util.function.Predicate<? super T> predicate);

    @HeapPrecondition("this |-> sep.()")
    @HeapPostcondition("sep.emp")
    void forEach(java.util.function.Consumer<? super T> action );

    @HeapPrecondition("sep.emp")
    @HeapPostcondition("_ |-> sep.()")
    static <T> Stream<T> of(T... values){return null;}
}

public class StreamTest {
    static void run(){
        Stream<Integer> st = Stream.of(1,2,3,4,5);

        Stream<Integer >st1 = st.filter(x -> x % 2 == 0);
        Stream<Integer >st2 = st1.map(x -> x*x);
        st2.forEach(System.out::println);
    }
}
