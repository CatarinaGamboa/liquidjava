package repair.regen.rj_language;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import rj.grammar.RJParser.VarContext;

public class RJSubstituteVisitor {
	TokenStreamRewriter rewriter;
	
	public RJSubstituteVisitor(TokenStreamRewriter rewriter) {
		this.rewriter = rewriter;
	}

	void subtitute(ParseTree rc, String from, String to) {
		if(rc instanceof VarContext) {
			VarContext var = (VarContext) rc;
			TerminalNode tn = var.ID();
			Token t =  tn.getSymbol();
			if(t.getText().equals(from)) {
				rewriter.replace(t, to);
			}
		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				subtitute(rc.getChild(j), from, to);
			}
		}
			
	}

}
