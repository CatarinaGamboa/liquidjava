package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repair.regen.ast.Expression;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.utils.Pair;
import repair.regen.utils.Triple;
import repair.regen.utils.Utils;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class AliasWrapper {
	private String name;
	private List<CtTypeReference<?>> varTypes;
	private List<String> varNames;
	private Predicate expression;
//	private Context context;
	
	private String newAliasFormat = "#alias_%s_%d";

	public AliasWrapper(Triple<String, String, List<Pair<String, String>>> alias, Factory factory, String wILD_VAR,
			Context context2, String klass, String path) {
		name = alias.getFist();
		expression = new Predicate(alias.getSecond());
		
		varTypes = new ArrayList<>();
		varNames = new ArrayList<>();
		for(Pair<String,String> p : alias.getThird()) {
			CtTypeReference<?> r = Utils.getType(p.getFirst().equals(klass)? path: p.getFirst(), factory);
			varTypes.add(r);
			varNames.add(p.getSecond());
		}			
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
		return expr.getExpression().clone();		
	}
	
	
	public Constraint getPremises(List<String> list, List<String> newNames){
		List<Predicate> invocationPredicates = getPredicatesFromExpression(list);
		Constraint prem = new Predicate();
		for (int i = 0; i < invocationPredicates.size(); i++) {
			prem = Conjunction.createConjunction(prem, 
					new EqualsPredicate(new VariablePredicate(newNames.get(i)) , invocationPredicates.get(i)));
		}
		return prem.clone();
	}

	private List<Predicate> getPredicatesFromExpression(List<String> list) {
		List<Predicate> lp = new ArrayList<>();
		for(String e: list)
			lp.add(new Predicate(e));
	
		return lp;
	}

	public List<String> getNewVariables(Context context) {
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
