package repair.regen.language.alias;

import org.modelcc.IModel;
import org.modelcc.Optional;

import repair.regen.language.Expression;
import repair.regen.language.function.Type;
import repair.regen.language.symbols.BraceLeft;
import repair.regen.language.symbols.BraceRight;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.language.symbols.TypeKeyword;

public class Alias implements IModel{
	@Optional
	TypeKeyword tk;
	AliasName name;
	
	ParenthesisLeft pl;
	Type type;
	Var var;
	ParenthesisRight rl;
	
	BraceLeft bl;
	Expression e;
	BraceRight br;
	
	
	public String toString() {
		return "type "+name+ "("+type.toString()+" "+var.toString()+")"+"{"+e.toString()+"}";
	}


	public String getName() {
		return name.toString();
	}
	
	public Type getType() {
		return type;
	}
	public Var getVar() {
		return var;
	}
	public Expression getExpression() {
		return e;
	}
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alias other = (Alias) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


}
