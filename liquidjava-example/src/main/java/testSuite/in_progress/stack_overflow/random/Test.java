package testSuite.in_progress.stack_overflow.random;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Test {

    public static String extractIndividualInstance(ArrayList<String> instances) {
        Random generator = new Random(new Date().getTime());
        int random = generator.nextInt(instances.size() - 1); // if size is 0 throws exception
    
        String singleInstance = instances.get(random);
        instances.remove(random);
    
        return singleInstance;
    }
    
}
