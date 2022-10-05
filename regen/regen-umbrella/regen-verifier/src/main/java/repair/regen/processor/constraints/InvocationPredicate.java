package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.List;

import repair.regen.errors.ErrorEmitter;
import repair.regen.rj_language.ast.Expression;
import repair.regen.rj_language.ast.FunctionInvocation;

public class InvocationPredicate extends Predicate {

    public InvocationPredicate(ErrorEmitter ee, String name, String... params) {
        List<Expression> le = new ArrayList<>();
        for (String s : params) {
            le.add(innerParse(s, ee));
        }
        exp = new FunctionInvocation(name, le);
    }

    public InvocationPredicate(ErrorEmitter ee, String name, String s) {
        List<Expression> le = new ArrayList<>();
        le.add(innerParse(s, ee));
        exp = new FunctionInvocation(name, le);
    }

    public InvocationPredicate(String name, Expression expression) {
        List<Expression> le = new ArrayList<>();
        le.add(expression);
        exp = new FunctionInvocation(name, le);
    }
}
