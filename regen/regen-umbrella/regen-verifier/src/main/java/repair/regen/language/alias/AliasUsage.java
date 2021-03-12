package repair.regen.language.alias;

import java.util.ArrayList;
import java.util.List;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.function.Argument;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.smt.TranslatorToZ3;

public class AliasUsage extends Expression implements IModel{
	AliasName name;
	ParenthesisLeft pl;
	Argument arg;
	ParenthesisRight pr;

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return (ctx.makeAlias(name, getExpressions())).eval(ctx);
//		return null;
	}

	@Override
	public String toString() {
		return name.toString()+"("+arg.toString()+")";
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		arg.substituteVariable(from, to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		arg.getVariableNames(l);
	}
	
	public List<Expression> getExpressions() {
		List<Expression> lv = new ArrayList<>();
		arg.getAllExpressions(lv);
		return lv;
	}

}
