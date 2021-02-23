package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Optional;
import org.modelcc.Pattern;
import org.modelcc.Value;

import repair.regen.language.Variable;
import repair.regen.language.symbols.Coma;

public class MultipleTypes implements IModel{

	Type t;
	@Optional
	Variable v;
	@Optional
	Coma c;
	
	public Type getType() {
		return t;
	}
	
	public String toString() {
		return t.toString() + (c==null? "":","); 
	}
}
