package repair.regen.smt;

import java.util.HashMap;

import com.microsoft.z3.Context;

import spoon.reflect.declaration.CtType;

public class SMTEvaluator {
	
	private Context prepareContext(HashMap<String, CtType<?>> ctx) {
		Context z3ctx = new Context();
		for (String name : ctx.keySet()) {
			z3ctx.mkIntConst(name);
		}
		return z3ctx;
	}
	
	public void verifySubtype(String subRef, String supRef, HashMap<String, CtType<?>> ctx) {
		Context z3ctx = prepareContext(ctx);
		// TODO: create a parser for our SMT-ready refinement language
		// TODO: discharge the verification to z3
	}
}
