package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.symbols.Dot;
import repair.regen.language.symbols.ThisKeyword;
import repair.regen.smt.TranslatorToZ3;

//public class ClassAttribute extends Expression implements IModel {
//	ThisKeyword t;
//	Dot d;
//	Variable var;
//
//	@Override
//	public Expr eval(TranslatorToZ3 ctx) throws Exception {
//		return var.eval(ctx);
//	}
//
//	@Override
//	public String toString() {
//		return "type."+var.toString();
//	}
//
//	@Override
//	public void substituteVariable(String from, String to) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void getVariableNames(List<String> l) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
