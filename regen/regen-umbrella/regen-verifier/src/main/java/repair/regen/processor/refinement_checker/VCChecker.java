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



	private void process(Constraint expectedType, CtElement element, List<RefinedVariable> mainVars, 
			List<RefinedVariable> vars) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbSMT = new StringBuilder();

		//Check
		Constraint cSMT = new Predicate();
		for(RefinedVariable var:mainVars) {
			cSMT = Conjunction.createConjunction(cSMT, var.getMainRefinement());
			String ref = var.getMainRefinement().toString();

			//imprimir
			sb.append("forall "+var.getName()+":"+ref+" -> \n");
			sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
		}

		for(RefinedVariable var:vars) {
			cSMT = Conjunction.createConjunction(cSMT, var.getRefinement());
			String ref = var.getRefinement().toString();

			//imprimir
			sb.append("forall "+var.getName()+":"+ref+" -> \n");
			sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
		}
		sb.append(expectedType);
		printVCs(sb.toString(), sbSMT.toString(), expectedType);

		//check type
		smtChecking(cSMT, expectedType, element);
	}


	public void processSubtyping(Constraint expectedType, CtElement element) {
		List<RefinedVariable> lrv = new ArrayList<>();
		List<RefinedVariable> mainVars = new ArrayList<>();
		for(String s: expectedType.getVariableNames()) {
			if(context.hasVariable(s)) {
				RefinedVariable rv = context.getVariableByName(s);
				mainVars.add(rv);
				List<RefinedVariable> lm = getVariables(rv.getMainRefinement(), rv.getName());
				addAllDiferent(lrv, lm);
			}
		}
		if(expectedType instanceof Predicate && ((Predicate)expectedType).isBooleanTrue())
			return;
		process(expectedType, element, mainVars, lrv);
	}

	private void addAllDiferent(List<RefinedVariable> toExpand, List<RefinedVariable> from) {
		for(RefinedVariable rv:from) {
			if(!toExpand.contains(rv))
				toExpand.add(rv);
		}
	}

	public List<RefinedVariable> getVariables(Constraint c, String varName) {
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


	/**
	 * Checks the expectedType against the cSMT constraint.
	 * If the types do not check and error is sent and the program ends
	 * @param cSMT
	 * @param expectedType
	 * @param element
	 */
	void smtChecking(Constraint cSMT, Constraint expectedType, CtElement element) {
//		printVCs("", cSMT.toString(), expectedType);
		try {
			new SMTEvaluator().verifySubtype(cSMT, expectedType, 
					context.getContext(), context.getGhosts(), context.getAlias());
			System.out.println("End smt checking");
		} catch (TypeCheckError e) {
			printError(element, expectedType, cSMT);

		}catch (GhostFunctionError e) {
			printErrorArgs(element, expectedType, e.getMessage());
		}catch(TypeMismatchError e) {
			printErrorTypeMismatch(element, expectedType, e.getMessage());
		}catch (Exception e) {
			System.err.println("Unknown error:"+e.getMessage());
			e.printStackTrace();
			System.exit(7);
			
		}
		//catch(NullPointerException e) {
		//	printErrorUnknownVariable(element, expectedType, correctRefinement);
		//}

	}



	//################################# PRINTS ########################################
	private void printVCs(String string, String stringSMT, Constraint expectedType) {
		System.out.println("\n----------------------------VC--------------------------------");
		System.out.println("VC:\n"+string);
		System.out.println("\nSMT subtyping:" + stringSMT + " <: " + expectedType.toString());
		System.out.println("--------------------------------------------------------------");

	}


	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param expectedType
	 * @param cSMT
	 */
	private <T> void printError(CtElement var, Constraint expectedType, Constraint cSMT) {
		System.out.println("______________________________________________________");
		System.err.println("Failed to check refinement at: ");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Type expected:" + expectedType.toString());
		System.out.println("Refinement found:" + cSMT.toString());
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}
	private <T> void printErrorUnknownVariable(CtElement var, String et, String correctRefinement) {
		System.out.println("______________________________________________________");
		System.err.println("Encountered unknown variable");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}
	private <T> void printErrorArgs(CtElement var, Constraint expectedType, String msg) {
		System.out.println("______________________________________________________");
		System.err.println("Error in ghost invocation: "+ msg);
		System.out.println(var+"\nError in refinement:" + expectedType.toString());
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}

	private void printErrorTypeMismatch(CtElement element, Constraint expectedType, String message) {
		System.out.println("______________________________________________________");
		System.err.println(message);
		System.out.println();
		System.out.println(element);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
		
	}
}
