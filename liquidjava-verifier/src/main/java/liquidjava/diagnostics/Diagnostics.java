package liquidjava.diagnostics;

import java.util.ArrayList;
import liquidjava.diagnostics.errors.LJError;
import liquidjava.diagnostics.warnings.LJWarning;

/**
 * Singleton class to store diagnostics (errors and warnings) during the verification process
 * 
 * @see LJError
 * @see LJWarning
 */
public class Diagnostics {
    public static final Diagnostics diagnostics = new Diagnostics();

    private ArrayList<LJError> errors;
    private ArrayList<LJWarning> warnings;

    private Diagnostics() {
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

    public void add(LJError error) {
        if (!this.errors.contains(error))
            this.errors.add(error);
    }

    public void add(LJWarning warning) {
        if (!this.warnings.contains(warning))
            this.warnings.add(warning);
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

    public LJError getError() {
        return foundError() ? this.errors.get(0) : null;
    }

    public LJWarning getWarning() {
        return foundWarning() ? this.warnings.get(0) : null;
    }

    public void clear() {
        this.errors.clear();
        this.warnings.clear();
    }

    public String getErrorOutput() {
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

    public String getWarningOutput() {
        StringBuilder sb = new StringBuilder();
        if (foundWarning()) {
            for (LJWarning warning : warnings) {
                sb.append(warning.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
