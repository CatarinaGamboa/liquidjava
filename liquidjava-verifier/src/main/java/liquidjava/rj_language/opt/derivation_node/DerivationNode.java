package liquidjava.rj_language.opt.derivation_node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class DerivationNode {

    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting() // remove later
        .disableHtmlEscaping() // to not escape characters like &, >, <, =, etc.
        .create();

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}