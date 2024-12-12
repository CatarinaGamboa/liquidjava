package liquidjava.processor;

import liquidjava.rj_language.Predicate;
import spoon.reflect.reference.CtTypeReference;

/**
 * @author cgamboa
 */
public class VCImplication {
    String name;
    CtTypeReference<?> type;
    Predicate refinement;
    VCImplication next;

    public VCImplication(String name, CtTypeReference<?> type, Predicate ref) {
        this.name = name;
        this.type = type;
        this.refinement = ref;
    }

    public VCImplication(Predicate ref) {
        this.refinement = ref;
    }

    public void setNext(VCImplication c) {
        next = c;
    }

    public String toString() {
        if (name != null && type != null) {
            String qualType = type.getQualifiedName();
            String simpleType = qualType.contains(".") ? qualType.substring(qualType.lastIndexOf(".") + 1) : qualType;
            return String.format(
                    "%-20s %s %s",
                    "∀" + name + ":" + simpleType + ",",
                    refinement.toString(),
                    next != null ? " => \n" + next.toString() : "");
        } else return String.format("%-20s %s", "", refinement.toString());
    }

    public Predicate toConjunctions() {
        Predicate c = new Predicate();
        if (name == null && type == null && next == null) return c;
        c = auxConjunction(c);
        return c;
    }

    private Predicate auxConjunction(Predicate c) {
        Predicate t = Predicate.createConjunction(c, refinement);
        if (next == null) return t;
        t = next.auxConjunction(t);
        return t;
    }

    public VCImplication clone() {
        VCImplication vc = new VCImplication(this.name, this.type, this.refinement.clone());
        if (this.next != null) vc.next = this.next.clone();
        return vc;
    }
}
