package liquidjava.processor.context;

import java.util.ArrayList;
import java.util.List;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.utils.Utils;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostFunction {

    private final String name;
    private final List<CtTypeReference<?>> paramTypes;
    private final CtTypeReference<?> returnType;
    private final String prefix;

    public GhostFunction(GhostDTO f, Factory factory, String prefix) {
        String klass = this.getParentClassName(prefix);
        this.name = f.name();
        this.returnType = Utils.getType(f.returnType().equals(klass) ? prefix : f.returnType(), factory);
        this.paramTypes = new ArrayList<>();
        this.prefix = prefix;
        for (String t : f.paramTypes()) {
            this.paramTypes.add(Utils.getType(t.equals(klass) ? prefix : t, factory));
        }
    }

    public GhostFunction(String name, List<String> paramTypes, CtTypeReference<?> returnType, Factory factory,
            String prefix) {
        String klass = this.getParentClassName(prefix);
        String type = returnType.toString().equals(klass) ? prefix : returnType.toString();
        this.name = name;
        this.returnType = Utils.getType(type, factory);
        this.paramTypes = new ArrayList<>();
        this.prefix = prefix;
        for (String mType : paramTypes) {
            this.paramTypes.add(Utils.getType(mType.equals(klass) ? prefix : mType, factory));
        }
    }

    protected GhostFunction(String name, List<CtTypeReference<?>> list, CtTypeReference<?> returnType, String prefix) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes = list;
        this.prefix = prefix;
    }

    public CtTypeReference<?> getReturnType() {
        return returnType;
    }

    public List<CtTypeReference<?>> getParametersTypes() {
        return paramTypes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ghost ").append(returnType.toString()).append(" ").append(name).append("(");
        for (CtTypeReference<?> t : paramTypes) {
            sb.append(t.toString()).append(" ,");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getQualifiedName() {
        return Utils.qualifyName(prefix, name);
    }

    public String getParentClassName() {
        return getParentClassName(prefix);
    }

    private String getParentClassName(String pref) {
        return Utils.getSimpleName(pref);
    }

    // Match by fully qualified name, exact simple name or by comparing the simple name of the provided identifier
    // This allows references written in a different class (different prefix) to still match
    public boolean matches(String name) {
        return this.getQualifiedName().equals(name) || this.name.equals(name)
                || this.name.equals(Utils.getSimpleName(name));
    }
}
