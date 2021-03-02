package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;

import repair.regen.language.Expression;
import repair.regen.language.IfElseExpression;

public class IfThenElse extends Constraint{
	private Constraint condition;
	private Constraint then;
	private Constraint els;
	
	public IfThenElse(Constraint a, Constraint b, Constraint c) {
		condition = a;
		then = b;
		els = c;
	}

	@Override
	public Constraint substituteVariable(String from, String to) {
		Constraint c1 = condition.substituteVariable(from, to);
		Constraint c2 = then.substituteVariable(from, to);
		Constraint c3 = els.substituteVariable(from, to);
		return new IfThenElse(c1,c2, c3);
	}

	@Override
	public Constraint negate() {
		Predicate p = new Predicate(this.toString());
		return p.negate();
	}

	@Override
	public Constraint clone() {
		return new IfThenElse(condition.clone(), then.clone(), els.clone());
	}

	@Override
	public List<String> getVariableNames() {
		List<String> a = new ArrayList<>();
		for(String s:condition.getVariableNames())
			if(!a.contains(s))
				a.add(s);
		for(String s:then.getVariableNames())
			if(!a.contains(s))
				a.add(s);
		for(String s:els.getVariableNames())
			if(!a.contains(s))
				a.add(s);
		return a;
	}

	@Override
	public String toString() {
		return "("+condition.toString()+"? "+then.toString()+" : "+els.toString()+")";
	}
	
	@Override
	Expression getExpression() {
		return new IfElseExpression(condition.getExpression(), then.getExpression(), els.getExpression());
	}

}
