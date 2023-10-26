package liquidjava.processor.refinement_checker;

import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.reference.CtTypeReference;

public class TypeCheckingUtils {

  public static String getStringFromAnnotation(CtExpression<?> ce) {
    if (ce instanceof CtLiteral<?>) {
      CtLiteral<?> cl = (CtLiteral<?>) ce;
      CtTypeReference<?> r = ce.getType();
      if (r.getSimpleName().equals("String")) return (String) cl.getValue();

    } else if (ce instanceof CtBinaryOperator) {
      CtBinaryOperator<?> cbo = (CtBinaryOperator<?>) ce;
      String l = getStringFromAnnotation(cbo.getLeftHandOperand());
      String r = getStringFromAnnotation(cbo.getRightHandOperand());
      return l + r;
    }
    return null;
  }
}
