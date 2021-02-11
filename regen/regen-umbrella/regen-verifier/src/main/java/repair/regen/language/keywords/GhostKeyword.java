package repair.regen.language.keywords;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;
import org.modelcc.Value;

@Priority(value = 2)
@Pattern(regExp = "ghost")
public class GhostKeyword  implements IModel {
	@Value
	String g;
	
	public String toString() {
		return g;
	}
}
