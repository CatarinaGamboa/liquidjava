package repair.regen.processor.refinement_checker;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.ObjectState;
import repair.regen.processor.context.RefinedFunction;
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
				transferRefinements(superFunction.get(), f, method, tc);
			}
		}
		if(klass.getSuperclass() != null) { 				//extended class
			CtTypeReference<?> t = klass.getSuperclass();
			RefinedFunction superFunction = tc.context.getFunction(name, t.getQualifiedName(), size);
			if(superFunction != null) {
				transferRefinements(superFunction, f, method, tc);
			}
		}
		
	}

	static void transferRefinements(RefinedFunction superFunction, RefinedFunction function, CtMethod<?> method, TypeChecker tc) {
		HashMap<String, String> super2function = getParametersMap(superFunction, function);
		transferReturnRefinement(superFunction,function, method, tc, super2function);
		transferArgumentsRefinements(superFunction, function, method, tc, super2function);
		transferStateRefinements(superFunction, function, method, tc);
	}


	private static HashMap<String, String> getParametersMap(RefinedFunction superFunction, RefinedFunction function) {
		List<Variable> superArgs = superFunction.getArguments();
		List<Variable> fArgs = function.getArguments();
		HashMap<String, String> m = new HashMap<String, String>();
		for (int i = 0; i < fArgs.size(); i++) {
			m.put(superArgs.get(i).getName(), fArgs.get(i).getName());
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
			Constraint argRef = arg.getRefinement();
			Constraint superArgRef = superArg.getRefinement().substituteVariable(superArg.getName(), 
															  super2function.get(superArg.getName()));
			if(argRef.isBooleanTrue())
				arg.setRefinement(superArgRef);
			else {
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
				return Optional.of(rf);
		}
		return Optional.empty();
	}
	

	private static void transferStateRefinements(RefinedFunction superFunction, RefinedFunction function,
			CtMethod<?> method, TypeChecker tc) {
		if(superFunction.hasStateChange()) {
			if(!function.hasStateChange()) {
				for(ObjectState o: superFunction.getAllStates())
					function.addStates(o.clone());
			}else {
				//TODO verify subtype
			}
		}
		
	}

}
