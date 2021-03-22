package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.shared.utils.cli.Commandline.Argument;

import repair.regen.language.BinaryExpression;
import repair.regen.language.BooleanLiteral;
import repair.regen.language.Expression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.IfElseExpression;
import repair.regen.language.UnaryExpression;
import repair.regen.language.Variable;
import repair.regen.language.alias.AliasUsage;
import repair.regen.language.function.FunctionInvocationExpression;
import repair.regen.processor.context.GhostFunction;

public class AuxVisitTree {

	static void getGhostInvocations(Expression exp, List<GhostFunction> gh, List<GhostFunction> contextGhosts) {
		if(exp instanceof UnaryExpression)
			getGhostInvocations(((UnaryExpression) exp).getExpression(), gh, contextGhosts);
		else if(exp instanceof BinaryExpression) {
			getGhostInvocations(((BinaryExpression) exp).getFirstExpression(), gh, contextGhosts);
			getGhostInvocations(((BinaryExpression) exp).getSecondExpression(), gh, contextGhosts);
		}else if(exp instanceof ExpressionGroup) {
			getGhostInvocations(((ExpressionGroup) exp).getExpression(), gh, contextGhosts);	
		}else if(exp instanceof IfElseExpression) {
			getGhostInvocations(((IfElseExpression) exp).getCondition(), gh, contextGhosts);
			getGhostInvocations(((IfElseExpression) exp).getThenExpression(), gh, contextGhosts);
			getGhostInvocations(((IfElseExpression) exp).getElseExpression(), gh, contextGhosts);
		}else if(exp instanceof AliasUsage) {
			for(Expression e :((AliasUsage)exp).getExpressions())
				getGhostInvocations(e, gh, contextGhosts);
		}else if(exp instanceof FunctionInvocationExpression) {
			FunctionInvocationExpression fie = (FunctionInvocationExpression)exp;
			for(GhostFunction f: contextGhosts) {
				if(f.getName().equals(fie.getFunctionName()))
					gh.add(f);
			}
		}
		
	}
	
	static Expression changeOldMentions(Expression exp, String previousName, String newName) {
		Expression e = null;
		if(exp instanceof UnaryExpression) {
			e = changeOldMentions(((UnaryExpression) exp).getExpression(), previousName, newName);
			if(e != null) ((UnaryExpression) exp).setExpression(e);
		}else if(exp instanceof BinaryExpression) {
			e = changeOldMentions(((BinaryExpression) exp).getFirstExpression(), previousName, newName);
			if(e!=null) ((BinaryExpression) exp).setFirstExpression(e);
			e = changeOldMentions(((BinaryExpression) exp).getSecondExpression(),previousName, newName);
			if(e!=null) ((BinaryExpression) exp).setSecondExpression(e);
		}else if(exp instanceof ExpressionGroup) {
			e = changeOldMentions(((ExpressionGroup) exp).getExpression(),previousName, newName);
			if(e!=null) ((ExpressionGroup) exp).setExpression(e);
		}else if(exp instanceof IfElseExpression) {
			e = changeOldMentions(((IfElseExpression) exp).getCondition(), previousName, newName);
			if(e!=null) ((IfElseExpression) exp).setCondition(e);
			e = changeOldMentions(((IfElseExpression) exp).getThenExpression(), previousName, newName);
			if(e!=null) ((IfElseExpression) exp).setThen(e);
			e = changeOldMentions(((IfElseExpression) exp).getElseExpression(), previousName, newName);
			if(e!=null) ((IfElseExpression) exp).setElse(e);
		}else if(exp instanceof AliasUsage) {
			List<Expression> l = ((AliasUsage)exp).getExpressions();
			for (int i = 0; i < l.size(); i++) {
				e = changeOldMentions(l.get(i), previousName, newName);
				if (e!=null) ((AliasUsage)exp).setExpression(i, e);
			}
		}else if(exp instanceof FunctionInvocationExpression) {
			FunctionInvocationExpression fie = (FunctionInvocationExpression)exp;
			if(fie.getFunctionName().equals("old")) {
				System.out.println("FOUND AN OLD!!!!!");
				return new Variable(newName);
			}
			if(fie.getArgument() != null) {
				List<Expression> le = new ArrayList();
				fie.getArgument().getAllExpressions(le);
				for (int i = 0; i < le.size(); i++) {
					e = changeOldMentions(le.get(i), previousName, newName);
					if(e!=null) fie.setArgument(i, e);
				}
			}
		}
		return null;
	}

	public static void substituteVariable(Expression exp, String from, String to) {
		if(exp instanceof UnaryExpression)
			substituteVariable(((UnaryExpression) exp).getExpression(), from, to);
		else if(exp instanceof BinaryExpression) {
			substituteVariable(((BinaryExpression) exp).getFirstExpression(), from, to);
			substituteVariable(((BinaryExpression) exp).getSecondExpression(), from, to);
		}else if(exp instanceof ExpressionGroup) {
			substituteVariable(((ExpressionGroup) exp).getExpression(), from, to);	
		}else if(exp instanceof IfElseExpression) {
			substituteVariable(((IfElseExpression) exp).getCondition(), from, to);
			substituteVariable(((IfElseExpression) exp).getThenExpression(),  from, to);
			substituteVariable(((IfElseExpression) exp).getElseExpression(), from, to);
		}else if(exp instanceof AliasUsage) {
			for(Expression e :((AliasUsage)exp).getExpressions())
				substituteVariable(e, from, to);
		}else if(exp instanceof FunctionInvocationExpression) {
			FunctionInvocationExpression fie = (FunctionInvocationExpression)exp;
			if(fie.getArgument() != null) {
				List<Expression> le = new ArrayList();
				fie.getArgument().getAllExpressions(le);
				for (int i = 0; i < le.size(); i++) {
					substituteVariable(le.get(i), from, to);
					
				}
			}
		}else if(exp instanceof Variable) {
			Variable v = (Variable) exp;
			if(v.getName().equals(from))
				v.changeName(to);
		}
		
	}

	public static boolean isTrue(Expression exp) {
		if(exp instanceof BinaryExpression) {
			return isTrue(((BinaryExpression) exp).getFirstExpression()) &&
				   isTrue(((BinaryExpression) exp).getSecondExpression());
		}else if(exp instanceof ExpressionGroup)
			return isTrue(((ExpressionGroup) exp).getExpression());
		else if(exp instanceof BooleanLiteral) {
			if(((BooleanLiteral)exp).getValue())
				return true;
		}else if(exp instanceof Variable)
			if(((Variable)exp).getName().equals("true"))
				return true;
		
		return false;
	}
}
