package liquidjava.diagnostics;

import java.net.URI;
import java.util.HashMap;
import liquidjava.processor.context.PlacementInCode;
import spoon.reflect.cu.SourcePosition;

public class ErrorEmitter {

    private String titleMessage;
    private String fullMessage;
    private URI filePath;
    private ErrorPosition position;
    private int errorStatus;
    private HashMap<String, PlacementInCode> map;

    public ErrorEmitter() {
    }

    public void addError(String titleMessage, String msg, SourcePosition p, int errorStatus,
            HashMap<String, PlacementInCode> map) {
        this.titleMessage = titleMessage;
        fullMessage = msg;
        try {
            position = new ErrorPosition(p.getLine(), p.getColumn(), p.getEndLine(), p.getEndColumn());
            filePath = p.getFile().toURI();
        } catch (Exception ignored) {
            fullMessage = "Seems like this error is created in generated part of source code, so no precise"
                    + " position is provided. " + fullMessage;
            position = null;
            filePath = null;
        }
        this.errorStatus = errorStatus;
        this.map = map;
    }

    public void addError(String titleMessage, String msg, SourcePosition p, int errorStatus) {
        this.titleMessage = titleMessage;
        fullMessage = msg;
        try {
            position = new ErrorPosition(p.getLine(), p.getColumn(), p.getEndLine(), p.getEndColumn());
            filePath = p.getFile().toURI();
        } catch (Exception ignored) {
            fullMessage = "Seems like this error is created in generated part of source code, so no precise"
                    + " position is provided. " + fullMessage;
            position = null;
            filePath = null;
        }
        this.errorStatus = errorStatus;
    }

    public void addError(String titleMessage, String msg, int errorStatus) {
        this.titleMessage = titleMessage;
        fullMessage = msg;
        this.errorStatus = errorStatus;
    }

    public boolean foundError() {
        return fullMessage != null;
    }

    public String getTitleMessage() {
        return titleMessage;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public URI getFilePath() {
        return filePath;
    }

    public void reset() {
        fullMessage = null;
        position = null;
        errorStatus = 0;
        map = null;
    }

    public ErrorPosition getPosition() {
        return position;
    }

    public int getErrorStatus() {
        return errorStatus;
    }

    public HashMap<String, PlacementInCode> getVCMap() {
        return map;
    }
}
