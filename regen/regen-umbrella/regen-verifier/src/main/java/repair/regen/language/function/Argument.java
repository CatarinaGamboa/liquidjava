package repair.regen.language.function;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Optional;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.smt.TranslatorToZ3;
/*
 * FArgs = emp | Arguments
 * Arguments = Exp FollowUp | Exp
 * FollowUp = , Arguments
 */
public class Argument implements IModel{
	Expression v;
	@Optional
	FollowUpArgument fua;
	
	public void eval(TranslatorToZ3 ctx, List<Expr> l) throws Exception{
		l.add(v.eval(ctx));
		if(hasFollowUpArgument())
			fua.eval(ctx, l);
	}
	
	public Expression getExpression() {
		return v;
	}
	
	public boolean hasFollowUpArgument() {
		return fua!=null;
	}
	
	public FollowUpArgument getFollowUpArgument() {
		return fua;
	}
	

	public String toString() {
		return v.toString() + (fua == null? "":fua.toString());
	}
	
	public void substituteVariable(String from, String to) {
		v.substituteVariable(from, to);
		if(fua != null)
			fua.substituteVariable(from, to);
	}

	public void getVariableNames(List<String> l) {
		v.getVariableNames(l);
		if(fua != null)
			fua.getVariableNames(l);
	}
	


	public void getAllExpressions(List<Expression> lv) {
		lv.add(v);
		if(fua != null)
			fua.getAllExpressions(lv);
		
	}

	public void setExpression(int i, Expression e) {
		if(i == 0)
			v = e;
		else if(fua != null)
			fua.getArgument().setExpression(i-1, e);
		else {
			System.out.println("Expression index not found: "+ i);
			assert(false);
		}
	}

}
