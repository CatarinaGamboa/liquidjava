package liquidjava.rj_language.opt.derivation_node;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DerivationNode {

    @Override
    public String toString() {
        return prettyPrint(toJson(), "");
    }

    private String prettyPrint(Map<String, Object> json, String indent) {
        StringBuilder sb = new StringBuilder();
        String nextIndent = indent + "  ";
        sb.append("{\n");
        for (String key : json.keySet()) {
            sb.append(nextIndent + "\"" + key + "\": ");
            Object value = json.get(key);
            if (value instanceof Map) {
                sb.append(prettyPrint((Map<String, Object>) value, nextIndent));
            } else if (value instanceof String) {
                sb.append("\"" + value + "\"");
            } else {
                sb.append(value);
            }
            sb.append(",\n");
        }
        if (json.size() > 0) {
            sb.setLength(sb.length() - 2); // remove last comma
            sb.append("\n");
        }
        sb.append(indent + "}");
        return sb.toString();
    }

    public abstract Map<String, Object> toJson();

    protected Map<String, Object> baseJson() {
        return new LinkedHashMap<>();
    }
}