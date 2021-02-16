package repair.regen.language.function;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.symbols.Dot;
import repair.regen.smt.TranslatorToZ3;

public class ObjectFieldInvocation extends Expression implements IModel{
	Variable var;
	Dot d;
	FunctionName name;
	
// TODO add more possible args 	
//	ParenthesisLeft pl;
////	@Multiplicity(minimum=0,maximum=1000)
////	@Optional
//	Argument mv;
//	ParenthesisRight pr;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		Expr[] e = new Expr[1];
		e[0] = var.eval(ctx);//TODO add others after
		return ctx.makeFunctionInvocation(name.toString(), e);
	}
	
	public Variable getVariable() {
		return var;
	}
	
	public String getFunctionName() {
		return name.toString();
	}

	@Override
	public String toString() {
		return var.toString()+"."+name.toString();
	}

}
