package repair.regen.language.function;

import java.util.ArrayList;
import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Multiplicity;
import org.modelcc.Optional;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.smt.TranslatorToZ3;

public class FunctionInvocationExpression extends Expression implements IModel{
	FunctionName name;
	ParenthesisLeft pl;
	
//	@Multiplicity(minimum=0,maximum=1000)
//	@Optional -> no args not allowed by z3
	Argument mv;

	ParenthesisRight pr;

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception{
		List<Expr> params = new ArrayList<>();
		mv.eval(ctx, params);
		Expr[] ps = params.stream().toArray(Expr[]::new);
		return ctx.makeFunctionInvocation(name.toString(), ps);
		
	}
	
	public String getFunctionName() {
		return name.toString();
	}
	
	public Argument getArgument() {
		return mv;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name.toString()+" (");
		sb.append(mv.toString());

		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		mv.substituteVariable(from, to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		mv.getVariableNames(l);
	}

	public void setArgument(int i, Expression e) {
		mv.setExpression(i, e);
	}

	
	
}
