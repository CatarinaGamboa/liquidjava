package repair.regen.rj_language.visitors;

import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import rj.grammar.RJParser.GhostCallContext;

public class ChangeOldVisitor {
	TokenStreamRewriter rewriter;
	private final static String OLD = "old"; 
	
	public ChangeOldVisitor(TokenStreamRewriter rewriter) {
		this.rewriter = rewriter;
	}

	public void changeOldTo(ParseTree rc, String previous, String now) {
		if(rc instanceof GhostCallContext) {
			GhostCallContext gcc = (GhostCallContext) rc;
			if(gcc.ID().getText().equals(OLD)) {
				if(gcc.args().getText().equals(now)) {
					rewriter.replace(gcc.start, gcc.stop, previous);
				}
			}
		}
		if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				changeOldTo(rc.getChild(j), previous, now);
			}
		}		
	}
}
