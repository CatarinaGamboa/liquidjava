import repair.regen.specification.Refinement;

public class SimpleRefinementTest {

	public static void main(String[] args) {
		int a = 1;
		
		
		@SuppressWarnings("unused")
		@Refinement("b > 2") 
		int b = a;
	}

}
