package repair.regen.processor.refinement_checker;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.processor.context.ObjectState;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Utils;
import repair.regen.processor.context.Variable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;

public class AuxHierarchyRefinememtsPassage {

	static <R> void checkFunctionInSupertypes(CtClass<?> klass, CtMethod<R> method, RefinedFunction f ,TypeChecker tc) {
		String name = method.getSimpleName();
		int size = method.getParameters().size();
		if(klass.getSuperInterfaces().size() > 0) {			//implemented interfaces
			Optional<RefinedFunction> superFunction = functionInInterface(klass, 
					name, size, tc);
			if(superFunction.isPresent()) {
//				System.out.println("superFunction: "+superFunction+ "; class="+superFunction.get().getTargetClass());
				transferRefinements(superFunction.get(), f, method, tc);
			}
		}
		if(klass.getSuperclass() != null) { 				//extended class
			CtTypeReference<?> t = klass.getSuperclass();
			RefinedFunction superFunction = tc.context.getFunction(name, t.getQualifiedName(), size);
			if(superFunction != null) {
//				System.out.println("superFunction: "+superFunction+ "; class="+superFunction.getTargetClass());
				transferRefinements(superFunction, f, method, tc);
			}
		}

	}

	static void transferRefinements(RefinedFunction superFunction, RefinedFunction function, CtMethod<?> method, TypeChecker tc) {
		HashMap<String, String> super2function = getParametersMap(superFunction, function, tc);
		transferReturnRefinement(superFunction,function, method, tc, super2function);
		transferArgumentsRefinements(superFunction, function, method, tc, super2function);
		transferStateRefinements(superFunction, function, method, tc);
	}


	private static HashMap<String, String> getParametersMap(RefinedFunction superFunction, RefinedFunction function, TypeChecker tc) {
		List<Variable> superArgs = superFunction.getArguments();
		List<Variable> fArgs = function.getArguments();
		HashMap<String, String> m = new HashMap<String, String>();
		for (int i = 0; i < fArgs.size(); i++) {
			String newName = String.format(tc.instanceFormat, fArgs.get(i).getName(), tc.context.getCounter());
			m.put(superArgs.get(i).getName(), newName); 
			m.put(fArgs.get(i).getName(), newName);
			tc.context.addVarToContext(newName, superArgs.get(i).getType(), new Predicate());
		}
		return m;
	}

	static void transferArgumentsRefinements(RefinedFunction superFunction, RefinedFunction function, 
			CtMethod<?> method, TypeChecker tc, HashMap<String, String> super2function) {
		List<Variable> superArgs = superFunction.getArguments();
		List<Variable> args = function.getArguments();
		List<CtParameter<?>> params = method.getParameters();
		for (int i = 0; i < args.size(); i++) {
			Variable arg = args.get(i);
			Variable superArg = superArgs.get(i);
			String newName = super2function.get(arg.getName());
			//create new name
			Constraint argRef = arg.getRefinement().substituteVariable(arg.getName(), newName);
			Constraint superArgRef =superArg.getRefinement().substituteVariable(superArg.getName(), newName);
			
			System.out.println(arg.getName()+" has ref "+argRef);
			if(argRef.isBooleanTrue()) {
				System.out.println(arg.getName()+" has ref boolean true");
				arg.setRefinement(superArgRef.substituteVariable(newName, arg.getName()));
			} else {
				boolean f = tc.checkStateSMT(superArgRef, argRef, params.get(i));
				if(!f)
					ErrorPrinter.printError(method, argRef, superArgRef);
			}
		}

	}

	static void transferReturnRefinement(RefinedFunction superFunction, RefinedFunction function, CtMethod<?> method, TypeChecker tc, HashMap<String, String> super2function) {
		Constraint functionRef =  function.getRefinement();
		Constraint superRef = superFunction.getRefinement();
		if(functionRef.isBooleanTrue())
			function.setRefinement(superRef);
		else{
			String name = String.format(tc.freshFormat, tc.context.getCounter());
			tc.context.addVarToContext(name, superFunction.getType(), new Predicate());
			//functionRef might be stronger than superRef -> check (superRef <: functionRef)
			functionRef = functionRef.substituteVariable(tc.WILD_VAR, name);
			superRef = superRef.substituteVariable(tc.WILD_VAR, name);
			for(String m:super2function.keySet()) 
				superRef  = superRef.substituteVariable(m, super2function.get(m));
			for(String m:super2function.keySet()) 
				functionRef  = functionRef.substituteVariable(m, super2function.get(m));
		
			boolean f = tc.checkStateSMT(functionRef, superRef, method);
			if(!f)
				ErrorPrinter.printError(method, superRef, functionRef);
		}
	}



	static Optional<RefinedFunction> functionInInterface(CtClass<?> klass, String simpleName, int size, TypeChecker tc) {
		List<RefinedFunction> lrf = tc.context.getAllMethodsWithNameSize(simpleName, size);
		List<String> st = klass.getSuperInterfaces().stream().map(p->p.getQualifiedName()).collect(Collectors.toList());
		for(RefinedFunction rf :lrf) {
			if(st.contains(rf.getTargetClass()))
				return Optional.of(rf);//TODO only works for 1 interface
		}
		return Optional.empty();
	}


	private static void transferStateRefinements(RefinedFunction superFunction, RefinedFunction subFunction,
			CtMethod<?> method, TypeChecker tc) {
		if(superFunction.hasStateChange()) {
			if(!subFunction.hasStateChange()) {
				for(ObjectState o: superFunction.getAllStates())
					subFunction.addStates(o.clone());
			}else {
				List<ObjectState> superStates = superFunction.getAllStates();
				List<ObjectState> subStates = subFunction.getAllStates();
				for (int i = 0; i < superStates.size(); i++) {
					ObjectState superState = superStates.get(i);
					ObjectState subState = subStates.get(i);

					String thisName = String.format(tc.freshFormat, tc.context.getCounter());
					createVariableInContext(thisName, tc, subFunction, superFunction);
					
					Constraint superConst = matchVariableNames(tc.THIS, thisName, superState.getFrom());
					Constraint subConst = matchVariableNames(tc.THIS, thisName, superFunction, 
							subFunction, subState.getFrom());

					//fromSup <: fromSub   <==> fromSup is sub type and fromSub is expectedType
					boolean correct = tc.checkStateSMT(superConst, subConst, method);
					if(!correct) ErrorPrinter.printError(method, subState.getFrom(), superState.getFrom());
					System.out.println("Came to checkStates hierarchy");

					
					superConst = matchVariableNames(tc.THIS, thisName, superState.getTo());
					subConst = matchVariableNames(tc.THIS, thisName,superFunction, subFunction, subState.getTo());
					//toSub <: toSup   <==> ToSub is sub type and toSup is expectedType
					correct = tc.checkStateSMT(subConst, superConst, method);
					if(!correct) ErrorPrinter.printError(method, subState.getTo(), superState.getTo());
				}
			}
		}



	}

	private static void createVariableInContext(String thisName, TypeChecker tc, RefinedFunction subFunction, RefinedFunction superFunction) {
		RefinedVariable rv  = tc.context.addVarToContext(thisName, Utils.getType(subFunction.getTargetClass(), tc.factory), 
				new Predicate());
		rv.addSuperType(Utils.getType(superFunction.getTargetClass(), tc.factory));//TODO: change: this only works for one superclass
		
	}

	/**
	 * Changes all variable names in c to match the names of superFunction
	 * @param fromName
	 * @param thisName
	 * @param superFunction
	 * @param subFunction
	 * @param c
	 * @return
	 */
	private static Constraint matchVariableNames(String fromName, String thisName, RefinedFunction superFunction,
			RefinedFunction subFunction, Constraint c) {
		Constraint nc = c.substituteVariable(fromName, thisName);
		List<Variable> superArgs = superFunction.getArguments();
		List<Variable> subArgs = subFunction.getArguments();
		for (int i = 0; i < subArgs.size(); i++) {
			nc.substituteVariable(subArgs.get(i).getName(), superArgs.get(i).getName());
		}
		return nc;
	}

	private static Constraint matchVariableNames(String fromName, String thisName, Constraint c) {
		return c.substituteVariable(fromName, thisName);
	}
}