package liquidjava.rj_language.opt.derivation_node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class DerivationNode {

    // disable html escaping to avoid escaping characters like &, >, <, =, etc.
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}