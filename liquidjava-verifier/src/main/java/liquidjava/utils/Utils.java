package liquidjava.utils;

import java.util.Set;

import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class Utils {

    public static final String AND = "&&";
    public static final String OR = "||";

    public static final String EQ = "==";
    public static final String NEQ = "!=";
    public static final String GT = ">";
    public static final String GE = ">=";
    public static final String LT = "<";
    public static final String LE = "<=";

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MUL = "*";
    public static final String DIV = "/";
    public static final String MOD = "%";

    public static final String WILD_VAR = "_";
    public static final String OLD = "old";

    public static final String INT = "int";
    public static final String DOUBLE = "double";
    public static final String STRING = "String";
    public static final String BOOLEAN = "boolean";
    public static final String INT_LIST = "int[]";
    public static final String LIST = "List";
    public static final String SHORT = "short";
    public static final String LONG = "long";
    public static final String FLOAT = "float";

    private static final Set<String> defaultNames = Set.of("old", "length", "addToIndex", "getFromIndex");

    public static CtTypeReference<?> getType(String type, Factory factory) {
        // TODO complete
        switch (type) {
        case INT:
            return factory.Type().INTEGER_PRIMITIVE;
        case DOUBLE:
            return factory.Type().DOUBLE_PRIMITIVE;
        case BOOLEAN:
            return factory.Type().BOOLEAN_PRIMITIVE;
        case INT_LIST:
            return factory.createArrayReference(getType("int", factory));
        case STRING:
            return factory.Type().STRING;
        case LIST:
            return factory.Type().LIST;
        default:
            // return factory.Type().OBJECT;
            return factory.createReference(type);
        }
    }

    public static String getSimpleName(String name) {
        return name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : name;
    }

    public static String qualifyName(String prefix, String name) {
        if (defaultNames.contains(name))
            return name;
        return String.format("%s.%s", prefix, name);
    }
}
