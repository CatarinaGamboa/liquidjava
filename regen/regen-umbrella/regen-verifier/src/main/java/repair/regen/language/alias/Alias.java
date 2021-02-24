package repair.regen.language.alias;

import java.util.ArrayList;
import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Optional;

import repair.regen.language.Expression;
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
	Parameter p;
	ParenthesisRight rl;
	
	BraceLeft bl;
	Expression e;
	BraceRight br;
	
	
	public String toString() {
		return "type "+name+ "("+p.toString()+")"+"{"+e.toString()+"}";
	}


	public String getName() {
		return name.toString();
	}

	public Expression getExpression() {
		return e;
	}
	
	public List<String> getVariableNames(){
		List<String> l  = new ArrayList<>();
		p.getVariableNames(l);
		return l;
	}
	
	public List<String> getTypesNames(){
		List<String> l  = new ArrayList<>();
		p.getTypesNames(l);
		return l;
	}



}
