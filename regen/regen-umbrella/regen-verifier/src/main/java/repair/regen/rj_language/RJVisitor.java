package repair.regen.rj_language;

import org.antlr.v4.runtime.Token;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
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

public class RJVisitor {

	TranslatorToZ3 ctx;

	public RJVisitor(TranslatorToZ3 ctx) {
		this.ctx = ctx;
	}

	public Expr eval(RuleContext rc) throws Exception{
		if(rc instanceof ProgContext)
			return progEvaluate((ProgContext)rc);
		else if(rc instanceof StartContext)
			return startEvaluate(rc);
		else if(rc instanceof PredContext)
			return predEvaluate(rc);
		else if(rc instanceof ExpContext) 
			return expEvaluate(rc);
		else if(rc instanceof OperandContext)
			return operandEvaluate(rc);
		else if(rc instanceof LiteralExpressionContext) 
			return literalExpressionEvaluate(rc);
		else if(rc instanceof FunctionCallContext)
			return functionCallEvaluate((FunctionCallContext)rc);
		else if(rc instanceof LiteralContext)
			return literalEvaluate((LiteralContext)rc);

		return null;
	}




	private Expr progEvaluate(ProgContext rc) throws Exception {
		if(rc.isEmpty())
			return null;
		return eval(rc.start());
	}

	private Expr startEvaluate(RuleContext rc) throws Exception{
		if(rc instanceof StartPredContext)
			return eval(((StartPredContext) rc).pred());
		//alias and ghost do not have evaluation
		return null;
	}

	private Expr predEvaluate(RuleContext rc) throws Exception {
		if(rc instanceof PredGroupContext)
			return eval(((PredGroupContext)rc).pred());
		else if(rc instanceof PredNegateContext)
			return eval(((PredNegateContext)rc).pred());
		else if(rc instanceof PredLogicContext) {
			PredLogicContext plc = (PredLogicContext) rc;
			Expr e1 = eval(plc.pred(0));
			Expr e2 = eval(plc.pred(1));
			return logicOpEvaluate(plc.LOGOP(), e1, e2);
		}else if(rc instanceof IteContext) {
			IteContext ite = (IteContext) rc;
			Expr cond = eval(ite.pred(0));
			Expr then = eval(ite.pred(1));
			Expr els = eval(ite.pred(2));
			return ctx.makeIte(cond, then, els);
		}else {
			return eval(((PredExpContext) rc).exp());
		}
	}

	private Expr expEvaluate(RuleContext rc) throws Exception {
		if(rc instanceof ExpGroupContext)
			return eval(((ExpGroupContext)rc).exp());
		else if(rc instanceof ExpBoolContext) {
			ExpBoolContext ebc = (ExpBoolContext) rc;
			Expr e1 = eval(ebc.exp(0));
			Expr e2 = eval(ebc.exp(1));
			return boolOpEvaluate(ebc.BOOLOP(), e1, e2);
		}else {
			ExpOperandContext eoc = (ExpOperandContext) rc;
			return eval(eoc.operand());
		}
	}

	private Expr operandEvaluate(RuleContext rc) throws Exception{
		if(rc instanceof OpLiteralContext)
			return eval(((OpLiteralContext)rc).literalExpression());
		else if(rc instanceof OpArithContext) {
			OpArithContext oac = (OpArithContext) rc;
			Expr e1 = eval(oac.operand(0));
			Expr e2 = eval(oac.operand(1));
			return arithOpEvaluate(oac.ARITHOP(), e1, e2);
		}else if(rc instanceof OpSubContext) {
			OpSubContext osc = (OpSubContext) rc;
			Expr e1 = eval(osc.operand(0));
			Expr e2 = eval(osc.operand(1));
			return ctx.makeSub(e1, e2);
		}else if(rc instanceof OpMinusContext) {
			OpMinusContext omc = (OpMinusContext) rc;
			return ctx.makeMinus(eval(omc.operand()));
		}else {
			OpNotContext onc = (OpNotContext) rc;
			return ctx.mkNot(eval(onc.operand()));
		} 
	}

	private Expr literalExpressionEvaluate(RuleContext rc) throws Exception{
		if(rc instanceof LitGroupContext)
			return eval(((LitGroupContext)rc).literalExpression());
		else if(rc instanceof LitContext)
			return eval(((LitContext)rc).literal());
		else if(rc instanceof VarContext) {
//			((VarContext)rc)
			return varEvaluate(((VarContext)rc).ID());
		}else if(rc instanceof TargetInvocationContext) {
			//TODO Finish Invocation with Target (a.len())
			return null;
		}else {
			InvocationContext ic = (InvocationContext) rc;
			return eval(ic.functionCall());
		}
	}

	private Expr functionCallEvaluate(FunctionCallContext rc) throws Exception {
		if(rc.ghostCall() != null) {
			GhostCallContext c = rc.ghostCall();
			if(c.args() != null) {
				Expr[] ps = getExprArray(c.args());
				return ctx.makeFunctionInvocation(c.ID().getText(), ps);
			}else {
				Expr[] ps = new Expr[0];
				return ctx.makeFunctionInvocation(c.ID().getText(), ps);
			}
		}else {
			AliasCallContext c = rc.aliasCall();
			if(c.args() != null) {
				//TODO Alias Call
			}else {
				//TODO Alias Call
			}
		}
		return null;
	}


	private Expr[] getExprArray(ArgsContext args) throws Exception {
		List<PredContext> lp = args.pred();
		List<Expr> le = new ArrayList<>();
		for(PredContext pc: lp)
			le.add(eval(pc));
		return le.stream().toArray(Expr[]::new);
	}

	//------------------------Terminal Nodes ----------------------------------------
	private Expr logicOpEvaluate(TerminalNode logop, Expr e1, Expr e2) {
		String t = logop.getText();
		if(t.equals("&&"))
			return ctx.makeAnd(e1, e2);
		else if(t.equals("||"))
			return ctx.makeOr(e1, e2);
		else
			return ctx.makeImplies(e1, e2);
	}

	private Expr boolOpEvaluate(TerminalNode boolop, Expr e1, Expr e2) {
		String op = boolop.getText();
		switch (op) {
		case "==":
			return ctx.makeEquals(e1, e2);
		case "!=":
			return ctx.mkNot(ctx.makeEquals(e1, e2));
		case ">=":
			return ctx.makeGtEq(e1, e2);
		case ">":
			return ctx.makeGt(e1, e2);
		case "<=":
			return ctx.makeLtEq(e1, e2);
		default: //last case <
			return ctx.makeLt(e1, e2);
		}
	}

	private Expr arithOpEvaluate(TerminalNode arithop, Expr e1, Expr e2) {
		String op = arithop.getText();
		switch (op) {
		case "+":
			return ctx.makeAdd(e1, e2);
		case "*":
			return ctx.makeMul(e1, e2);
		case "/":
			return ctx.makeDiv(e1, e2);
		default: //last case %
			return ctx.makeMod(e1, e2);
		}
	}
	
	private Expr varEvaluate(TerminalNode id) throws Exception{
		return ctx.makeVariable(id.getText());
	}

	private Expr literalEvaluate(LiteralContext rc) {
		if(rc.BOOL() != null)
			return ctx.makeBooleanLiteral(Boolean.parseBoolean(rc.BOOL().getText()));
		else if(rc.INT() != null)
			return ctx.makeIntegerLiteral(Integer.parseInt(rc.INT().getText()));
		else if(rc.REAL() != null)
			return ctx.makeDoubleLiteral(Double.parseDouble(rc.REAL().getText()));
		else
			return ctx.makeString(rc.STRING().getText());
	}


}
