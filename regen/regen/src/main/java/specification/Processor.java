package specification;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Processor {

    public List<String> getRefinement(Object object) throws Exception {  
        
        List<String> result = new ArrayList<>();
        
        Class<?> clazz = object.getClass();
        
        for (Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);

            if (method.isAnnotationPresent(Refinement.class))
                result.add(method.getAnnotation(Refinement.class).value().toString());
        }       
        
        return result;
    }
    
}
