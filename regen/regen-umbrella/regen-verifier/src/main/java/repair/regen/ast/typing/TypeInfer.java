package repair.regen.ast.typing;
import static org.junit.Assert.fail;

import java.util.Optional;

import repair.regen.ast.AliasInvocation;
import repair.regen.ast.BinaryExpression;
import repair.regen.ast.Expression;
import repair.regen.ast.FunctionInvocation;
import repair.regen.ast.GroupExpression;
import repair.regen.ast.Ite;
import repair.regen.ast.LiteralBoolean;
import repair.regen.ast.LiteralInt;
import repair.regen.ast.LiteralReal;
import repair.regen.ast.LiteralString;
import repair.regen.ast.UnaryExpression;
import repair.regen.ast.Var;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import repair.regen.utils.Utils;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class TypeInfer {
	
	public static boolean checkCompatibleType(Expression e1, Expression e2, Context ctx, Factory factory) {
		Optional<CtTypeReference<?>> t1 = getType(ctx, factory, e1);
		Optional<CtTypeReference<?>> t2 = getType(ctx, factory, e2);
		return t1.isPresent() && t2.isPresent() && t1.get().equals(t2.get());
	}
	
	public static Optional<CtTypeReference<?>> getType(Context ctx, Factory factory, Expression e) {
		if(e instanceof LiteralString)
			return Optional.of(Utils.getType("String", factory));
		
		else if(e instanceof LiteralInt)
			return Optional.of(Utils.getType("int", factory));
		
		else if(e instanceof LiteralReal)
			return Optional.of(Utils.getType("double", factory));
		
		else if(e instanceof LiteralBoolean)
			return boolType(factory);
		
		else if(e instanceof Var)
			return varType(ctx, factory, (Var) e);
			
		else if(e instanceof UnaryExpression)
			return unaryType(ctx, factory, (UnaryExpression) e);
		
		else if(e instanceof Ite)
			return boolType(factory);
		
		else if(e instanceof BinaryExpression)
			return binaryType(ctx, factory, (BinaryExpression) e);
		
		else if(e instanceof GroupExpression)
			return getType(ctx, factory, ((GroupExpression) e).getExpression());
		
		else if(e instanceof FunctionInvocation)
			return functionType(ctx, factory, (FunctionInvocation) e);
		
		else if(e instanceof AliasInvocation)
			return boolType(factory);
		
		return Optional.empty();
	}
	

	private static Optional<CtTypeReference<?>> varType(Context ctx, Factory factory, Var v) {
		String name = v.getName();
		if(!ctx.hasVariable(name))
			return Optional.empty();
		return Optional.of(ctx.getVariableByName(name).getType());
	}

	private static Optional<CtTypeReference<?>> unaryType(Context ctx, Factory factory, UnaryExpression e) {
		if(e.getOp().equals("!"))
			return boolType(factory);
		return getType(ctx, factory, e.getExpression());
	}
	
	private static Optional<CtTypeReference<?>> binaryType(Context ctx, Factory factory, BinaryExpression e) {
		if(e.isLogicOperation())
			return boolType(factory); // &&, ||, -->
		
		else if(e.isBooleanOperation()) {// >, >=, <, <=, ==, !=
			return boolType(factory);
			
		}else if(e.isArithmeticOperation()) {
			Optional<CtTypeReference<?>> t1 = getType(ctx, factory, e.getFirstOperand()); 
			Optional<CtTypeReference<?>> t2 = getType(ctx, factory, e.getSecondOperand());
			if(!t1.isPresent() || !t2.isPresent()) return Optional.empty();
			if(t1.get().equals(t2.get()))
				return t1;
			//TODO
			fail("To implement in TypeInfer: Binary type, arithmetic with different arg types");
		}
		return null;
	}
	
	private static Optional<CtTypeReference<?>> functionType(Context ctx, Factory factory, FunctionInvocation e) {
		Optional<GhostFunction> gh = ctx.getGhosts().stream().filter(g->g.getName().equals(e.getName())).findAny();
		return gh.isPresent()? Optional.of(gh.get().getReturnType()) : Optional.empty();
	}

	private static Optional<CtTypeReference<?>> boolType(Factory factory){
		return Optional.of(Utils.getType("boolean", factory));
	}


}
