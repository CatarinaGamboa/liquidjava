package repair.regen.processor.context;

import repair.regen.language.alias.Alias;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class AliasWrapper {
	private String name;
	private CtTypeReference<?> varType;
	private String varName;
	private Predicate expression;
	

	public AliasWrapper(Alias alias, Factory factory) {
		name = alias.getName();
		this.varName = alias.getVar().toString();
		this.varType = Utils.getType(alias.getType().toString(), factory);
		this.expression = new Predicate(alias.getExpression());
	}

	public String getName() {
		return name;
	}
	
	public CtTypeReference getType() {
		return varType;
	}
	public String getVarName() {
		return varName;
	}
	public Predicate getClonedConstraint() {
		return (Predicate) expression.clone();
	}
	
	
	
//	TypeKeyword tk;
//	AliasName name;
//	
//	ParenthesisLeft pl;
//	Type type;
//	Var var;
//	ParenthesisRight rl;
//	
//	BraceLeft bl;
//	Expression e;
//	BraceRight br;

}
