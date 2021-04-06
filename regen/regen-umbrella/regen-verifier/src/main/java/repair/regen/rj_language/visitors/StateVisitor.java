package repair.regen.rj_language.visitors;

import java.util.Map;

import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import repair.regen.rj_language.RefinementsParser;
import rj.grammar.RJParser.ArgsContext;
import rj.grammar.RJParser.GhostCallContext;
import rj.grammar.RJParser.VarContext;

public class StateVisitor {
	TokenStreamRewriter rewriter;

	public StateVisitor(TokenStreamRewriter rewriter) {
		this.rewriter = rewriter;
	}

	public void changeState(ParseTree rc, Map<String, String> nameRefinementMap, String[] toChange) throws Exception {
		if(rc instanceof GhostCallContext) {
			GhostCallContext gc = (GhostCallContext)rc;
			String fname = gc.ID().getText(); 
			ArgsContext a = gc.args();
			if(a.pred().size() == 1 && nameRefinementMap.containsKey(fname)) {
				VarContext var = getVar(a.pred(0));
				String varName = var.ID().getText();
				String refinement = nameRefinementMap.get(fname);
				for(String s: toChange)
					refinement = RefinementsParser.substitute(refinement, s, varName);
				rewriter.replace(gc.start, gc.stop, "("+refinement+")");
			}

		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				changeState(rc.getChild(j), nameRefinementMap, toChange);
			}
		}

	}

	private VarContext getVar(ParseTree rc) {
		if(rc instanceof VarContext)
			return (VarContext)rc;
		else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++)
				return getVar(rc.getChild(j));
		} 
		return null;
	}


}
