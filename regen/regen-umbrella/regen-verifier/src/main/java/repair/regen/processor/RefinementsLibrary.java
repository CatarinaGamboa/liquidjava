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
		double pi = 3.141592653589793;
		map.put("java.lang.Math.PI", WILD_VAR +" == "+pi);
		map.put("java.lang.Math.E", WILD_VAR+" == 2.7182818284590452354");
		
		String ref_abs = String.format("{true}->{(%s == arg0 || %s == -arg0) && %s > 0}", 
				WILD_VAR, WILD_VAR, WILD_VAR); 
		map.put("java.lang.Math.abs(int)", ref_abs);
		map.put("java.lang.Math.abs(float)", ref_abs);
		map.put("java.lang.Math.abs(double)", ref_abs);
		map.put("java.lang.Math.abs(long)", ref_abs);
		
		//NAN special case
		map.put("java.lang.Math.acos(double)", 
				String.format("{true}->{%s >= 0.0 && %s <= "+pi+"}", WILD_VAR,WILD_VAR));
		
		String addExact = String.format("{true}->{true}->{%s == (arg0 + arg1)}", WILD_VAR);
		map.put("java.lang.Math.addExact(int,int)", addExact);
		map.put("java.lang.Math.addExact(long,long)", addExact);
		
		map.put("java.lang.Math.random()", "{\\v > 0.0 && \\v < 1.0}");
		map.put("java.lang.Math.sqrt(double)", "{true}->{"+WILD_VAR+" > 0}");//TODO maybe improve
		
		double p2= pi/2;
		map.put("java.lang.Math.asin(double)", 
				String.format("{true}->{%s >= ("+(-p2)+") && %s <= ("+p2+")}", WILD_VAR, WILD_VAR));//NAN special case
		
		map.put("java.lang.Math.atan(double)", 
				String.format("{true}->{%s >= ("+(-p2)+") && %s <= ("+p2+")}", WILD_VAR, WILD_VAR));//NAN special case
		
		map.put("java.lang.Math.atan2(double,double)", 
				String.format("{true}->{true}->{%s >= ("+(-pi)+") && %s <= ("+pi+")}", WILD_VAR, WILD_VAR));//NAN special case
		
		//square root e power - makes sense to create this functions in specificationn language?
		//java.lang.Math.cbrt(double) only makes sense if the power is specified
		//java.lang.Math.ceil(double) makes sense to add cast in the a specification?
		
		//implicacao
		//power sim
		//if then else para z3 - ite ? :
		//(\\v == arg0 || \\v == -arg0) && (if arg1 > 0 then \\v > 0 else \\v < 0)
//		map.put("java.lang.Math.copySign(float,float)", 
//				String.format("{true}->{true}->"
//						+ "{((%s == arg0) || (%s == -arg0)) && ((!(arg1 > 0) || (%s > 0)) && ((arg1 > 0) || (%s < 0)))}", 
//						WILD_VAR, WILD_VAR, WILD_VAR, WILD_VAR));
		
		map.put("java.lang.Math.copySign(float,float)", 
				String.format("{true}->{true}->"
						+ "{(((%s == arg0) || (%s == (-arg0))) && (((arg1 > 0) --> (%s > 0)) && ((arg1 <= 0) --> (%s < 0))))}", 
						WILD_VAR, WILD_VAR, WILD_VAR, WILD_VAR));
		
		
		
		map.put("java.lang.Math.decrementExact(int)", 
				String.format("{true}->{%s == (arg0 - 1)}",	WILD_VAR));
		
		map.put("java.lang.Math.decrementExact(long)", 
				String.format("{true}->{%s == (arg0 - 1)}",	WILD_VAR));
		
		map.put("java.lang.Math.incrementExact(int)", 
				String.format("{true}->{%s == (arg0 + 1)}",	WILD_VAR));
		
		map.put("java.lang.Math.incrementExact(long)", 
				String.format("{true}->{%s == (arg0 + 1)}",	WILD_VAR));
		
		
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
