package liquidjava.errors;

import java.util.ArrayList;
import java.util.HashMap;

import liquidjava.errors.errors.LJError;
import liquidjava.errors.warnings.LJWarning;
import liquidjava.processor.context.PlacementInCode;

public class LJDiagnostics {
    private static LJDiagnostics instance;

    private ArrayList<LJError> errors;
    private ArrayList<LJWarning> warnings;
    private HashMap<String, PlacementInCode> translationMap;

    private LJDiagnostics() {
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.translationMap = new HashMap<>();
    }

    public static LJDiagnostics getInstance() {
        if (instance == null)
            instance = new LJDiagnostics();
        return instance;
    }

    public void addError(LJError error) {
        this.errors.add(error);
    }

    public void addWarning(LJWarning warning) {
        this.warnings.add(warning);
    }

    public void setTranslationMap(HashMap<String, PlacementInCode> map) {
        this.translationMap = map;
    }

    public boolean foundError() {
        return !this.errors.isEmpty();
    }

    public boolean foundWarning() {
        return !this.warnings.isEmpty();
    }

    public ArrayList<LJError> getErrors() {
        return this.errors;
    }

    public ArrayList<LJWarning> getWarnings() {
        return this.warnings;
    }

    public HashMap<String, PlacementInCode> getTranslationMap() {
        return this.translationMap;
    }

    public LJError getError() {
        return foundError() ? this.errors.get(0) : null;
    }

    public LJWarning getWarning() {
        return foundWarning() ? this.warnings.get(0) : null;
    }

    public void clear() {
        this.errors.clear();
        this.warnings.clear();
        this.translationMap.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (foundError()) {
            for (LJError error : errors) {
                sb.append(error.toString()).append("\n");
            }
        } else {
            if (foundWarning()) {
                sb.append("Warnings:\n");
                for (LJWarning warning : warnings) {
                    sb.append(warning.getMessage()).append("\n");
                }
                sb.append("Passed Verification!\n");
            }
        }
        return sb.toString();
    }
}
