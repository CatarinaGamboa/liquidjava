package repair.regen.processor;

import java.util.HashMap;
import java.util.Optional;

import org.eclipse.jdt.internal.core.nd.AbstractTypeFactory;

import repair.regen.specification.Refinement;

public class RefinementsLibrary {
	private HashMap<String, String> map = new HashMap<>();
	
	public RefinementsLibrary() {
		addMathMethods();
	}
	
//	@Refinement("")
//	int function(@Refinement("")int b)

	private void addMathMethods() {
		String ref_abs = "{true}->{(\\v == arg0 || \\v == -arg0) && \\v > 0}"; 
		map.put("java.lang.Math.abs(int)", ref_abs);
		map.put("java.lang.Math.abs(float)", ref_abs);
		map.put("java.lang.Math.abs(double)", ref_abs);
		map.put("java.lang.Math.abs(long)", ref_abs);
		
		
		
		map.put("java.lang.Math.random()", 
				"{\\v > 0.0 && \\v < 1.0}");
		
		map.put("java.lang.Math.sqrt(double)", 
				"{true}->{\\v > 0}");//TODO maybe improve
		
		

//		@Refinement(“\\v >= 0”)
//		Sqrt( @Refinement(“a > 0”) double a)
		
	}
	public Optional<String> getRefinement(String met) {
		if(map.containsKey(met))
			return Optional.of(map.get(met));
		return Optional.empty();
	}
	
	public boolean hasRefinement(String met) {
		return map.containsKey(met);
	}
	
	

}
