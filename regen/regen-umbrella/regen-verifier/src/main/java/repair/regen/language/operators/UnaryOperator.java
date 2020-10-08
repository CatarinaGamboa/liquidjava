package repair.regen.language.operators;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

public abstract class UnaryOperator implements IModel {
	public abstract Expr eval(TranslatorToZ3 ctx, Expression e);
}