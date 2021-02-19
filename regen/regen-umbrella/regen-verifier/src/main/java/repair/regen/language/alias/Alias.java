package repair.regen.language.alias;

import org.modelcc.IModel;

import repair.regen.language.Expression;
import repair.regen.language.function.Type;
import repair.regen.language.symbols.BraceLeft;
import repair.regen.language.symbols.BraceRight;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.language.symbols.TypeKeyword;

public class Alias implements IModel{
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

}
