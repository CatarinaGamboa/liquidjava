package repair.regen.language.alias;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Value;
@Pattern(regExp = "[a-z][a-zA-z0-9]*")
public class Var implements IModel{
	
	@Value
	String s;
	
	public String toString() {
		return s;
	}

}
