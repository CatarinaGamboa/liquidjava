package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;

import repair.regen.language.alias.Alias;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class AliasWrapper {
	private String name;
	private List<CtTypeReference<?>> varTypes;
	private List<String> varNames;
	private Predicate expression;
	

	public AliasWrapper(Alias alias, Factory factory, String wildvar) {
		name = alias.getName();
		this.varNames = alias.getVariableNames();
		this.varTypes = new ArrayList<>();
		for(String t:alias.getTypesNames())
			this.varTypes.add(Utils.getType(t, factory));
		this.expression = new Predicate(alias.getExpression());
	}

	public String getName() {
		return name;
	}
	
	public List<CtTypeReference<?>> getTypes() {
		return varTypes;
	}
	
	
	public List<String> getVarNames() {
		return varNames;
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
