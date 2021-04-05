package repair.regen.rj_language.visitors;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import repair.regen.language.BinaryExpression;
import repair.regen.language.BooleanLiteral;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.Variable;
import rj.grammar.RJParser.ExpGroupContext;
import rj.grammar.RJParser.ExpOperandContext;
import rj.grammar.RJParser.IteContext;
import rj.grammar.RJParser.LitContext;
import rj.grammar.RJParser.LitGroupContext;
import rj.grammar.RJParser.LiteralContext;
import rj.grammar.RJParser.OpLiteralContext;
import rj.grammar.RJParser.PredExpContext;
import rj.grammar.RJParser.PredGroupContext;
import rj.grammar.RJParser.PredLogicContext;
import rj.grammar.RJParser.StartPredContext;
import rj.grammar.RJParser.VarContext;

public class BooleanTrueVisitor {
	
	public boolean isTrue(ParseTree rc) {
		if(rc instanceof LiteralContext) {
			LiteralContext c = (LiteralContext) rc;
			if(c.BOOL() != null)
				return c.BOOL().getText().equals("true");
		}else if (rc instanceof StartPredContext)
			return isTrue(((StartPredContext)rc).pred());
		else if (rc instanceof PredGroupContext)
			return isTrue(((PredGroupContext)rc).pred());
		else if(rc instanceof PredLogicContext) {
			return isTrue(((PredLogicContext)rc).pred(0)) &&
				   isTrue(((PredLogicContext)rc).pred(1));
		}else if(rc instanceof IteContext)	
			return isTrue(((PredLogicContext)rc).pred(0)) &&
					   isTrue(((PredLogicContext)rc).pred(1))&&
					   isTrue(((PredLogicContext)rc).pred(2));
		else if(rc instanceof PredExpContext)
			return isTrue(((PredExpContext)rc).exp());
		else if(rc instanceof ExpGroupContext)
			return isTrue(((ExpGroupContext)rc).exp());
		else if(rc instanceof ExpOperandContext)
			return isTrue(((ExpOperandContext)rc).operand());
		else if(rc instanceof OpLiteralContext)
			return isTrue(((OpLiteralContext)rc).literalExpression());
		else if(rc instanceof LitGroupContext)
			return isTrue(((LitGroupContext)rc).literalExpression());
		else if(rc instanceof LitContext)
			return isTrue(((LitContext)rc).literal());
		return false;
	}

}
//if(exp instanceof BinaryExpression) {
//	return isTrue(((BinaryExpression) exp).getFirstExpression()) &&
//		   isTrue(((BinaryExpression) exp).getSecondExpression());
//}else if(exp instanceof ExpressionGroup)
//	return isTrue(((ExpressionGroup) exp).getExpression());
//else if(exp instanceof BooleanLiteral) {
//	if(((BooleanLiteral)exp).getValue())
//		return true;
//}else if(exp instanceof Variable)
//	if(((Variable)exp).getName().equals("true"))
//		return true;
//
//return false;