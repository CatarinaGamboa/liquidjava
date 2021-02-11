package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Value;

@Pattern(regExp = "[a-zA-Z][a-zA-z0-9]*")
public class FunctionName implements IModel{
	@Value
	String name;

	public String toString() {
		return name;
	}

}
