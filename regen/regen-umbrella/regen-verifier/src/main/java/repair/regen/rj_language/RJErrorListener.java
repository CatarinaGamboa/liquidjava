package repair.regen.rj_language;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class RJErrorListener implements ANTLRErrorListener {

	int errors = 0;
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		// TODO Auto-generated method stub
		errors++;
		
	}

	@Override
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
			BitSet ambigAlts, ATNConfigSet configs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
			BitSet conflictingAlts, ATNConfigSet configs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
			ATNConfigSet configs) {
		// TODO Auto-generated method stub

	}
	
	public int getErrors() {
		return errors;
	}

}
