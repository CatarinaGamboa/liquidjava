package repair.regen.language;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public abstract class Expression implements IModel {
	public abstract Expr eval(TranslatorToZ3 ctx);
	
	public abstract String toString();
	
	public abstract void substituteVariable(String from, String to);
}
