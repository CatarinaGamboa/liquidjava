package repair.regen.processor.refinement_checker;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import repair.regen.errors.ErrorEmitter;
import repair.regen.errors.ErrorHandler;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VCImplication;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.context.PlacementInCode;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.smt.GhostFunctionError;
import repair.regen.smt.NotFoundError;
import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import repair.regen.smt.TypeMismatchError;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

public class VCChecker {
	private Context context;
	private List<RefinedVariable> pathVariables;
	private ErrorEmitter errorEmitter;
	Pattern thisPattern = Pattern.compile("#this_\\d+");
	Pattern instancePattern = Pattern.compile("^#(.+)_[0-9]+$");


	public VCChecker(ErrorEmitter errorEmitter) {
		context = Context.getInstance();
		pathVariables = new Stack<>();
		this.errorEmitter = errorEmitter;
	}

	public void processSubtyping(Constraint expectedType, List<GhostState> list, String wild_var, 
			String this_var, CtElement element, Factory f) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		if(expectedType.isBooleanTrue())
			return;

		HashMap<String, PlacementInCode> map = new HashMap<>();
		String[] s = {wild_var, this_var};
		Constraint premisesBeforeChange = joinConstraints(expectedType, element, mainVars, lrv, map)
				.toConjunctions();
		Constraint premises = new Predicate();
		Constraint et = new Predicate();
		try {
			premises = premisesBeforeChange 
					.changeStatesToRefinements(list, s, errorEmitter)
					.changeAliasToRefinement(context, element, f);

			et = expectedType
					.changeStatesToRefinements(list, s, errorEmitter)
					.changeAliasToRefinement(context, element, f);
		} catch (Exception e1) {
			printError(premises, expectedType, element, map, e1.getMessage());
			return;
		}

//		System.out.println(premises.toString() + "\n"+et.toString());
		try {
			smtChecking(premises, et, element);
		} catch (Exception e) {
			//To emit the message we use the constraints before the alias and state change
			System.out.println();
			printError(e, premisesBeforeChange, expectedType, element, map);
		}
	}

	public void processSubtyping(Constraint type, Constraint expectedType, List<GhostState> list, 
			String wild_var, String this_var, CtElement element, String string, Factory f) {
		boolean b = canProcessSubtyping(type, expectedType, list, wild_var, this_var, element, f);
		if(!b)
			printSubtypingError(element, expectedType, type, string);
	}

	public boolean canProcessSubtyping(Constraint type, Constraint expectedType, List<GhostState> list, 
			String wild_var, String this_var,CtElement element, Factory f) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		gatherVariables(type, lrv, mainVars);
		if(expectedType.isBooleanTrue() && type.isBooleanTrue())
			return true;


		//		Constraint premises = joinConstraints(type, element, mainVars, lrv);
		HashMap<String, PlacementInCode> map = new HashMap<>();
		String[] s = {wild_var, this_var};

		Constraint premises = new Predicate(); 
		Constraint et = new Predicate();
		try {
			premises = joinConstraints(expectedType, element, mainVars, lrv, map).toConjunctions();
			premises = Conjunction.createConjunction(premises, type)
					.changeStatesToRefinements(list, s, errorEmitter)
					.changeAliasToRefinement(context, element, f);
			et = expectedType
					.changeStatesToRefinements(list, s, errorEmitter)
					.changeAliasToRefinement(context, element, f);
		} catch (Exception e) {
			return false;
//			printError(premises, expectedType, element, map, e.getMessage());
		}

		System.out.println(premises.toString() + "\n"+et.toString());
		return smtChecks(premises, et, element);
	}

	private /*Constraint*/VCImplication joinConstraints(Constraint expectedType, CtElement element, List<RefinedVariable> mainVars, 
			List<RefinedVariable> vars, HashMap<String, PlacementInCode> map) {

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
		VCImplication cSMT = new VCImplication(new Predicate());
		if(firstSi != null && lastSi != null) {
			cSMT = firstSi.clone();
			lastSi.setNext(new VCImplication(expectedType));
			printVCs(firstSi.toString(), cSMT.toConjunctions().toString(), expectedType);
		}

		return cSMT;//firstSi != null ? firstSi : new VCImplication(new Predicate());
	}

	private void addMap(RefinedVariable var, HashMap<String, PlacementInCode> map) {
		map.put(var.getName(), var.getPlacementInCode());
//		System.out.println();
//		if(var instanceof VariableInstance) {
//			VariableInstance vi = (VariableInstance) var;
//			if(vi.getParent().isPresent())
//				map.put(vi.getName(), vi.getParent().get().getName());	
//			else if(instancePattern.matcher(var.getName()).matches()){
//				String result = var.getName().replaceAll("(_[0-9]+)$", "").replaceAll("^#", "");
//				map.put(var.getName(), result);
//			}
//		}else if(thisPattern.matcher(var.getName()).matches())
//			map.put(var.getName(), "this");
	}

	private void gatherVariables(Constraint expectedType, List<RefinedVariable> lrv, List<RefinedVariable> mainVars) {
		for(String s: expectedType.getVariableNames()) {
			if(context.hasVariable(s)) {
				RefinedVariable rv = context.getVariableByName(s);
				if(!mainVars.contains(rv) && !lrv.contains(rv))
					mainVars.add(rv);
				List<RefinedVariable> lm = getVariables(rv.getMainRefinement(), rv.getName());
				addAllDiferent(lrv, lm,  mainVars);
			}
		}
	}

	private void addAllDiferent(List<RefinedVariable> toExpand, List<RefinedVariable> from, List<RefinedVariable> remove) {
		for(RefinedVariable rv:from) {
			if(!toExpand.contains(rv) && !remove.contains(rv))
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


	public boolean smtChecks(Constraint cSMT, Constraint expectedType, CtElement elem) {
		try {
			new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
		}
		catch (TypeCheckError e) { 
			return false;
		}catch(Exception e) {
//			System.err.println("Unknown error:"+e.getMessage());
//			e.printStackTrace();
//			System.exit(7);
//			fail();
			errorEmitter.addError("Unknown Error", e.getMessage(), elem.getPosition(), 7);
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
	 * @throws Exception 
	 * @throws GhostFunctionError 
	 * @throws TypeCheckError 
	 */
	private void smtChecking(Constraint cSMT, Constraint expectedType, CtElement element) throws TypeCheckError, GhostFunctionError, Exception {
		new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
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
		System.out.println("\n----------------------------VC--------------------------------\n");
		System.out.println(string);
		System.out.println("\nSMT subtyping:" + stringSMT + " <: " + expectedType.toString());
		System.out.println("--------------------------------------------------------------");

	}
	
	
	//Print Errors---------------------------------------------------------------------------------------------------
	
	private HashMap<String, PlacementInCode> createMap(CtElement element, Constraint expectedType){
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		HashMap<String, PlacementInCode> map = new HashMap<>();
		joinConstraints(expectedType, element, mainVars, lrv, map);
		return map;
	}
	
	protected void printSubtypingError(CtElement element, Constraint expectedType, 
			Constraint foundType, String customeMsg) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		gatherVariables(foundType, lrv, mainVars);
		HashMap<String, PlacementInCode> map = new HashMap<>();
		Constraint premises = joinConstraints(expectedType, element, mainVars, lrv, map).toConjunctions();
		printError(premises, expectedType, element, map, customeMsg);
	}

	public void printSameStateError(CtElement element, Constraint expectedType, String klass) {
		HashMap<String, PlacementInCode> map = createMap(element, expectedType);
		ErrorHandler.printSameStateSetError(element, expectedType, klass, map, errorEmitter);	
	}


	private void printError(Exception e, Constraint premisesBeforeChange, Constraint expectedType, CtElement element,
			HashMap<String, PlacementInCode> map) {
		String s = null;
		if(element instanceof CtInvocation) {
			CtInvocation ci = (CtInvocation) element;
			String totalS = ci.getExecutable().toString();
			if(ci.getTarget() != null) {
				int targetL = ci.getTarget().toString().length();
				totalS = ci.toString().substring(targetL+1);
			}
			s = "Method invocation " + totalS + " in:";
			System.out.println();
		}

		Constraint etMessageReady =  expectedType;//substituteByMap(expectedType, map);
		Constraint cSMTMessageReady = premisesBeforeChange;//substituteByMap(premisesBeforeChange, map);
		if ( e instanceof TypeCheckError) {
			ErrorHandler.printError(element, s, etMessageReady, cSMTMessageReady, map, errorEmitter);
		}else if(e instanceof GhostFunctionError) {
			ErrorHandler.printErrorArgs(element, etMessageReady, e.getMessage(), map, errorEmitter);
		}else if(e instanceof TypeMismatchError) {
			ErrorHandler.printErrorTypeMismatch(element, etMessageReady, e.getMessage(), map, errorEmitter);
		}else if(e instanceof NotFoundError) {
			ErrorHandler.printNotFound(element, cSMTMessageReady, etMessageReady, e.getMessage(), map, errorEmitter);
		}else {
			ErrorHandler.printCostumeError(element, e.getMessage(), errorEmitter);
//			System.err.println("Unknown error:"+e.getMessage());
//			e.printStackTrace();
//			System.exit(7);
		}
	}

	private void printError(Constraint premises, Constraint expectedType, CtElement element,
			HashMap<String, PlacementInCode> map, String s) {
		Constraint etMessageReady =  expectedType;//substituteByMap(expectedType, map);
		Constraint cSMTMessageReady = premises;//substituteByMap(premises, map);
		ErrorHandler.printError(element, s, etMessageReady, cSMTMessageReady, map, errorEmitter);
	}

	public void printStateMismatchError(CtElement element, String method, Constraint c, String states) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(c, lrv, mainVars);
		HashMap<String, PlacementInCode> map = new HashMap<>();
		VCImplication constraintForErrorMsg = joinConstraints(c, element, mainVars, lrv, map);
//		HashMap<String, PlacementInCode> map = createMap(element, c);
		ErrorHandler.printStateMismatch(element, method, constraintForErrorMsg, states, map, errorEmitter);
	}



}
