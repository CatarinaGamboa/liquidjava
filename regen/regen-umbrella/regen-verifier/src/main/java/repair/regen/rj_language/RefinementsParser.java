package repair.regen.rj_language;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;

import com.microsoft.z3.Expr;

import repair.regen.rj_language.visitors.BooleanTrueVisitor;
import repair.regen.rj_language.visitors.ChangeOldVisitor;
import repair.regen.rj_language.visitors.EvalVisitor;
import repair.regen.rj_language.visitors.GhostInvocationsVisitor;
import repair.regen.rj_language.visitors.StateVisitor;
import repair.regen.rj_language.visitors.SubstituteVisitor;
import repair.regen.smt.TranslatorToZ3;
import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class RefinementsParser {
	
	public static void main(String[] args) throws Exception {
		String toParse = "(f(a2) && (old(a2) == 6))";
//		String s = substitute(toParse, "#i_0", "i_5");
//		System.out.println(isTrue(toParse));
//		List<String> ls = Arrays.asList("f", "g", "h");
//		System.out.println(getGhostInvocations(toParse, ls));
		
		HashMap<String, String> m = new HashMap<>();
		m.put("green", "state_1(this) == 1");
		m.put("amber", "state_1(_) == 2");
		m.put("red", "state_1(this) == 3");
		String[] s = {"this", "_"};
		String r = changeStateRefinement("(y == 10) && (green(#s1) || amber(#s1)) && f(yy) == 5", m, s);
		System.out.println(r);
		
		
	}

	
	public static Expr eval(String s, TranslatorToZ3 ctx) throws Exception {
		RuleContext rc = compile(s);
		EvalVisitor visitor = new EvalVisitor(ctx);
		Expr e =  visitor.eval(rc);
		return e;
	}
	
	public static boolean isTrue(String s) throws ParsingException {
		RuleContext rc = compile(s);
		return BooleanTrueVisitor.isTrue(rc);
	}
	
	public static List<String> getGhostInvocations(String s, List<String> all) throws ParsingException {
		RuleContext rc = compile(s);
		return GhostInvocationsVisitor.getGhostInvocations(rc, all);
	}


	
	
	public static RuleContext compile(String toParse) throws ParsingException {
		CodePointCharStream input;
//		toParse = "((v >= 0.0) && !(!(sum (#this_1) == v)))";
//		toParse = "((((( Sum (#a_21) == (sum (#a_19) + #v_20)) && (sum (#a_19) == #v_17)) && (#v_17 == 50.0)) && (#v_20 == 60.0)) && !(sum (#a_21) > 30.0))";
//		toParse = "((#i_0 == 10) && !(#i_0 > 10))";
		input = CharStreams.fromString(toParse);
		RJErrorListener err = new RJErrorListener();
		
		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
//		parser.start(); //all consumed

		if(err.getErrors() > 0) {
			throw new ParsingException(err.getMessages());
		}
		return parser.prog();
	}
	
	public static String changeOldTo(String s, String previous, String now) throws ParsingException {
		CodePointCharStream input;
		input = CharStreams.fromString(s);
		RJErrorListener err = new RJErrorListener();
		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
		
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
//		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
		
		RuleContext rc = parser.prog();
		ChangeOldVisitor co = new ChangeOldVisitor(rewriter);
		co.changeOldTo(rc, previous, now);
		return rewriter.getText();
	}
	
	public static String substitute(String s, String from, String to) throws Exception {
		CodePointCharStream input;
		input = CharStreams.fromString(s);
		RJErrorListener err = new RJErrorListener();
		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
		
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
//		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
		
		RuleContext rc = parser.prog();
		SubstituteVisitor sv = new SubstituteVisitor(rewriter);
		sv.subtitute(rc, from, to);
		
		return rewriter.getText();
		
	}
	
	public static String changeStateRefinement(String s, Map<String,String> nameRefinementMap, String[] toChange) throws Exception {
		CodePointCharStream input;
		input = CharStreams.fromString(s);
		RJErrorListener err = new RJErrorListener();
		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
		
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
//		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
		
		RuleContext rc = parser.prog();
		StateVisitor sv = new StateVisitor(rewriter);
		sv.changeState(rc, nameRefinementMap, toChange);
		
		return rewriter.getText();
		
	}
	
	
	
}
