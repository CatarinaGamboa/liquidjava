package repair.regen.language.alias;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Value;
@Pattern(regExp = "[A-Z][a-zA-z0-9]*")
public class AliasName implements IModel{

	@Value
	String name;

	public String toString() {
		return name;
	}



}
