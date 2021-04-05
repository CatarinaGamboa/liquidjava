package repair.regen.rj_language.visitors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import rj.grammar.RJParser.GhostCallContext;

public class GhostInvocationsVisitor {
	public static List<String> getGhostInvocations(ParseTree rc,  List<String> all) {
		List<String> ls = new ArrayList<>();
		getGhosts(rc, ls, all);
		return ls;
	}

	private static void getGhosts(ParseTree rc,List<String> toAdd, List<String> all) {
		if(rc instanceof GhostCallContext) {
			GhostCallContext gc = (GhostCallContext)rc;
			String ghostName = gc.ID().getText();
			for(String name:all)
				if(name.equals(ghostName))
					toAdd.add(name);	
		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				getGhosts(rc.getChild(j), toAdd, all);
			}
		}
	}
}
