package liquidjava.rj_language.parsing;

import java.util.Optional;
import liquidjava.processor.facade.AliasDTO;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.visitors.AliasVisitor;
import liquidjava.rj_language.visitors.CreateASTVisitor;
import liquidjava.rj_language.visitors.GhostVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class RefinementsParser {

  public static Expression createAST(String toParse) throws ParsingException {
    ParseTree pt = compile(toParse);
    return CreateASTVisitor.create(pt);
  }

  /**
   * The triple information of the ghost declaration in the order <type, name, list<type,name>>
   *
   * @param s
   * @return
   * @throws ParsingException
   */
  public static GhostDTO getGhostDeclaration(String s) throws ParsingException {
    ParseTree rc = compile(s);
    GhostDTO g = GhostVisitor.getGhostDecl(rc);
    if (g == null)
      throw new ParsingException(" The ghost should be in format <type> <name> (<parameters>)");
    return g;
  }

  public static AliasDTO getAliasDeclaration(String s) throws ParsingException {
    Optional<String> os = getErrors(s);
    if (os.isPresent()) throw new ParsingException(os.get());
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
    // parser.addParseListener(new RJListener());
    parser.addErrorListener(err);

    RuleContext rc = parser.prog();
    AliasVisitor av = new AliasVisitor(input);
    return av.getAlias(rc);
  }

  private static ParseTree compile(String toParse) throws ParsingException {
    Optional<String> s = getErrors(toParse);
    if (s.isPresent()) throw new ParsingException(s.get());

    CodePointCharStream input = CharStreams.fromString(toParse);
    RJErrorListener err = new RJErrorListener();

    RJLexer lexer = new RJLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(err);

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    RJParser parser = new RJParser(tokens);
    parser.setBuildParseTree(true);
    parser.removeErrorListeners();
    parser.addErrorListener(err);
    return parser.prog();
  }

  /**
   * First passage to check if there are syntax errors
   *
   * @param toParse
   * @return
   */
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
    // parser.addParseListener(new RJListener());
    parser.addErrorListener(err);
    parser.start(); // all consumed
    if (err.getErrors() > 0) return Optional.of(err.getMessages());
    return Optional.empty();
  }
}
