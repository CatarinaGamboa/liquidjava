package repair.regen.rj_language.visitors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import rj.grammar.RJParser.VarContext;

public class VariableVisitor {

	public static List<String> getNames(ParseTree rc) {
		List<String> ls = new ArrayList<>();
		auxGetNames(rc, ls);
		return ls;
	}

	private static void auxGetNames(ParseTree rc, List<String> ls) {
		if(rc instanceof VarContext) {
			VarContext v = (VarContext) rc;
			String s = v.ID().getText();
			if(!ls.contains(s))
				ls.add(s);
		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				auxGetNames(rc.getChild(j), ls);
			}
		}		
		
	}

}
