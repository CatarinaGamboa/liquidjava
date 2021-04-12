package repair.regen.rj_language.visitors;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import repair.regen.ast.AliasInvocation;
import repair.regen.ast.BinaryExpression;
import repair.regen.ast.Expression;
import repair.regen.ast.FunctionInvocation;
import repair.regen.ast.GroupExpression;
import repair.regen.ast.Ite;
import repair.regen.ast.LiteralBoolean;
import repair.regen.ast.LiteralInt;
import repair.regen.ast.LiteralReal;
import repair.regen.ast.LiteralString;
import repair.regen.ast.UnaryExpression;
import repair.regen.ast.Var;
import rj.grammar.RJParser.AliasCallContext;
import rj.grammar.RJParser.ArgsContext;
import rj.grammar.RJParser.ExpBoolContext;
import rj.grammar.RJParser.ExpContext;
import rj.grammar.RJParser.ExpGroupContext;
import rj.grammar.RJParser.ExpOperandContext;
import rj.grammar.RJParser.FunctionCallContext;
import rj.grammar.RJParser.GhostCallContext;
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
import rj.grammar.RJParser.PredExpContext;
import rj.grammar.RJParser.PredGroupContext;
import rj.grammar.RJParser.PredLogicContext;
import rj.grammar.RJParser.PredNegateContext;
import rj.grammar.RJParser.ProgContext;
import rj.grammar.RJParser.StartContext;
import rj.grammar.RJParser.StartPredContext;
import rj.grammar.RJParser.TargetInvocationContext;
import rj.grammar.RJParser.VarContext;

public class CreateASTVisitor {

	public static Expression create(ParseTree rc) {
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


	private static Expression progCreate(ProgContext rc){
		if(rc.start() != null)
			return create(rc.start());
		return null;
	}

	private static Expression startCreate(ParseTree rc) {
		if(rc instanceof StartPredContext)
			return create(((StartPredContext) rc).pred());
		//alias and ghost do not have evaluation
		return null;
	}

	private static Expression predCreate(ParseTree rc) {
		if(rc instanceof PredGroupContext)
			return new GroupExpression(create(((PredGroupContext)rc).pred()));
		else if(rc instanceof PredNegateContext)
			return new UnaryExpression("!", 
					create(((PredNegateContext)rc).pred()));
		else if(rc instanceof PredLogicContext)
			return new BinaryExpression(
					create(((PredLogicContext)rc).pred(0)),
					((PredLogicContext)rc).LOGOP().getText(),
					create(((PredLogicContext)rc).pred(1)));
		else if(rc instanceof IteContext)
			return new Ite(	create(((IteContext)rc).pred(0)),
							create(((IteContext)rc).pred(1)),
							create(((IteContext)rc).pred(2)));
		else
			return create(((PredExpContext)rc).exp());
		
	}

	private  static Expression expCreate(ParseTree rc)  {
		if(rc instanceof ExpGroupContext)
			return new GroupExpression(create(((ExpGroupContext)rc).exp()));
		else if(rc instanceof ExpBoolContext) {
			return new BinaryExpression(
					create(((ExpBoolContext)rc).exp(0)),
					((ExpBoolContext)rc).BOOLOP().getText(),
					create(((ExpBoolContext)rc).exp(1)));
		}else {
			ExpOperandContext eoc = (ExpOperandContext) rc;
			return create(eoc.operand());
		}
	}

	private  static Expression operandCreate(ParseTree rc){
		if(rc instanceof OpLiteralContext)
			return create(((OpLiteralContext)rc).literalExpression());
		else if(rc instanceof OpArithContext)
			return new BinaryExpression(
					create(((OpArithContext)rc).operand(0)),
					((OpArithContext)rc).ARITHOP().getText(),
					create(((OpArithContext)rc).operand(1)));
		else if(rc instanceof OpSubContext) 
			return new BinaryExpression(
					create(((OpSubContext)rc).operand(0)),
					"-",
					create(((OpSubContext)rc).operand(1)));
		else if(rc instanceof OpMinusContext)
			return new UnaryExpression("-", create(((OpMinusContext)rc).operand()));
		else if(rc instanceof OpNotContext)
			return new UnaryExpression("!", create(((OpNotContext)rc).operand()));
		else if(rc instanceof OpGroupContext)
			return new GroupExpression(create(((OpGroupContext)rc).operand()));
		
		fail("Error achieved case not covered in operandEvaluate");
		return null;
	}

	private  static Expression literalExpressionCreate(ParseTree rc){
		if(rc instanceof LitGroupContext)
			return new GroupExpression(create(((LitGroupContext)rc).literalExpression()));
		else if(rc instanceof LitContext)
			return create(((LitContext)rc).literal());
		else if(rc instanceof VarContext) {
			return new Var(((VarContext)rc).ID().getText());
		}else if(rc instanceof TargetInvocationContext) {
			//TODO Finish Invocation with Target (a.len())
			return null;
		}else {
			return create(((InvocationContext)rc).functionCall());
		}
	}
	



	private static Expression functionCallCreate(FunctionCallContext rc) {
		if(rc.ghostCall() != null) {
			GhostCallContext gc = rc.ghostCall();
			List<Expression> le = getArgs(gc.args());
			return new FunctionInvocation(gc.ID().getText(), le);
		} else {
			AliasCallContext gc = rc.aliasCall();
			List<Expression> le = getArgs(gc.args());
			return new AliasInvocation(gc.ID_UPPER().getText(), le);
		}
		
	}


	private static List<Expression> getArgs(ArgsContext args) {
		List<Expression> le = new ArrayList<>();
		if(args != null)
			for(PredContext oc : args.pred()) {
				le.add(create(oc));
			}
		return le;
	}


	private static Expression literalCreate(LiteralContext literalContext) {
		if(literalContext.BOOL() != null)
			return new LiteralBoolean(literalContext.BOOL().getText());
		else if(literalContext.STRING() != null)
			return new LiteralString(literalContext.STRING().getText());
		else if(literalContext.INT() != null)
			return new LiteralInt(literalContext.INT().getText());
		else if(literalContext.REAL() != null)
			return new LiteralReal(literalContext.REAL().getText());
		fail("Error got to unexistant literal.");
		return null;
	}


}