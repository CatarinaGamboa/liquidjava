package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Value;

@Pattern(regExp = "[a-z][a-zA-z0-9]*")
public class FunctionName implements IModel{
	@Value
	String name;
	
	public FunctionName() {}
	public FunctionName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
