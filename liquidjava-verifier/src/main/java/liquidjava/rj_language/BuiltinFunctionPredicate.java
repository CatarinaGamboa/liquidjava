package liquidjava.rj_language;

import liquidjava.diagnostics.errors.LJError;
import spoon.reflect.declaration.CtElement;

public class BuiltinFunctionPredicate extends Predicate {

    public BuiltinFunctionPredicate(CtElement elem, String functionName, String... params) throws LJError {
        super(functionName + "(" + getFormattedParams(params) + ")", elem);
    }

    public static BuiltinFunctionPredicate length(String param, CtElement elem) throws LJError {
        return new BuiltinFunctionPredicate(elem, "length", param);
    }

    public static BuiltinFunctionPredicate addToIndex(String array, String index, String value, CtElement elem)
            throws LJError {
        return new BuiltinFunctionPredicate(elem, "addToIndex", index, value);
    }

    private static String getFormattedParams(String... params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(params[i]);
            if (i < params.length - 1)
                sb.append(", ");
        }
        return sb.toString();
    }
}
