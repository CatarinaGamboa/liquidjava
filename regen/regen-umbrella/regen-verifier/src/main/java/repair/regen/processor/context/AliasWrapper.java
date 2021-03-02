package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import repair.regen.language.Expression;
import repair.regen.language.alias.Alias;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Disjunction;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.CatchVariableScopeFunction;

public class AliasWrapper {
	private String name;
	private List<CtTypeReference<?>> varTypes;
	private List<String> varNames;
	private Predicate expression;
	private Context context;
	
	private String newAliasFormat = "#alias_%s_%d";

	public AliasWrapper(Alias alias, Factory factory, String wildvar, Context c) {
		name = alias.getName();
		this.varNames = alias.getVariableNames();
		this.varTypes = new ArrayList<>();
		for(String t:alias.getTypesNames())
			this.varTypes.add(Utils.getType(t, factory));
		this.expression = new Predicate(alias.getExpression());
		context= c;
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

	public Expression getNewExpression(List<String> newNames) {
		Constraint expr = getClonedConstraint();
		for (int i = 0; i < newNames.size(); i++) {
			expr = expr.substituteVariable(varNames.get(i), newNames.get(i));
		}
		return (new Predicate(expr.toString())).getExpression();		
	}
	
	
	public Expression getPremises(List<Expression> list, List<String> newNames){
		List<Predicate> invocationPredicates = getPredicatesFromExpression(list);
		Constraint prem = new Predicate();
		for (int i = 0; i < invocationPredicates.size(); i++) {
			prem = Conjunction.createConjunction(prem, 
					new EqualsPredicate(new VariablePredicate(newNames.get(i)) , invocationPredicates.get(i)));
		}
		return prem.getExpression();
	}

	private List<Predicate> getPredicatesFromExpression(List<Expression> list) {
		List<Predicate> lp = new ArrayList<>();
		for(Expression e: list)
			lp.add(new Predicate(e));
	
		return lp;
	}

	public List<String> getNewVariables() {
		List<String> n = new ArrayList<>();
		for(int i=0; i < varNames.size(); i++) 
			n.add(String.format(newAliasFormat, varNames.get(i), context.getCounter()));
		return n;
	}
	
	public Map<String, CtTypeReference<?>> getTypes(List<String> names){
		Map<String, CtTypeReference<?>> m = new HashMap<>();
		for (int i = 0; i < names.size(); i++) {
			m.put(names.get(i), varTypes.get(i));
		}
		return m;
	}
	

//	public Expression getSubstitutedExpression(List<String> newNames) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
	
	
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
