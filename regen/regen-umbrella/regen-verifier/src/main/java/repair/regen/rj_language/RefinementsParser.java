package repair.regen.rj_language;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;

import com.microsoft.z3.Expr;

import repair.regen.rj_language.visitors.EvalVisitor;
import repair.regen.rj_language.visitors.SubstituteVisitor;
import repair.regen.smt.TranslatorToZ3;
import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class RefinementsParser {
	
	public static void main(String[] args) throws Exception {
		String toParse = "((#i_0 == 10) && !(#i_0 > 10))";
		String s = substitute(toParse, "#i_0", "i_5");
		System.out.println(s);
	}

	
	public static Expr eval(String s, TranslatorToZ3 ctx) throws Exception {
		RuleContext rc = compile(s);
		EvalVisitor visitor = new EvalVisitor(ctx);
		Expr e =  visitor.eval(rc);
		return e;
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
	
}
