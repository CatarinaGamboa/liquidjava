package repair.regen.processor.constraints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repair.regen.ast.Expression;
import repair.regen.ast.UnaryExpression;
import repair.regen.errors.ErrorEmitter;
import repair.regen.errors.ErrorHandler;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.facade.AliasDTO;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

public abstract class Constraint {
	
	public abstract Constraint substituteVariable(String from, String to);
	
	public abstract Constraint clone();
	public abstract List<String> getVariableNames();
	public abstract String toString();
	public abstract boolean isBooleanTrue();
	public abstract Constraint changeOldMentions(String previousName, String newName, ErrorEmitter ee);
	public abstract Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange, ErrorEmitter ee);
	public abstract Expression getExpression();
	
	
	public Constraint negate() {
		Expression e = new UnaryExpression("!", getExpression());
		return new Predicate(e);
	}

	protected Expression parse(String ref, CtElement element, ErrorEmitter e) throws ParsingException {
		try{
			return RefinementsParser.createAST(ref);	 
		} catch (ParsingException e1) {
			ErrorHandler.printSyntaxError(e1.getMessage(), ref, element, e);
			throw e1;
		}	
	}
	
	protected Expression innerParse(String ref, ErrorEmitter e) {
		try{
			return RefinementsParser.createAST(ref);	 
		} catch (ParsingException e1) {
			ErrorHandler.printSyntaxError(e1.getMessage(), ref, e);
		}	
		return null;
	}

	
	public Constraint changeAliasToRefinement(Context context, CtElement element, Factory f) throws Exception {
		Expression ref = getExpression();
		
		Map<String, AliasDTO> alias = new HashMap<>();
		for(AliasWrapper aw : context.getAlias()) {
			alias.put(aw.getName(), aw.createAliasDTO());
		}
		
		//      name         refinement             type    variable
//		HashMap<String, Pair<Expression, List<Pair<String, Expression>>>> mapAlias = new HashMap();
//		for(AliasWrapper aw : context.getAlias()) {
//			List<Expression> argumentsExpressions = aw.getVarNames().stream().map(p->parse(p)).collect(Collectors.toList());
//			List<String> argumentsTypes = aw.getTypes().stream().map(p->p.getQualifiedName()).collect(Collectors.toList());
//			
//			List<Pair<String, Expression>> l = new ArrayList<>();
//			//zip
//			for (int i = 0; i < argumentsExpressions.size(); i++)
//				l.add(new Pair<>(argumentsTypes.get(i), argumentsExpressions.get(i)));
//			
//			Pair<Expression, List<Pair<String, Expression>>> p = 
//					new Pair<>(aw.getClonedConstraint().getExpression(), l);
//			mapAlias.put(aw.getName(), p);
//		}
		ref = ref.changeAlias(alias, context, f);
		return new Predicate(ref);
	}

	

}
