package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Multiplicity;
import org.modelcc.Optional;

import repair.regen.language.Variable;
import repair.regen.language.symbols.GhostKeyword;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;

public class FunctionDeclaration implements IModel{
	@Optional
	GhostKeyword g;
	Type retType;
	FunctionName name;
	ParenthesisLeft lp;
    
	@Multiplicity(minimum=0,maximum=1000)
    @Optional
    MultipleTypes[] argtypes;
	
    ParenthesisRight lr;
	
	
	public Type getReturnType() {
		return retType;
	}
	public FunctionName getName() {
		return name;
	}
	public MultipleTypes[] getArgTypes() {
		return argtypes;
	}
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(g+" "+retType.toString()+" "+name.toString()+"(");
    	if(argtypes != null)
	    	for (MultipleTypes t:argtypes) {
				sb.append(t.toString()+" ");
			}
    	sb.append(")");
    	return sb.toString();
    }
	
	
}
