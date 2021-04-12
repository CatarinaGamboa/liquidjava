package repair.regen.rj_language;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.microsoft.z3.Expr;

import repair.regen.ast.Expression;
import repair.regen.rj_language.visitors.AliasVisitor;
import repair.regen.rj_language.visitors.BooleanTrueVisitor;
import repair.regen.rj_language.visitors.ChangeOldVisitor;
import repair.regen.rj_language.visitors.CreateASTVisitor;
import repair.regen.rj_language.visitors.EvalVisitor;
import repair.regen.rj_language.visitors.GhostVisitor;
import repair.regen.rj_language.visitors.StateVisitor;
import repair.regen.rj_language.visitors.SubstituteVisitor;
import repair.regen.rj_language.visitors.VariableVisitor;
import repair.regen.smt.TranslatorToZ3;
import repair.regen.utils.Pair;
import repair.regen.utils.Triple;
import rj.grammar.RJLexer;
import rj.grammar.RJParser;


public class RefinementsParser {
	public static void main(String[] args) throws Exception {
		//		String toParse = "int between(double x, int low, java.lang.String)";
		String rr = "Between(5, 10 + 4, yy) && yy > 10 && foo(x)";
		Expression e = createAST(rr);
		System.out.println(e);

	}


	public static Expression createAST(String toParse) throws ParsingException {
		ParseTree pt = compile(toParse);
		return CreateASTVisitor.create(pt);
	}



	//DONE IN EXP
//	public static Expr eval(String s, TranslatorToZ3 ctx) throws Exception {
//		ParseTree rc = compile(s);
//		EvalVisitor visitor = new EvalVisitor(ctx);
//		Expr e =  visitor.eval(rc);
//
//		//try ast
//		Expression ee = createAST(s); 
//		Expr e2 = ee.eval(ctx);
//
//		assert(e.equals(e2));
//
//		return e;
//	}

	//DONE IN EXP
//	public static boolean isTrue(String s) throws ParsingException {
//		ParseTree rc = compile(s);
//
//		//test ast
//		List<String> la = new ArrayList<>();
//		Expression ee = createAST(s); 
//		boolean eb = ee.isBooleanTrue();
//
//		boolean b = BooleanTrueVisitor.isTrue(rc);
//		assert(eb == b); 
//		return b;
//	}

	//DONE IN EXP
//	public static List<String> getVariableNames(String s) throws ParsingException {
//		ParseTree rc = compile(s);
//		//test ast
//		List<String> la = new ArrayList<>();
//		Expression ee = createAST(s); 
//		ee.getVariableNames(la);
//
//		List<String> l = VariableVisitor.getNames(rc);
//		assert(l.equals(la));
//
//		return l;
//	}

	//DONE IN EXP
//	public static List<String> getStateInvocations(String s, List<String> all) throws ParsingException {
//		ParseTree rc = compile(s);
//
//		//test ast
//		List<String> la = new ArrayList<>();
//		Expression ee = createAST(s); 
//		ee.getStateInvocations(la, all);
//
//		List<String> l = GhostVisitor.getGhostInvocations(rc, all);
//		assert(l.equals(la));
//
//		return l;
//	}

	//MAINTAIN
	/**
	 * The triple information of the ghost declaration in the order <type, name, list<type,name>>
	 * @param s
	 * @return
	 * @throws ParsingException
	 */
	public static Triple<String, String, List<Pair<String,String>>> getGhostDeclaration(String s) throws ParsingException{
		ParseTree rc = compile(s);
		return GhostVisitor.getGhostDecl(rc);
	}

	//MAINTAIN
	public static Triple<String, String, List<Pair<String,String>>> getAliasDeclaration(String s) throws ParsingException{
		Optional<String> os = getErrors(s);
		if(os.isPresent())
			throw new ParsingException(os.get());
		CodePointCharStream input;
		input = CharStreams.fromString(s);
		RJErrorListener err = new RJErrorListener();
		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);

		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
		//		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);

		RuleContext rc = parser.prog();
		AliasVisitor av = new AliasVisitor(input);
		return av.getAlias(rc);
	}

	public static ParseTree compile(String toParse) throws ParsingException {
		//		toParse = "x > low && x < high";
		//		toParse = "((((( Sum (#a_21) == (sum (#a_19) + #v_20)) && (sum (#a_19) == #v_17)) && (#v_17 == 50.0)) && (#v_20 == 60.0)) && !(sum (#a_21) > 30.0))";
		//		toParse = "((#i_0 == 10) && !(#i_0 > 10))";
		Optional<String> s = getErrors(toParse);
		if(s.isPresent())
			throw new ParsingException(s.get());

		CodePointCharStream input = CharStreams.fromString(toParse);
		RJErrorListener err = new RJErrorListener();

		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
		//		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
		//		parser.start(); //all consumed



		return parser.prog();
	}





	//DONE IN AST
//	public static String changeOldTo(String s, String previous, String now) throws ParsingException {
//		Optional<String> os = getErrors(s);
//		if(os.isPresent())
//			throw new ParsingException(os.get());
//
//		CodePointCharStream input;
//		input = CharStreams.fromString(s);
//		RJErrorListener err = new RJErrorListener();
//		RJLexer lexer = new RJLexer(input);
//		lexer.removeErrorListeners();
//		lexer.addErrorListener(err);
//
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
//
//		RJParser parser = new RJParser(tokens);
//		parser.setBuildParseTree(true);
//		parser.removeErrorListeners();
//		//		parser.addParseListener(new RJListener());
//		parser.addErrorListener(err);
//
//		RuleContext rc = parser.prog();
//		ChangeOldVisitor co = new ChangeOldVisitor(rewriter);
//		co.changeOldTo(rc, previous, now);
//
//		String rs = rewriter.getText();
//
//		//test ast ###########################
//		List<String> la = new ArrayList<>();
//		Expression ee = createAST(s); 
//
//		Expression params = createAST(now);
//		List<Expression> lee = new ArrayList<>();
//		lee.add(params);
//		ee.substituteFunction("old", lee, createAST(previous));
//		String astOld = ee.toString();
//
//		rs = rs.replaceAll(" ", "");
//		astOld = astOld.replaceAll(" ", "");
//		assertEquals(rs, astOld);
//
//		return rs;
//	}

	//DONE IN AST
//	public static String substitute(String s, String from, String to) throws ParsingException {
//		Optional<String> os = getErrors(s);
//		if(os.isPresent())
//			throw new ParsingException(os.get());
//
//		CodePointCharStream input;
//		input = CharStreams.fromString(s);
//		RJErrorListener err = new RJErrorListener();
//		RJLexer lexer = new RJLexer(input);
//		lexer.removeErrorListeners();
//		lexer.addErrorListener(err);
//
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
//
//		RJParser parser = new RJParser(tokens);
//		parser.setBuildParseTree(true);
//		parser.removeErrorListeners();
//		//		parser.addParseListener(new RJListener());
//		parser.addErrorListener(err);
//		
//
//		RuleContext rc = parser.prog();
//		SubstituteVisitor sv = new SubstituteVisitor(rewriter);
//		sv.subtitute(rc, from, to);
//		
//		//test ast
//		Expression ee = createAST(s); 
//		ee.substitute(from, to);
//		String es = ee.toString();
//		
//		String r  = rewriter.getText();
//		r = r.replaceAll(" ", "");
//		es = es.replaceAll(" ", "");
//		if(!r.equals(es))
//			System.out.println();
//		
////		es = es.replaceAll(" ", "");
////		r = r.replaceAll(" ", "");
//		assertEquals(es, r);
//		
//		return r;
//
//	}

	//DONE IN AST
//	public static String changeStateRefinement(String s, Map<String,String> nameRefinementMap, String[] toChange) throws Exception {
//		Optional<String> os = getErrors(s);
//		if(os.isPresent())
//			throw new ParsingException(os.get());
//		CodePointCharStream input;
//		input = CharStreams.fromString(s);
//		RJErrorListener err = new RJErrorListener();
//		RJLexer lexer = new RJLexer(input);
//		lexer.removeErrorListeners();
//		lexer.addErrorListener(err);
//
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
//
//		RJParser parser = new RJParser(tokens);
//		parser.setBuildParseTree(true);
//		parser.removeErrorListeners();
//		//		parser.addParseListener(new RJListener());
//		parser.addErrorListener(err);
//
//		RuleContext rc = parser.prog();
//		StateVisitor sv = new StateVisitor(rewriter);
//		sv.changeState(rc, nameRefinementMap, toChange);
//
//
//		//test ast ###########################
//		List<String> la = new ArrayList<>();
//		Expression ee = createAST(s); 
//		Map<String, Expression> m = new HashMap<>();
//		for(String k:nameRefinementMap.keySet())
//			m.put(k, createAST(nameRefinementMap.get(k)));
//		ee = ee.substituteState(m, toChange);
//		
//		String se = ee.toString();
//		//##########################################
//
//		String rs = rewriter.getText();  
//		
//		se = se.replaceAll(" ", "");
//		rs = rs.replaceAll(" ", "");
//		
////		if(!rs.equals(se));
////			System.out.println();
//		
//		assertEquals(rs, se);
//
//
//		return rs;
//	}



	/**
	 * 
	 * @param s
	 * @param m key:aliasName, pair<refinement, list<var names>>
	 * @return
	 * @throws Exception
	 */
	//DONE in AST
//	public static String changeAlias(String s, HashMap<String, Pair<String, List<String>>> m) throws Exception {
//		Optional<String> os = getErrors(s);
//		if(os.isPresent())
//			throw new ParsingException(os.get());
//		CodePointCharStream input;
//		input = CharStreams.fromString(s);
//		RJErrorListener err = new RJErrorListener();
//		RJLexer lexer = new RJLexer(input);
//		lexer.removeErrorListeners();
//		lexer.addErrorListener(err);
//
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//		TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
//
//		RJParser parser = new RJParser(tokens);
//		parser.setBuildParseTree(true);
//		parser.removeErrorListeners();
//		//		parser.addParseListener(new RJListener());
//		parser.addErrorListener(err);
//
//		RuleContext rc = parser.prog();
//		AliasVisitor sv = new AliasVisitor(rewriter);
//		sv.changeAlias(rc, m);
//		
//		
//		String rwt = rewriter.getText();
//
//		//########TEST ast
//		Expression r = createAST(s);
//		HashMap<String, Pair<Expression, List<Expression>>> mapAlias = new HashMap();
//		for(String aName : m.keySet()) {
//			Expression ref = createAST(m.get(aName).getFirst());
//			List<Expression> le = new ArrayList<>();
//			for(String varName : m.get(aName).getSecond()) {
//				le.add(createAST(varName));
//			}
//			mapAlias.put(aName, new Pair<Expression, List<Expression>>(ref, le));
//		}
//		
//		r = r.changeAlias(mapAlias);
//		String rr = rwt.toString();
//		rwt = rwt.replaceAll(" ", "");
//		rr = rr.replaceAll(" ", "");
//		assertEquals(rwt, rr);
//		
//		return rwt;
//	}

	private static Optional<String> getErrors(String toParse) {
		CodePointCharStream input = CharStreams.fromString(toParse);
		RJErrorListener err = new RJErrorListener();

		RJLexer lexer = new RJLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(err);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		RJParser parser = new RJParser(tokens);
		parser.setBuildParseTree(true);
		parser.removeErrorListeners();
		//		parser.addParseListener(new RJListener());
		parser.addErrorListener(err);
		parser.start(); //all consumed
		if(err.getErrors() > 0)
			return Optional.of(err.getMessages());
		return Optional.empty();

	}


}
