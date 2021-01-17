package repair.regen.processor;

import java.util.HashMap;
import java.util.Optional;

import org.eclipse.jdt.internal.core.nd.AbstractTypeFactory;

import repair.regen.specification.Refinement;

public class RefinementsLibrary {
	private HashMap<String, String> map = new HashMap<>();
	private String WILD_VAR;
	
	public RefinementsLibrary(String wildvar) {
		WILD_VAR = wildvar;
		addMathMethods();
	}
	
//	@Refinement("")
//	int function(@Refinement("")int b)

	private void addMathMethods() {
		String ref_abs = String.format("{true}->{(%s == arg0 || %s == -arg0) && %s > 0}", WILD_VAR, WILD_VAR, WILD_VAR); 
		map.put("java.lang.Math.abs(int)", ref_abs);
		map.put("java.lang.Math.abs(float)", ref_abs);
		map.put("java.lang.Math.abs(double)", ref_abs);
		map.put("java.lang.Math.abs(long)", ref_abs);
		
		map.put("java.lang.Math.PI", WILD_VAR +" == 3.141592653589793");
		map.put("java.lang.Math.E", WILD_VAR+" == 2.7182818284590452354");
		
		
		
		map.put("java.lang.Math.random()", 
				"{\\v > 0.0 && \\v < 1.0}");
		
		map.put("java.lang.Math.sqrt(double)", 
				"{true}->{"+WILD_VAR+" > 0}");//TODO maybe improve
		
		

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
