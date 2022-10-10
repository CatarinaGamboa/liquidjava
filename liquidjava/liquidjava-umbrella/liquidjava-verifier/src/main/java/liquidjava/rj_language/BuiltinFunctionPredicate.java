package liquidjava.rj_language;

import liquidjava.errors.ErrorEmitter;
import liquidjava.rj_language.parsing.ParsingException;
import spoon.reflect.declaration.CtElement;

public class BuiltinFunctionPredicate extends Predicate {

    public BuiltinFunctionPredicate(ErrorEmitter ee, CtElement elem, String functionName, String... params)
            throws ParsingException {
        super(functionName + "(" + getFormattedParams(params) + ")", elem, ee);
    }

    public static BuiltinFunctionPredicate builtin_length(String param, CtElement elem, ErrorEmitter ee)
            throws ParsingException {
        return new BuiltinFunctionPredicate(ee, elem, "length", param);
    }

    public static BuiltinFunctionPredicate builtin_addToIndex(String array, String index, String value, CtElement elem,
            ErrorEmitter ee) throws ParsingException {
        return new BuiltinFunctionPredicate(ee, elem, "addToIndex", index, value);
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
