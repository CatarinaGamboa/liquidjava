package repair.regen.language.alias;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.smt.TranslatorToZ3;

public class AliasUsage extends Expression implements IModel{
	AliasName name;
	ParenthesisLeft pl;
	Variable var;
	ParenthesisRight pr;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return name.toString()+"("+var.toString()+")";
	}

}
