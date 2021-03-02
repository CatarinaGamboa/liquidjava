package repair.regen.processor.constraints;

import repair.regen.language.BinaryExpression;
import repair.regen.language.ExpressionGroup;
import repair.regen.language.operators.AdditionOperator;
import repair.regen.language.operators.AndOperator;
import repair.regen.language.operators.BinaryOperator;
import repair.regen.language.operators.DivisionOperator;
import repair.regen.language.operators.EqualsOperator;
import repair.regen.language.operators.GreaterOrEqualOperator;
import repair.regen.language.operators.GreaterThanOperator;
import repair.regen.language.operators.LessOrEqualOperator;
import repair.regen.language.operators.LessThanOperator;
import repair.regen.language.operators.ModOperator;
import repair.regen.language.operators.MultiplicationOperator;
import repair.regen.language.operators.NotEqualOperator;
import repair.regen.language.operators.OrOperator;
import repair.regen.language.operators.SubtractionOperator;

public class OperationPredicate extends Predicate{
	
	public OperationPredicate(Constraint c1, String op, Constraint c2) {
		super();
		if(c1 != null && c2 != null && op != null)
			setExpression(new ExpressionGroup(new BinaryExpression(c1.getExpression(), 
					operationFromString(op), c2.getExpression())));
		else
			System.out.println("Something to implement later on Operation Predicate!");
	}
	
	private BinaryOperator operationFromString(String s) {
		switch (s) {
		case "+": return new AdditionOperator();
		case "-": return new SubtractionOperator();
		case "*": return new MultiplicationOperator();
		case "/": return new DivisionOperator();
		case "%": return new ModOperator();
		
		case "&&": return new AndOperator();
		case "||": return new OrOperator();

		case "==": return new EqualsOperator();
		case "!=": return new NotEqualOperator();
		case ">=": return new GreaterOrEqualOperator();
		case ">":  return new GreaterThanOperator();
		case "<=": return new LessOrEqualOperator();
		case "<":  return new LessThanOperator();	
		default:
			System.out.println("ERROR in creation of OperationPredicate");
			return null;
		}
	}

}

