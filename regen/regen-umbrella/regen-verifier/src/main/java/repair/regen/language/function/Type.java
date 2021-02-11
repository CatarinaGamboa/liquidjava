package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Value;

@Pattern(regExp = "(int)|(bool)|([A-Z][a-zA-z0-9]*)")
public class Type implements IModel{
	
	@Value
	String type;
	
	public String toString() {
		return type.toString(); 
	}

}
