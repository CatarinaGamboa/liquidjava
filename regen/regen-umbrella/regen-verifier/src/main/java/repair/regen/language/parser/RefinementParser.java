package repair.regen.language.parser;

import java.util.Optional;

import org.modelcc.io.java.JavaModelReader;
import org.modelcc.language.metamodel.LanguageModel;
import org.modelcc.parser.Parser;
import org.modelcc.parser.ParserFactory;

import repair.regen.language.Expression;

public class RefinementParser {
	@SuppressWarnings("unchecked")
	public static Optional<Expression> parse(String code) {
		try {
			//System.out.println("Parsing: " + code);
			LanguageModel m = JavaModelReader.read(Expression.class);
			Parser<Expression> parser = ParserFactory.create(m, ParserFactory.WHITESPACE);
			Expression modeled = parser.parse(code);
			//System.out.println("modeled: " + modeled);
			return Optional.of(modeled);
		} catch (Exception e) {
			System.out.println("Could not parse: " + code);
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
