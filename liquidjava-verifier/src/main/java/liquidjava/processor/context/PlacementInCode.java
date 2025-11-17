package liquidjava.processor.context;

import java.lang.annotation.Annotation;
import spoon.reflect.code.CtComment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;

public class PlacementInCode {
    private final String text;
    private final SourcePosition position;

    private PlacementInCode(String text, SourcePosition pos) {
        this.text = text;
        this.position = pos;
    }

    public String getText() {
        return text;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public static PlacementInCode createPlacement(CtElement elem) {
        CtElement elemCopy = elem.clone();
        // cleanup annotations
        if (!elem.getAnnotations().isEmpty()) {
            for (CtAnnotation<? extends Annotation> a : elem.getAnnotations()) {
                elemCopy.removeAnnotation(a);
            }
        }
        // cleanup comments
        if (!elem.getComments().isEmpty()) {
            for (CtComment a : elem.getComments()) {
                elemCopy.removeComment(a);
            }
        }
        String elemText = elemCopy.toString();
        return new PlacementInCode(elemText, elem.getPosition());
    }

    public String toString() {
        if (position.getFile() == null) {
            return "No position provided. Possibly asking for generated code";
        }
        return text + "  at:" + position.getFile().getName() + ":" + position.getLine() + ", " + position.getColumn();
    }
}
