package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Multiplicity;
import org.modelcc.Optional;

import repair.regen.language.Variable;
import repair.regen.language.keywords.GhostKeyword;
import repair.regen.language.keywords.ParenthesisLeft;
import repair.regen.language.keywords.ParenthesisRight;

public class FunctionDeclaration implements IModel{

	GhostKeyword g;
	Type retType;
	Variable name;
	ParenthesisLeft lp;
    
	@Multiplicity(minimum=0,maximum=1000)
    @Optional
    Type[] argtypes;
	
    ParenthesisRight lr;
	
	
	public Type getReturnType() {
		return retType;
	}
	public Variable getName() {
		return name;
	}
	public Type[] getArgTypes() {
		return argtypes;
	}
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(g+" "+retType.toString()+" "+name.toString()+"(");
    	if(argtypes != null)
	    	for (Type t:argtypes) {
				sb.append(t+" ");
			}
    	sb.append(")");
    	return sb.toString();
    }
	
	
}
