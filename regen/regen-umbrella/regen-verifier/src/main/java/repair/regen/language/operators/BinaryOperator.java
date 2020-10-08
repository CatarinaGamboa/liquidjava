package repair.regen.language.operators;

import org.modelcc.Associativity;
import org.modelcc.AssociativityType;
import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Associativity(AssociativityType.LEFT_TO_RIGHT)
public abstract class BinaryOperator implements IModel {
	public abstract Expr eval(TranslatorToZ3 ctx, Expression e1, Expression e2);
}
