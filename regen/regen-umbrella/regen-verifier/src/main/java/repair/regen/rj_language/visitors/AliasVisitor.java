package repair.regen.rj_language.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import repair.regen.rj_language.RefinementsParser;
import repair.regen.utils.Pair;
import repair.regen.utils.Triple;
import rj.grammar.RJParser.AliasCallContext;
import rj.grammar.RJParser.AliasContext;
import rj.grammar.RJParser.ArgDeclIDContext;
import rj.grammar.RJParser.ArgsContext;
import rj.grammar.RJParser.PredContext;

public class AliasVisitor {
	TokenStreamRewriter rewriter;
	
	public AliasVisitor(TokenStreamRewriter rewriter) {
		this.rewriter = rewriter;
	}

	public void changeAlias(ParseTree rc,  HashMap<String, Pair<String, List<String>>> map) throws Exception {
		if(rc instanceof AliasCallContext) {
			AliasCallContext acc = (AliasCallContext) rc;
			String name = acc.ID_UPPER().getText();
			if(map.containsKey(name)) {
				Pair<String, List<String>> p = map.get(name);
				String refinement = p.getFirst();
				List<String> args = p.getSecond();
				List<String> calledArgs = getArgsText(acc.args());
				if(args.size() != calledArgs.size())
					throw new Exception("Invocation with wrong number of arguments:\nInvocation:"
				+acc.getText() + "\nAlias "+name+" expecting "+args.size()+" arguments");
				
				String newRef = substituteInRefinement(refinement, args, calledArgs);
				rewriter.replace(acc.start, acc.stop, "("+newRef+")");
				
			}
		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				changeAlias(rc.getChild(j), map);
			}
		}		
	}

	private List<String> getArgsText(ArgsContext args) {
		List<String> ls = new ArrayList<>();
		for(PredContext p: args.pred())
			ls.add(p.getText());
		return ls;
	}
	

	private String substituteInRefinement(String refinement, List<String> args, List<String> calledArgs) throws Exception {
		String r = refinement;
		for (int i = 0; i < args.size(); i++)
			r = RefinementsParser.substitute(r, args.get(i), "("+calledArgs.get(i)+")");
		return r;
	}

	public static Triple<String, String,List<Pair<String,String>>> getAlias(ParseTree rc) {
		if(rc instanceof AliasContext) {
			AliasContext ac = (AliasContext) rc;
			String name = ac.ID_UPPER().getText();
			String ref = ac.pred().getText();
			
			List<Pair<String,String>> args = getArgsDecl(ac.argDeclID());
			return new Triple<String, String, List<Pair<String,String>>>(name, ref, args);
			
		}else if (rc.getChildCount() > 0) {
			int i = rc.getChildCount();
			for (int j = 0; j < i; j++) {
				return getAlias(rc.getChild(j));
			}
		}
		return null;
	}

	private static List<Pair<String, String>> getArgsDecl(ArgDeclIDContext argDeclID) {
		List<Pair<String, String>> l = new ArrayList<Pair<String, String>>();
		auxGetArgsDecl(argDeclID, l);
		return l;
	}

	private static void auxGetArgsDecl(ArgDeclIDContext argDeclID, List<Pair<String, String>> l) {
		String type = argDeclID.type().getText();
		String name = argDeclID.ID().getText();
		l.add(new Pair<>(type, name));
		if(argDeclID.argDeclID() != null)
			auxGetArgsDecl(argDeclID.argDeclID(), l);
	}


}
