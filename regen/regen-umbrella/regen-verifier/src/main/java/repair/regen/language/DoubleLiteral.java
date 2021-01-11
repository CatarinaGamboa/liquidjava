package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
@Priority(5)
public class DoubleLiteral  /*extends LiteralExpression implements IModel*/ {
//	@Value
//	double value;
//
//	@Override
//	public Expr eval(TranslatorToZ3 ctx) {
//		//return ctx.makeIntegerLiteral((int)val);
//		System.out.println("Was going to double with value "+value);
//		if(value == Math.floor(value) && !Double.isInfinite(value)) {
//			IntegerLiteral i = new IntegerLiteral();
//			i.setValue((int)value);
//			System.out.println("come to int");
//			return i.eval(ctx);
//		}
//		System.out.println("come to double");
//		return ctx.makeDoubleLiteral(value);
//	}
}
