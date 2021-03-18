package repair.regen.processor.refinement_checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.smt.GhostFunctionError;
import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import repair.regen.smt.TypeMismatchError;
import spoon.reflect.declaration.CtElement;

public class VCChecker {
	private Context context;
	private List<RefinedVariable> pathVariables;

	public VCChecker() {
		context = Context.getInstance();
		pathVariables = new Stack<>();
	}

	public void processSubtyping(Constraint expectedType, CtElement element) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		if(expectedType.isBooleanTrue())
			return;

		Constraint premises = joinConstraints(expectedType, element, mainVars, lrv);
		smtChecking(premises, expectedType, element);
	}

	public boolean processSubtyping(Constraint type, Constraint expectedType, CtElement element) {
		List<RefinedVariable> lrv = new ArrayList<>(),  mainVars = new ArrayList<>();
		gatherVariables(expectedType, lrv, mainVars);
		gatherVariables(type, lrv, mainVars);
		if(expectedType.isBooleanTrue() && type.isBooleanTrue())
			return true;


//		Constraint premises = joinConstraints(type, element, mainVars, lrv);
		Constraint premises = joinConstraints(expectedType, element, mainVars, lrv);
		premises = Conjunction.createConjunction(premises, type);//TODO add to print
//		premises = Conjunction.createConjunction(premises, joinConstraints(type, element, mainVars, lrv));
		return smtChecks(premises, expectedType, element);
	}

	private Constraint joinConstraints(Constraint expectedType, CtElement element, List<RefinedVariable> mainVars, 
			List<RefinedVariable> vars) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbSMT = new StringBuilder();

		//Check
		Constraint cSMT = new Predicate();
		for(RefinedVariable var:mainVars) {//join main refinements of mainVars
			cSMT = Conjunction.createConjunction(cSMT, var.getMainRefinement());
			String ref = var.getMainRefinement().toString();

			//imprimir
			sb.append("forall "+var.getName()+":"+ref+" -> \n");
			sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
		}

		for(RefinedVariable var:vars) {//join refinements of vars
			cSMT = Conjunction.createConjunction(cSMT, var.getRefinement());
			String ref = var.getRefinement().toString();

			//imprimir
			sb.append("forall "+var.getName()+":"+ref+" -> \n");
			sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
		}
		sb.append(expectedType);
		printVCs(sb.toString(), sbSMT.toString(), expectedType);
		return cSMT;
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
		}catch (Exception e) { 
			return false;
		}
		return true;
	}

	/**
	 * Checks the expectedType against the cSMT constraint.
	 * If the types do not check and error is sent and the program ends
	 * @param cSMT
	 * @param expectedType
	 * @param element
	 */
	private void smtChecking(Constraint cSMT, Constraint expectedType, CtElement element) {
		//		printVCs("", cSMT.toString(), expectedType);
		try {
			new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
			System.out.println("End smt checking");
		} catch (TypeCheckError e) {
			ErrorPrinter.printError(element, expectedType, cSMT);

		}catch (GhostFunctionError e) {
			ErrorPrinter.printErrorArgs(element, expectedType, e.getMessage());
		}catch(TypeMismatchError e) {
			ErrorPrinter.printErrorTypeMismatch(element, expectedType, e.getMessage());
		}catch (Exception e) {
			System.err.println("Unknown error:"+e.getMessage());
			e.printStackTrace();
			System.exit(7);

		}
		//catch(NullPointerException e) {
		//	printErrorUnknownVariable(element, expectedType, correctRefinement);
		//}

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
