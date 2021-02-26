package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Priority(10)
public class RealLiteral extends LiteralExpression implements IModel {
	@Value
	double value;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		if(value == Math.floor(value) && !Double.isInfinite(value)) {
			//Integer came to Real by mistake
			IntegerLiteral i = new IntegerLiteral();
			i.setValue((int)value);
			return i.eval(ctx);
		}
		return ctx.makeDoubleLiteral(value);
	}
	
	@Override
	public Expr beforeEval(TranslatorToZ3 ctx) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		//End leaf - real literal
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		//End leaf - real literal
	}
	
}
