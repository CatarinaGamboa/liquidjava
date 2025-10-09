package liquidjava.smt;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FPExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.context.GhostState;
import liquidjava.processor.context.RefinedVariable;
import spoon.reflect.reference.CtTypeReference;

public class TranslatorContextToZ3 {

    static void translateVariables(Context z3, Map<String, CtTypeReference<?>> ctx,
            Map<String, Expr<?>> varTranslation) {

        for (String name : ctx.keySet())
            varTranslation.put(name, getExpr(z3, name, ctx.get(name)));

        varTranslation.put("true", z3.mkBool(true));
        varTranslation.put("false", z3.mkBool(false));
    }

    public static void storeVariablesSubtypes(Context z3, List<RefinedVariable> variables,
            Map<String, List<Expr<?>>> varSuperTypes) {
        for (RefinedVariable v : variables) {
            if (!v.getSuperTypes().isEmpty()) {
                ArrayList<Expr<?>> a = new ArrayList<>();
                for (CtTypeReference<?> ctr : v.getSuperTypes())
                    a.add(getExpr(z3, v.getName(), ctr));
                varSuperTypes.put(v.getName(), a);
            }
        }
    }

    private static Expr<?> getExpr(Context z3, String name, CtTypeReference<?> type) {
        String typeName = type.getQualifiedName();
        if (typeName.contentEquals("int"))
            return z3.mkIntConst(name);
        else if (typeName.contentEquals("short"))
            return z3.mkIntConst(name);
        else if (typeName.contentEquals("boolean"))
            return z3.mkBoolConst(name);
        else if (typeName.contentEquals("long"))
            return z3.mkRealConst(name);
        else if (typeName.contentEquals("float")) {
            return (FPExpr) z3.mkConst(name, z3.mkFPSort64());
        } else if (typeName.contentEquals("double")) {
            return (FPExpr) z3.mkConst(name, z3.mkFPSort64());
        } else if (typeName.contentEquals("int[]")) {
            return z3.mkArrayConst(name, z3.mkIntSort(), z3.mkIntSort());
        } else {
            Sort nSort = z3.mkUninterpretedSort(typeName);
            return z3.mkConst(name, nSort);
            // System.out.println("Add new type: "+typeName);
        }
    }

    static void addAlias(Context z3, List<AliasWrapper> alias, Map<String, AliasWrapper> aliasTranslation) {
        for (AliasWrapper a : alias) {
            aliasTranslation.put(a.getName(), a);
        }
    }

    public static void addGhostFunctions(Context z3, List<GhostFunction> ghosts,
            Map<String, FuncDecl<?>> funcTranslation) {
        addBuiltinFunctions(z3, funcTranslation);
        if (!ghosts.isEmpty()) {
            for (GhostFunction gh : ghosts) {
                addGhostFunction(z3, gh, funcTranslation);
            }
        }
    }

    private static void addBuiltinFunctions(Context z3, Map<String, FuncDecl<?>> funcTranslation) {
        funcTranslation.put("length", z3.mkFuncDecl("length", getSort(z3, "int[]"), getSort(z3, "int"))); // ERRRRRRRRRRRRO!!!!!!!!!!!!!
        // System.out.println("\nWorks only for int[] now! Change in future. Ignore this
        // message, it is a glorified
        // todo");
        // TODO add built-in function
        Sort[] s = Stream.of(getSort(z3, "int[]"), getSort(z3, "int"), getSort(z3, "int")).toArray(Sort[]::new);
        funcTranslation.put("addToIndex", z3.mkFuncDecl("addToIndex", s, getSort(z3, "void")));

        s = Stream.of(getSort(z3, "int[]"), getSort(z3, "int")).toArray(Sort[]::new);
        funcTranslation.put("getFromIndex", z3.mkFuncDecl("getFromIndex", s, getSort(z3, "int")));
    }

    static Sort getSort(Context z3, String sort) {
        switch (sort) {
        case "int":
            return z3.getIntSort();
        case "boolean":
            return z3.getBoolSort();
        case "long":
            return z3.getRealSort();
        case "float":
            return z3.mkFPSort32();
        case "double":
            return z3.mkFPSortDouble();
        case "int[]":
            return z3.mkArraySort(z3.mkIntSort(), z3.mkIntSort());
        case "String":
            return z3.getStringSort();
        case "void":
            return z3.mkUninterpretedSort("void");
        // case "List":return z3.mkListSort(name, elemSort)
        default:
            return z3.mkUninterpretedSort(sort);
        }
    }

    public static void addGhostStates(Context z3, List<GhostState> ghostState,
            Map<String, FuncDecl<?>> funcTranslation) {
        for (GhostState g : ghostState) {
            addGhostFunction(z3, g, funcTranslation);
            // if(g.getRefinement() != null)
            // premisesToAdd.add(g.getRefinement().getExpression());
        }
    }

    private static void addGhostFunction(Context z3, GhostFunction gh, Map<String, FuncDecl<?>> funcTranslation) {
        List<CtTypeReference<?>> paramTypes = gh.getParametersTypes();
        Sort ret = getSort(z3, gh.getReturnType().toString());
        Sort[] domain = paramTypes.stream().map(t -> t.toString()).map(t -> getSort(z3, t)).toArray(Sort[]::new);
        String name = gh.getQualifiedName();
        funcTranslation.put(name, z3.mkFuncDecl(name, domain, ret));
    }
}
