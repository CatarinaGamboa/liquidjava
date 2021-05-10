package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;

import repair.regen.ast.Expression;
import repair.regen.ast.FunctionInvocation;

public class InvocationPredicate extends Predicate{

	public InvocationPredicate(String name, String... params) {
		List<Expression> le = new ArrayList<>();
		for(String s: params) {
			le.add(innerParse(s));
		}
		exp = new FunctionInvocation(name, le);
	}
	
	public InvocationPredicate(String name, String s) {
		List<Expression> le = new ArrayList<>();
		le.add(innerParse(s));
		exp = new FunctionInvocation(name, le);
	}

	public InvocationPredicate(String name, Expression expression) {
		List<Expression> le = new ArrayList<>();
		le.add(expression);
		exp = new FunctionInvocation(name, le);
	}
}
