package repair.regen.rj_language.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

import repair.regen.processor.facade.AliasDTO;
import repair.regen.rj_language.ParsingException;
import repair.regen.utils.Pair;
import rj.grammar.RJParser.AliasContext;
import rj.grammar.RJParser.ArgDeclIDContext;
import rj.grammar.RJParser.PredContext;

public class AliasVisitor {
    TokenStreamRewriter rewriter;
    CodePointCharStream input;

    public AliasVisitor(CodePointCharStream input) {
        this.input = input;
    }

    /**
     * Gets information about the alias
     *
     * @param rc
     *
     * @return
     *
     * @throws ParsingException
     */
    public AliasDTO getAlias(ParseTree rc) throws ParsingException {
        if (rc instanceof AliasContext) {
            AliasContext ac = (AliasContext) rc;
            String name = ac.ID_UPPER().getText();
            String ref = getText(ac.pred());
            List<Pair<String, String>> args = getArgsDecl(ac.argDeclID());

            List<String> varNames = args.stream().map(p -> p.getSecond()).collect(Collectors.toList());
            List<String> varTypes = args.stream().map(p -> p.getFirst()).collect(Collectors.toList());
            return new AliasDTO(name, varTypes, varNames, ref);

        } else if (rc.getChildCount() > 0) {
            int i = rc.getChildCount();
            for (int j = 0; j < i; j++) {
                return getAlias(rc.getChild(j));
            }
        }
        return null;
    }

    /**
     * Returns text with whitespaces
     *
     * @param pred
     *
     * @return
     */
    private String getText(PredContext pred) {
        int a = pred.start.getStartIndex();
        int b = pred.stop.getStopIndex();
        Interval interval = new Interval(a, b);
        return input.getText(interval);
    }

    private List<Pair<String, String>> getArgsDecl(ArgDeclIDContext argDeclID) {
        List<Pair<String, String>> l = new ArrayList<Pair<String, String>>();
        auxGetArgsDecl(argDeclID, l);
        return l;
    }

    private void auxGetArgsDecl(ArgDeclIDContext argDeclID, List<Pair<String, String>> l) {
        String type = argDeclID.type().getText();
        String name = argDeclID.ID().getText();
        l.add(new Pair<>(type, name));
        if (argDeclID.argDeclID() != null)
            auxGetArgsDecl(argDeclID.argDeclID(), l);
    }

}
