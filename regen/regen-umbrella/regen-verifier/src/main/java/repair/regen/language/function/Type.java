package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Optional;
import org.modelcc.Pattern;
import org.modelcc.Value;

import repair.regen.language.keywords.Coma;

@Pattern(regExp = "(int)|(bool)|([A-Z][a-zA-z0-9]*)")
public class Type implements IModel{

	@Value
	String t;
//	@Optional
//	Coma c;
	
	public String toString() {
		return t ;//+ (c==null? "":","); 
	}
}
