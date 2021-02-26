package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
@Priority(6)
@Pattern(regExp = "(#?[a-zA-Z][a-zA-z0-9#]*)|(_)")
public class Variable extends Expression implements IModel {
	
	@Value
	String name;

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		//System.out.println("IN VAR");
		return ctx.makeVariable(name);
	}
	

	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void changeName(String name) {
		this.name = name; 
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		//End leaf
		if(this.name.equals(from))
			changeName(to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		if(!l.contains(name))
			l.add(name);
	}
	
	
}