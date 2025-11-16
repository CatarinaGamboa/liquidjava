package liquidjava.utils;

import java.util.Set;

import liquidjava.utils.constants.Types;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class Utils {

    private static final Set<String> DEFAULT_NAMES = Set.of("old", "length", "addToIndex", "getFromIndex");

    public static CtTypeReference<?> getType(String type, Factory factory) {
        // TODO: complete with other types
        return switch (type) {
        case Types.INT -> factory.Type().INTEGER_PRIMITIVE;
        case Types.DOUBLE -> factory.Type().DOUBLE_PRIMITIVE;
        case Types.BOOLEAN -> factory.Type().BOOLEAN_PRIMITIVE;
        case Types.INT_LIST -> factory.createArrayReference(getType("int", factory));
        case Types.STRING -> factory.Type().STRING;
        case Types.LIST -> factory.Type().LIST;
        default -> factory.createReference(type);
        };
    }

    public static String getSimpleName(String name) {
        return name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : name;
    }

    public static String qualifyName(String prefix, String name) {
        if (DEFAULT_NAMES.contains(name))
            return name; // dont qualify
        return String.format("%s.%s", prefix, name);
    }

    public static SourcePosition getRefinementAnnotationPosition(CtElement element, String refinement) {
        return element.getAnnotations().stream().filter(a -> {
            String value = a.getValue("value").toString();
            String unquoted = value.substring(1, value.length() - 1);
            return unquoted.equals(refinement);
        }).findFirst().map(CtElement::getPosition).orElse(element.getPosition());
    }
}
