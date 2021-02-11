package repair.regen.language.operators;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;

@Priority(value=3)
@Pattern(regExp = "\\?")
public class IfThenOperator implements IModel{
	
	public String toString() {
		return "?";
	}
}
