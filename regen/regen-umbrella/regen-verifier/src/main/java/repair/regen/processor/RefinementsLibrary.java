package repair.regen.processor;

import java.util.HashMap;
import java.util.Optional;

import org.eclipse.jdt.internal.core.nd.AbstractTypeFactory;

public class RefinementsLibrary {
	private HashMap<String, String> map = new HashMap<>();
	
	public RefinementsLibrary() {
		addMathMethods();
	}

	private void addMathMethods() {
		map.put("java.lang.Math.abs(int)", 
			"{true}->{\\v > 0}");//TODO maybe improve
		
		map.put("java.lang.Math.random()", 
				"{\\v > 0}");//TODO maybe improve
		
		
//		@Refinement(“\\v >= 0”)
//		random()
//
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
