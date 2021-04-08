package repair.regen.rj_language.visitors;

import static org.junit.Assert.fail;

import org.antlr.v4.runtime.tree.ParseTree;

import repair.regen.ast.Expression;
import rj.grammar.RJParser.ExpBoolContext;
import rj.grammar.RJParser.ExpContext;
import rj.grammar.RJParser.ExpGroupContext;
import rj.grammar.RJParser.ExpOperandContext;
import rj.grammar.RJParser.FunctionCallContext;
import rj.grammar.RJParser.InvocationContext;
import rj.grammar.RJParser.IteContext;
import rj.grammar.RJParser.LitContext;
import rj.grammar.RJParser.LitGroupContext;
import rj.grammar.RJParser.LiteralContext;
import rj.grammar.RJParser.LiteralExpressionContext;
import rj.grammar.RJParser.OpArithContext;
import rj.grammar.RJParser.OpGroupContext;
import rj.grammar.RJParser.OpLiteralContext;
import rj.grammar.RJParser.OpMinusContext;
import rj.grammar.RJParser.OpNotContext;
import rj.grammar.RJParser.OpSubContext;
import rj.grammar.RJParser.OperandContext;
import rj.grammar.RJParser.PredContext;
import rj.grammar.RJParser.PredGroupContext;
import rj.grammar.RJParser.PredLogicContext;
import rj.grammar.RJParser.PredNegateContext;
import rj.grammar.RJParser.ProgContext;
import rj.grammar.RJParser.StartContext;
import rj.grammar.RJParser.StartPredContext;
import rj.grammar.RJParser.TargetInvocationContext;
import rj.grammar.RJParser.VarContext;

public class CreateASTVisitor {

	public static Expression create(ParseTree rc) throws Exception {
		if(rc instanceof ProgContext)
			return progCreate((ProgContext)rc);
		else if(rc instanceof StartContext)
			return startCreate(rc);
		else if(rc instanceof PredContext)
			return predCreate(rc);
		else if(rc instanceof ExpContext) 
			return expCreate(rc);
		else if(rc instanceof OperandContext)
			return operandCreate(rc);
		else if(rc instanceof LiteralExpressionContext) 
			return literalExpressionCreate(rc);
		else if(rc instanceof FunctionCallContext)
			return functionCallCreate((FunctionCallContext)rc);
		else if(rc instanceof LiteralContext)
			return literalCreate((LiteralContext)rc);

		return null;
	}




	private static Expression progCreate(ProgContext rc) throws Exception {
		if(rc.start() != null)
			return create(rc.start());
		return null;
	}

	private  static Expression startCreate(ParseTree rc) throws Exception{
		if(rc instanceof StartPredContext)
			return create(((StartPredContext) rc).pred());
		//alias and ghost do not have evaluation
		return null;
	}

	private  static Expression predCreate(ParseTree rc) throws Exception {
		if(rc instanceof PredGroupContext)
			return null;
		else if(rc instanceof PredNegateContext)
			return null;
		else if(rc instanceof PredLogicContext) {
			return null;
		}else if(rc instanceof IteContext) {
			return null;
		}else {
			return null;
		}
	}

	private  static Expression expCreate(ParseTree rc) throws Exception {
		if(rc instanceof ExpGroupContext)
			return null;
		else if(rc instanceof ExpBoolContext) {
			return null;
		}else {
			ExpOperandContext eoc = (ExpOperandContext) rc;
			return null;
		}
	}

	private  static Expression operandCreate(ParseTree rc) throws Exception{
		if(rc instanceof OpLiteralContext)
			return null;
		else if(rc instanceof OpArithContext) {
			return null;
		}else if(rc instanceof OpSubContext) {
			return null;
		}else if(rc instanceof OpMinusContext) {
			return null;
		}else if(rc instanceof OpNotContext){
			return null;
		} else if(rc instanceof OpGroupContext) {
			return null;
		}
		fail("Error achieved case not covered in operandEvaluate");
		return null;
	}

	private  static Expression literalExpressionCreate(ParseTree rc) throws Exception{
		if(rc instanceof LitGroupContext)
			return null;
		else if(rc instanceof LitContext)
			return null;
		else if(rc instanceof VarContext) {
			return null;
		}else if(rc instanceof TargetInvocationContext) {
			//TODO Finish Invocation with Target (a.len())
			return null;
		}else {
			InvocationContext ic = (InvocationContext) rc;
			return null;
		}
	}
	
	
	private static Expression functionCallCreate(FunctionCallContext rc) {
		// TODO Auto-generated method stub
		return null;
	}


	private static Expression literalCreate(LiteralContext rc) {
		// TODO Auto-generated method stub
		return null;
	}










}