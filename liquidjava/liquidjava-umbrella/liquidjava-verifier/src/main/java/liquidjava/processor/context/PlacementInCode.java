package liquidjava.processor.context;

import java.lang.annotation.Annotation;

import spoon.reflect.code.CtComment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;

public class PlacementInCode {
    private String text;
    private SourcePosition position;

    private PlacementInCode(String t, SourcePosition s) {
        this.text = t;
        this.position = s;
    }

    public String getText() {
        return text;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(SourcePosition position) {
        this.position = position;
    }

    public static PlacementInCode createPlacement(CtElement elem) {
        CtElement elemCopy = elem.clone();
        // cleanup annotations
        if (elem.getAnnotations().size() > 0) {
            for (CtAnnotation<? extends Annotation> a : elem.getAnnotations()) {
                elemCopy.removeAnnotation(a);
            }
        }
        // cleanup comments
        if (elem.getComments().size() > 0) {
            for (CtComment a : elem.getComments()) {
                elemCopy.removeComment(a);
            }
        }
        String elemText = elemCopy.toString();
        return new PlacementInCode(elemText, elem.getPosition());
    }

    public String getSimplePosition() {
        if (position.getFile() == null) {
            return "No position provided. Possibly asking for generated code";
        }
        return position.getFile().getName() + ":" + position.getLine() + ", " + position.getColumn();
    }

    public String toString() {
        if (position.getFile() == null) {
            return "No position provided. Possibly asking for generated code";
        }
        return text + "  at:" + position.getFile().getName() + ":" + position.getLine() + ", " + position.getColumn();
    }

}
