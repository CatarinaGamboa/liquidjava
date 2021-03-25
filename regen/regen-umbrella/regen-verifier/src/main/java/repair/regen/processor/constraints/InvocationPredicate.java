package repair.regen.processor.constraints;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.function.Argument;
import repair.regen.language.function.FunctionInvocationExpression;

public class InvocationPredicate extends Predicate{

	public InvocationPredicate(String name, String... params) {
		Expression e;
		if(params.length == 1) {
			Argument a = new Argument(new Variable(params[0]));
			FunctionInvocationExpression fie = new FunctionInvocationExpression(name, a);
			setExpression(fie);
		}
	}
	public InvocationPredicate(String name, Expression e) {
		Argument a = new Argument(e);
		FunctionInvocationExpression fie = new FunctionInvocationExpression(name, a);
		setExpression(fie);

	}
}
