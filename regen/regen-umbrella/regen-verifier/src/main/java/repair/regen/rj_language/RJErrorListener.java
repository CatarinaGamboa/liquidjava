package repair.regen.rj_language;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class RJErrorListener implements ANTLRErrorListener {

	private int errors;
	public List<String> msgs;
	
	public RJErrorListener() {
		super();
		errors = 0;
		msgs = new ArrayList<String>();
	}
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e){
		// TODO Auto-generated method stub
		errors++;
		msgs.add("Error in "+ msg + ", in the position "+charPositionInLine);
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
	
	public String getMessages() {
		StringBuilder sb = new StringBuilder();
		String pl = errors == 1? "":"s";
		sb.append("Found ").append(errors).append(" error"+pl).append(", with the message"+pl+":\n");
		for(String s: msgs)
			sb.append("* "+s+"\n");
		return sb.toString();
		
	}

}
