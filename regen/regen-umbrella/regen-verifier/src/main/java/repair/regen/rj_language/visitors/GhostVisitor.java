package repair.regen.rj_language.visitors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import repair.regen.utils.Pair;
import repair.regen.utils.Triple;
import rj.grammar.RJParser.ArgDeclContext;
import rj.grammar.RJParser.ArgDeclIDContext;
import rj.grammar.RJParser.GhostCallContext;
import rj.grammar.RJParser.GhostContext;

public class GhostVisitor {
	public static List<String> getGhostInvocations(ParseTree rc,  List<String> all) {
		List<String> ls = new ArrayList<>();
		getGhostsInvocations(rc, ls, all);
		return ls;
	}

	private static void getGhostsInvocations(ParseTree rc,List<String> toAdd, List<String> all) {
		if(rc instanceof GhostCallContext) {
			GhostCallContext gc = (GhostCallContext)rc;
			String ghostName = gc.ID().getText();
			for(String name:all)
				if(name.equals(ghostName))
					toAdd.add(name);	
		}
		if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				getGhostsInvocations(rc.getChild(j), toAdd, all);
			}
		}
	}
	
	
	public static Triple<String, String, List<Pair<String,String>>> getGhostDecl(ParseTree rc) {
		if(rc instanceof GhostContext) {
			GhostContext gc = (GhostContext)rc;
			String type = gc.type().getText();
			String name = gc.ID().getText();
			List<Pair<String,String>> args = getArgsDecl(gc.argDecl());
			return new Triple<String, String, List<Pair<String,String>>>(type, name, args);
		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				return getGhostDecl(rc.getChild(j));
			}
		}
		return null;
	}

	private static List<Pair<String, String>> getArgsDecl(ArgDeclContext argDecl) {
		List<Pair<String, String>> l = new ArrayList<Pair<String, String>>();
		auxGetArgsDecl(argDecl, l);
		return l;
	}

	private static void auxGetArgsDecl(ArgDeclContext argDecl, List<Pair<String, String>> l) {
		String type = argDecl.type().getText();
		String name = argDecl.ID() !=null ? argDecl.ID().getText() : "";
		l.add(new Pair<>(type, name));
		if(argDecl.argDecl() != null)
			auxGetArgsDecl(argDecl.argDecl(), l);
	}
}
