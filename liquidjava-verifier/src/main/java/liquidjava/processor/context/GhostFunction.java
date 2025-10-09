package liquidjava.processor.context;

import java.util.ArrayList;
import java.util.List;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.utils.Utils;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostFunction {

    private String name;
    private List<CtTypeReference<?>> param_types;
    private CtTypeReference<?> return_type;
    private String prefix;

    public GhostFunction(GhostDTO f, Factory factory, String prefix) {
        String klass = this.getParentClassName(prefix);
        this.name = f.getName();
        this.return_type = Utils.getType(f.getReturn_type().equals(klass) ? prefix : f.getReturn_type(), factory);
        this.param_types = new ArrayList<>();
        this.prefix = prefix;
        for (String t : f.getParam_types()) {
            this.param_types.add(Utils.getType(t.equals(klass) ? prefix : t, factory));
        }
    }

    public GhostFunction(String name, List<String> param_types, CtTypeReference<?> return_type, Factory factory,
            String prefix) {
        String klass = this.getParentClassName(prefix);
        String type = return_type.toString().equals(klass) ? prefix : return_type.toString();
        this.name = name;
        this.return_type = Utils.getType(type, factory);
        this.param_types = new ArrayList<>();
        this.prefix = prefix;
        for (int i = 0; i < param_types.size(); i++) {
            String mType = param_types.get(i).toString();
            this.param_types.add(Utils.getType(mType.equals(klass) ? prefix : mType, factory));
        }
    }

    protected GhostFunction(String name, List<CtTypeReference<?>> list, CtTypeReference<?> return_type, String prefix) {
        this.name = name;
        this.return_type = return_type;
        this.param_types = new ArrayList<>();
        this.param_types = list;
        this.prefix = prefix;
    }

    public CtTypeReference<?> getReturnType() {
        return return_type;
    }

    public List<CtTypeReference<?>> getParametersTypes() {
        return param_types;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ghost " + return_type.toString() + " " + name + "(");
        for (CtTypeReference<?> t : param_types) {
            sb.append(t.toString() + " ,");
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
