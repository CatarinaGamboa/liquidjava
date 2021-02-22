package repair.regen.language.alias;

import java.util.List;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.smt.TranslatorToZ3;
import repair.regen.smt.TypeCheckError;

public class AliasUsage extends Expression implements IModel{
	AliasName name;
	ParenthesisLeft pl;
	Variable var;
	ParenthesisRight pr;

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return ctx.makeAlias(name, var).eval(ctx);
	}


	@Override
	public String toString() {
		return name.toString()+"("+var.toString()+")";
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		var.substituteVariable(from, to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		var.getVariableNames(l);
	}

}
