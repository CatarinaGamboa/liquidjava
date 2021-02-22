package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
import repair.regen.smt.TypeCheckError;

public abstract class Expression implements IModel {
	public abstract Expr eval(TranslatorToZ3 ctx)  throws Exception;
	
	public abstract String toString();
	
	public abstract void substituteVariable(String from, String to);
	
	public abstract void getVariableNames(List<String> l);
}
