package liquidjava.rj_language.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.utils.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import rj.grammar.RJParser.ArgDeclContext;
import rj.grammar.RJParser.GhostContext;

public class GhostVisitor {

    public static GhostDTO getGhostDecl(ParseTree rc) {
        if (rc instanceof GhostContext) {
            GhostContext gc = (GhostContext) rc;
            String type = gc.type().getText();
            String name = gc.ID().getText();
            List<Pair<String, String>> args = getArgsDecl(gc.argDecl());
            List<String> ls = args.stream().map(m -> m.getFirst()).collect(Collectors.toList());
            if (ls == null) ls = new ArrayList<>();
            return new GhostDTO(name, ls, type);
            // return new Triple<String, String, List<Pair<String,String>>>(type, name, args);
        } else if (rc.getChildCount() > 0) {
            int i = rc.getChildCount();
            if (i > 0) return getGhostDecl(rc.getChild(0));
        }
        return null;
    }

    private static List<Pair<String, String>> getArgsDecl(ArgDeclContext argDecl) {
        List<Pair<String, String>> l = new ArrayList<Pair<String, String>>();
        if (argDecl != null) auxGetArgsDecl(argDecl, l);
        return l;
    }

    private static void auxGetArgsDecl(ArgDeclContext argDecl, List<Pair<String, String>> l) {
        String type = argDecl.type().getText();
        String name = argDecl.ID() != null ? argDecl.ID().getText() : "";
        l.add(new Pair<>(type, name));
        if (argDecl.argDecl() != null) auxGetArgsDecl(argDecl.argDecl(), l);
    }
}
