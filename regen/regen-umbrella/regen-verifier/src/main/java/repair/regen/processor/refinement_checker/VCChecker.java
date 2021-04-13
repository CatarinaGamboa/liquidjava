package repair.regen.processor.refinement_checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VCImplication;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import repair.regen.smt.GhostFunctionError;
import repair.regen.smt.NotFoundError;
import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import repair.regen.smt.TypeMismatchError;
import repair.regen.utils.ErrorPrinter;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;

public class VCChecker {
	private Context context;
	private List<RefinedVariable> pathVariables;
	Pattern thisPattern = Pattern.compile("#this_\\d+");
	Pattern instancePattern = Pattern.compile("^#(.+)_[0-9]+$");
	
	
	public VCChecker() {
		context = Context.getInstance();
		pathVariables = new Stack<>();
	}

	public void processSubtyping(Constraint expectedType, List<GhostState> list, String wild_var, String this_var, CtElement element) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		if(expectedType.isBooleanTrue())
			return;

		HashMap<String, String> map = new HashMap<String, String>();
		String[] s = {wild_var, this_var};
		Constraint premises = joinConstraints(expectedType, element, mainVars, lrv, map)
							.changeStatesToRefinements(list, s)
							.changeAliasToRefinement(context, element);
		Constraint et = expectedType
							.changeStatesToRefinements(list, s)
							.changeAliasToRefinement(context, element);
		
		System.out.println(premises.toString() + "\n"+et.toString());
		smtChecking(premises, et, element, map);
	}

	public boolean processSubtyping(Constraint type, Constraint expectedType, List<GhostState> list, String wild_var, String this_var,CtElement element) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		gatherVariables(type, lrv, mainVars);
		if(expectedType.isBooleanTrue() && type.isBooleanTrue())
			return true;


//		Constraint premises = joinConstraints(type, element, mainVars, lrv);
		HashMap<String, String> map = new HashMap<String, String>();
		String[] s = {wild_var, this_var};
		Constraint premises = joinConstraints(expectedType, element, mainVars, lrv, map);
		premises = Conjunction.createConjunction(premises, type)
							  .changeStatesToRefinements(list, s)
							  .changeAliasToRefinement(context, element);
		Constraint et = expectedType
							  .changeStatesToRefinements(list, s)
							  .changeAliasToRefinement(context, element);
		System.out.println(premises.toString() + "\n"+et.toString());
		return smtChecks(premises, et, element);
	}

	private Constraint joinConstraints(Constraint expectedType, CtElement element, List<RefinedVariable> mainVars, 
			List<RefinedVariable> vars, Map<String, String> map) {

		VCImplication firstSi = null;
		VCImplication lastSi = null;
		//Check
		for(RefinedVariable var:mainVars) {//join main refinements of mainVars
			addMap(var, map);			
			VCImplication si = new VCImplication(var.getName(), var.getType(), var.getMainRefinement());
			if(lastSi != null) {
				lastSi.setNext(si); lastSi = si;
			}
			if(firstSi == null) {
				firstSi = si; lastSi = si;
			}
			
		}

		for(RefinedVariable var:vars) {//join refinements of vars
			addMap(var, map);
			VCImplication si = new VCImplication(var.getName(), var.getType(), var.getRefinement());
			if(lastSi != null) {
				lastSi.setNext(si);	lastSi = si;
			}
			if(firstSi == null) {
				firstSi = si; lastSi = si;
			}
		}
		Constraint cSMT = new Predicate();
		if(firstSi != null && lastSi != null) {
			cSMT = firstSi.toConjunctions();
			lastSi.setNext(new VCImplication(expectedType));
			printVCs(firstSi.toString(), cSMT.toString(), expectedType);
		}

		return cSMT;
	}


	private void addMap(RefinedVariable var, Map<String, String> map) {
		if(var instanceof VariableInstance) {
			VariableInstance vi = (VariableInstance) var;
			if(vi.getParent().isPresent())
				map.put(vi.getName(), vi.getParent().get().getName());	
			else if(instancePattern.matcher(var.getName()).matches()){
				String result = var.getName().replaceAll("(_[0-9]+)$", "").replaceAll("^#", "");
				map.put(var.getName(), result);
			}
		}else if(thisPattern.matcher(var.getName()).matches())
				map.put(var.getName(), "this");
	}

	private void gatherVariables(Constraint expectedType, List<RefinedVariable> lrv, List<RefinedVariable> mainVars) {
		for(String s: expectedType.getVariableNames()) {
			if(context.hasVariable(s)) {
				RefinedVariable rv = context.getVariableByName(s);
				if(!mainVars.contains(rv))
					mainVars.add(rv);
				List<RefinedVariable> lm = getVariables(rv.getMainRefinement(), rv.getName());
				addAllDiferent(lrv, lm);
			}
		}

	}


	private void addAllDiferent(List<RefinedVariable> toExpand, List<RefinedVariable> from) {
		for(RefinedVariable rv:from) {
			if(!toExpand.contains(rv))
				toExpand.add(rv);
		}
	}

	private List<RefinedVariable> getVariables(Constraint c, String varName) {
		List<RefinedVariable> allVars = new ArrayList<>();
		getVariablesFromContext(c.getVariableNames(), allVars, varName);
		List<String> pathNames = pathVariables.stream()
				.map(a->a.getName())
				.collect(Collectors.toList());
		getVariablesFromContext(pathNames, allVars, "");


		return allVars;
	}

	private void getVariablesFromContext(List<String> lvars, List<RefinedVariable> allVars, 
			String notAdd) {
		for(String name: lvars) 
			if(!name.equals(notAdd) && context.hasVariable(name)) {
				RefinedVariable rv = context.getVariableByName(name);
				if(!allVars.contains(rv)) {
					allVars.add(rv);
					recAuxGetVars(rv, allVars);
				}
			}
	}

	private void recAuxGetVars(RefinedVariable var, List<RefinedVariable> newVars) {
		if(!context.hasVariable(var.getName()))
			return;
		Constraint c = var.getRefinement();
		String varName = var.getName();
		List<String> l = c.getVariableNames();
		for(String name:l) {
			if(!name.equals(varName) && context.hasVariable(name)) {
				RefinedVariable rv = context.getVariableByName(name);
				if(!newVars.contains(rv)) { 
					newVars.add(rv);
					recAuxGetVars(rv, newVars);
				}
			}
		}
	}


	public boolean smtChecks(Constraint cSMT, Constraint expectedType, CtElement element) {
		try {
			new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
		}catch (TypeCheckError e) { 
			return false;
		}catch(Exception e) {
			System.err.println("Unknown error:"+e.getMessage());
			e.printStackTrace();
			System.exit(7);
		}
		return true;
	}

	/**
	 * Checks the expectedType against the cSMT constraint.
	 * If the types do not check and error is sent and the program ends
	 * @param cSMT
	 * @param expectedType
	 * @param element
	 * @param map 
	 */
	private void smtChecking(Constraint cSMT, Constraint expectedType, CtElement element, HashMap<String, String> map) {
		//		printVCs("", cSMT.toString(), expectedType);
		String s = null;
		if(element instanceof CtInvocation) {
			CtInvocation ci = (CtInvocation) element;
			s = "Method invocation " + ci.getExecutable().toString() + " in:";
		}
		try {
			new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
			System.out.println("End smt checking");
		} catch (TypeCheckError e) {
			ErrorPrinter.printError(element, s, substituteByMap(expectedType, map), substituteByMap(cSMT, map));
		}catch (GhostFunctionError e) {
			ErrorPrinter.printErrorArgs(element, substituteByMap(expectedType, map), e.getMessage());
		}catch(TypeMismatchError e) {
			ErrorPrinter.printErrorTypeMismatch(element, substituteByMap(expectedType, map), e.getMessage());
		}catch(NotFoundError e) {
			ErrorPrinter.printNotFound(element, substituteByMap(cSMT, map), substituteByMap(expectedType, map), e.getMessage());
		}catch (Exception e) {
			System.err.println("Unknown error:"+e.getMessage());
			e.printStackTrace();
			System.exit(7);

		}
		//catch(NullPointerException e) {
		//	printErrorUnknownVariable(element, expectedType, correctRefinement);
		//}

	}


	/**
	 * Change variables in constraint by their value expression in the map
	 * @param c
	 * @param map
	 * @return
	 */
	private Constraint substituteByMap(Constraint c, HashMap<String, String> map) {
		Constraint c1 = c;
		for(String s: map.keySet())
			c1 = c1.substituteVariable(s, map.get(s));
		return c1;
	}

	public void addPathVariable(RefinedVariable rv) {
		pathVariables.add(rv);
	}

	public void removePathVariable(RefinedVariable rv) {
		pathVariables.remove(rv);
	}

	void removePathVariableThatIncludes(String otherVar) {
		List<RefinedVariable> toRemove = new ArrayList<>();
		for(RefinedVariable rv:pathVariables)
			if(rv.getRefinement().getVariableNames().contains(otherVar))
				toRemove.add(rv);

		for(RefinedVariable rv:toRemove) 
			pathVariables.remove(rv);
	}

	private void printVCs(String string, String stringSMT, Constraint expectedType) {
		System.out.println("\n----------------------------VC--------------------------------");
		System.out.println("VC:\n"+string);
		System.out.println("\nSMT subtyping:" + stringSMT + " <: " + expectedType.toString());
		System.out.println("--------------------------------------------------------------");

	}



}
