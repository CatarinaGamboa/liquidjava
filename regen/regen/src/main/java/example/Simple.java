package example;

import repair.regen.specification.Refinement;

@Refinement("super.x > 10")
public class Simple extends ToSimple {
    
    @Refinement("z > 0 && z > super.x")
    private int z;
    
    @Refinement("\\return == x + 1")
    public static int function1(@Refinement("x > 0") int x) {
        
        @Refinement("y > 0") 
        int y = x + 1;
        
        return y;
    }
}
