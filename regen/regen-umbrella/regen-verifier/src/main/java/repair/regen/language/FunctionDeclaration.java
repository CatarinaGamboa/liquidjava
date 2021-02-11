package repair.regen.language;

import org.modelcc.ID;
import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Prefix;
import org.modelcc.Suffix;
import org.modelcc.Value;

import repair.regen.language.keywords.GhostKeyword;

//@Prefix("ghost")
//@Suffix("\\)")
@Pattern(regExp = "ghost [a-zA-Z][a-zA-z0-9]*")
public class FunctionDeclaration implements IModel{

	GhostKeyword g;
	Variable name3;
	
	
}
