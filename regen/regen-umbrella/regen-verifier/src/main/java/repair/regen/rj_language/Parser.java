package repair.regen.rj_language;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class Parser {
	public static void main(String[] args) {
		CodePointCharStream input;
		String s = "_ == 30 ||| _ == 90";
		input = CharStreams.fromString(s);
		RJLexer lexer = new RJLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.start();
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
