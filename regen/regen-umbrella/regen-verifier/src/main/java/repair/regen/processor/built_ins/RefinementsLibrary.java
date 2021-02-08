package repair.regen.processor.built_ins;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.RefinedFunction;
import spoon.reflect.factory.Factory;

public class RefinementsLibrary {
	private HashMap<String, String> map = new HashMap<>();
	private String WILD_VAR;
	
	private static final String path = "math_refinements.properties";

	public RefinementsLibrary(String wildvar) {
		WILD_VAR = wildvar;
		addMathMethods();
	}

	//	@Refinement("")
	//	int function(@Refinement("")int b)

	private void addMathMethods() {
		double pi = 3.141592653589793;
		//map.put("java.lang.Math.PI", WILD_VAR +" == "+pi);
		//map.put("java.lang.Math.E", WILD_VAR+" == 2.7182818284590452354");

//		String ref_abs = String.format("{true}->{(%s == arg0 || %s == -arg0) && %s > 0}", 
//				WILD_VAR, WILD_VAR, WILD_VAR); 
//		map.put("java.lang.Math.abs(int)", ref_abs);
//		map.put("java.lang.Math.abs(float)", ref_abs);
//		map.put("java.lang.Math.abs(double)", ref_abs);
//		map.put("java.lang.Math.abs(long)", ref_abs);

		//NAN special case
		map.put("java.lang.Math.acos(double)", 
				String.format("{true}->{%s >= 0.0 && %s <= "+pi+"}", WILD_VAR,WILD_VAR));

//		String addExact = String.format("{true}->{true}->{%s == (arg0 + arg1)}", WILD_VAR);
//		map.put("java.lang.Math.addExact(int,int)", addExact);
//		map.put("java.lang.Math.addExact(long,long)", addExact);

//		map.put("java.lang.Math.random()", "{_ > 0.0 && _ < 1.0}");
//		map.put("java.lang.Math.sqrt(double)", "{true}->{"+WILD_VAR+" > 0}");//TODO maybe improve

		double p2= pi/2;
		map.put("java.lang.Math.asin(double)", 
				String.format("{true}->{%s >= ("+(-p2)+") && %s <= ("+p2+")}", WILD_VAR, WILD_VAR));//NAN special case

		map.put("java.lang.Math.atan(double)", 
				String.format("{true}->{%s >= ("+(-p2)+") && %s <= ("+p2+")}", WILD_VAR, WILD_VAR));//NAN special case

		map.put("java.lang.Math.atan2(double,double)", 
				String.format("{true}->{true}->{%s >= ("+(-pi)+") && %s <= ("+pi+")}", WILD_VAR, WILD_VAR));//NAN special case


		//		map.put("java.lang.Math.copySign(float,float)", 
		//				String.format("{true}->{true}->"
		//						+ "{((%s == arg0) || (%s == -arg0)) && ((!(arg1 > 0) || (%s > 0)) && ((arg1 > 0) || (%s < 0)))}", 
		//						WILD_VAR, WILD_VAR, WILD_VAR, WILD_VAR));

		//if arg1 > 0 then _ > 0 else _ <= 0
		
//		map.put("java.lang.Math.copySign(float,float)", 
//				String.format("{true}->{true}->"
//						+"{((arg1 > 0) --> (%s > 0)) && (!(arg1 > 0)-->!(%s > 0)) && ((%s == arg0) || (%s == (-arg0)))}",
//						WILD_VAR, WILD_VAR, WILD_VAR, WILD_VAR));


//		map.put("java.lang.Math.decrementExact(int)", 
//				String.format("{true}->{%s == (arg0 - 1)}",	WILD_VAR));

//		map.put("java.lang.Math.decrementExact(long)", 
//				String.format("{true}->{%s == (arg0 - 1)}",	WILD_VAR));

//		map.put("java.lang.Math.incrementExact(int)", 
//				String.format("{true}->{%s == (arg0 + 1)}",	WILD_VAR));

//		map.put("java.lang.Math.incrementExact(long)", 
//				String.format("{true}->{%s == (arg0 + 1)}",	WILD_VAR));

//		map.put("java.lang.Math.max(int,int)", String.format("{true}->{true}->"
//				+ "{((arg0 > arg1)-->(%s == arg0)) && ((arg0 <= arg1)-->(%s == arg1))}",
//				WILD_VAR, WILD_VAR));
//		map.put("java.lang.Math.min(int,int)", String.format("{true}->{true}->"
//				+ "{((arg0 < arg1)-->(%s == arg0)) && ((arg0 >= arg1)-->(%s == arg1))}",
//				WILD_VAR, WILD_VAR));


//		map.put("java.lang.Math.multiplyExact(int,int)", 
//				String.format("{true}->{true}->{%s == (arg0 * arg1)}",	WILD_VAR));

//		map.put("java.lang.Math.negateExact(int)", 
//				String.format("{true}->{%s == (-arg0)}",	WILD_VAR));
//
//		map.put("java.lang.Math.subtractExact(int,int)", 
//				String.format("{true}->{true}->{%s == (arg0 - arg1)}",	WILD_VAR));


		//		power sim
		//		if then else para z3 - ite ? :
		//		(_ == arg0 || _ == -arg0) && (if arg1 > 0 then _ > 0 else _ < 0)

	}

	
	public Optional<Constraint> getFieldRefinement(String r) {
		try (InputStream input = new FileInputStream(path)) {
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			String ref = prop.getProperty(r);
			return Optional.of(new Predicate(ref));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<RefinedFunction> getRefinement(String met, Method m, Factory factory) {
		String path = "math_refinements.properties";
		try (InputStream input = new FileInputStream(path)) {
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			String r = prop.getProperty(met);
			RefinedFunction rf = refinementToFunction(r, m, factory);
			return Optional.of(rf);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return Optional.empty();
	}
	

	
	private RefinedFunction refinementToFunction(String r, Method m, Factory factory) {
		RefinedFunction rf = new RefinedFunction();
		rf.setName(m.getName());
		rf.setType(factory.createCtTypeReference(m.getReturnType()));
		setReturnRefinement(rf, r);
		setParametersRefinements(rf, r, m, factory);
		return rf;
	}

	private void setReturnRefinement(RefinedFunction rf, String r) {
		String regex = "@Refinement\\(\".*\"\\)java";
		Pattern pattern = Pattern.compile(regex);
		Matcher mat = pattern.matcher(r);
		while (mat.find()) {
		    String s = mat.group(0);
		    String[] sep = s.split("\"");
		    rf.setRefReturn(new Predicate(sep[1]));
		}
	}

	
	private void setParametersRefinements(RefinedFunction rf, String r, Method m, Factory factory) {
		Parameter[] psM = m.getParameters();
		int i = 0;
		String regexP = "@Refinement\\(\"[^,(java)]*\"\\)";
		Pattern pattern = Pattern.compile(regexP);
		Matcher mat = pattern.matcher(r);
		while (mat.find()) {
		    String s = mat.group(0);
		    String[] sep = s.split("\"");
		    String ref = sep[1];
		    Constraint c = new Predicate(ref);
		    c = c.substituteVariable("_", psM[i].getName());
		    rf.addArgRefinements(psM[i].getName(), 
		    		factory.createCtTypeReference(psM[i].getType()), c);
		    i++;
		}
		while (i < psM.length) {
			rf.addArgRefinements(psM[i].getName(),factory.createCtTypeReference(psM[i].getType()), 
					new Predicate());
			i++;
		}	
	}

	public boolean hasRefinement(String met) {
		return map.containsKey(met);
	}



}
