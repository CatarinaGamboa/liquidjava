package testSuite.classes.stack_overflow.iterator;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
/**
 * Examples related to Iterator typestate refinements.
 * Ideas from: https://stackoverflow.com/questions/22361194/iterator-remove-illegalstateexception
 */
@SuppressWarnings("unused")
public class TestIterator {
    
    public static void main(String[] args) {

        // Define and initialize queues
        Queue<Integer> qev1, qev2, qcv1, qcv2;
        qev1 = new LinkedList<>();
        qev2 = new LinkedList<>();
        qcv1 = new LinkedList<>();
        qcv2 = new LinkedList<>();

        qev1.add(100);
        qev1.add(200);
        qev1.add(300);
        qev1.add(300);
        qev1.add(300);
        qev1.add(300);

        // Get an iterator for the queue
        Iterator<Integer> iterator = qev1.iterator();

        try {
            iterator.remove(); // Error no call to next before remove
        }
        catch(UnsupportedOperationException e) {
            System.out.println("Calling Iterator.remove() and throwing exception.");
        }

    }

    public static void testWrong(String[] args) {

        // Define and initialize queues
        Queue<Integer> qev1, qev2, qcv1, qcv2;
        qev1 = new LinkedList<>();
        qev2 = new LinkedList<>();
        qcv1 = new LinkedList<>();
        qcv2 = new LinkedList<>();

        qev1.add(100);
        qev1.add(200);
        qev1.add(300);
        qev1.add(300);
        qev1.add(300);
        qev1.add(300);

        // Get an iterator for the queue
        Iterator<Integer> iterator = qev1.iterator();

        try {
            iterator.hasNext();
            iterator.next();
            iterator.hasNext();
            iterator.next();
            iterator.remove(); // No error
            iterator.remove(); // Error: needs to have 
        }
        catch(UnsupportedOperationException e) {
            System.out.println("Calling Iterator.remove() and throwing exception.");
        }

    }
}
