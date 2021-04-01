package repair.regen.rj_language;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class Parser {
	public static void main(String[] args) throws ParsingException {
		CodePointCharStream input;
//		String s = "((v >= 0.0) && !(!(sum (#this_1) == v)))";
//		String s = "((((( Sum (#a_21) == (sum (#a_19) + #v_20)) && (sum (#a_19) == #v_17)) && (#v_17 == 50.0)) && (#v_20 == 60.0)) && !(sum (#a_21) > 30.0))";
		String s = "(_ < 100)? (_ == 6):((_*7) == 5)";
		input = CharStreams.fromString(s);
		RJErrorListener err = new RJErrorListener();
		
		RJLexer lexer = new RJLexer(input);
//		lexer.removeErrorListeners();
		lexer.addErrorListener(err);
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
//		parser.removeErrorListeners();
		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
		parser.start();
		
		if(err.getErrors() > 0) {
			throw new ParsingException(err.getMessages());
		}
		System.out.println("No errors");
			
		//			CasualLexer lexer = new CasualLexer(input);
		//			CasualErrorsListener errlis = new CasualErrorsListener();
		//
		//			CommonTokenStream tokens = new CommonTokenStream(lexer);
		//			RefinementsParser parser = new CasualParser(tokens);
		//			System.out.println();
		//			parser.setBuildParseTree(true);
		//			parser.addErrorListener(errlis);
		//			//parser.addParseListener(new CasualListener());
		//			parser.start();
		//			//System.out.println(parser.def().getText());
		//			//parser.decl().signature().
		//			//ValidateSemantic.validate(parser.prog());
		//			return errlis.getTotalErrors();
	}
}
