package repair.regen.rj_language;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class RefinementsParser {

	
	public static Expr eval(String s, TranslatorToZ3 ctx) throws Exception {
		RuleContext rc = compile(s);
		RJVisitor visitor = new RJVisitor(ctx);
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
//		parser.start();
		
		if(err.getErrors() > 0) {
			throw new ParsingException(err.getMessages());
		}
					
		return parser.prog();
	}
}
