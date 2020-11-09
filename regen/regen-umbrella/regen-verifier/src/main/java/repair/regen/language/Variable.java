package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
@Priority(6)
@Pattern(regExp = "[a-zA-Z][a-zA-z0-9]*")
public class Variable extends Expression implements IModel {
	
	@Value
	String name;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		//System.out.println("IN VAR");
		return ctx.makeVariable(name);
	}
}