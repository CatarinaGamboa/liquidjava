package repair.regen.processor.context;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.function.MultipleTypes;
import repair.regen.language.function.Type;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostFunction {
	
	private String name;
	private List<CtTypeReference<?>> param_types;
	private CtTypeReference<?> return_type;
	
	private String klassName;
	private int setGroup;
	private int order;
	
	public GhostFunction(FunctionDeclaration fdExp, Factory factory, String path, String klass) {
		name = fdExp.getName().toString();
		Type t = fdExp.getReturnType();
		return_type = Utils.getType(t.toString().equals(klass)? path: t.toString(), factory);
		param_types = new ArrayList<>();
		MultipleTypes[] pts = fdExp.getArgTypes();
		for(MultipleTypes mt: pts) {
			String mType = mt.getType().toString();
			param_types.add(Utils.getType(mType.equals(klass)? path : mType, factory));
		}
		this.klassName = klass;
		this.setGroup = 0;//does not have a group
	}
	
	public GhostFunction(String name,List<String> param_types, CtTypeReference<?> return_type, 
			Factory factory, String path, String klass, int group, int order) {
		this.name = name;
		this.return_type = Utils.getType(return_type.toString().equals(klass)? path: return_type.toString(), factory);
		this.param_types = new ArrayList<>();
		for (int i = 0; i < param_types.size(); i++) {
			String mType = param_types.get(i).toString();
			this.param_types.add(Utils.getType(mType.equals(klass)? path : mType, factory));
		}
		this.klassName = klass;
		this.setGroup = group;
		this.order = order;
	}

	
	public String getName() {
		return name;
	}
	
	public CtTypeReference<?> getReturnType() {
		return return_type;
	}

	public List<CtTypeReference<?>> getParametersTypes(){
		return param_types; 
	}
	

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ghost "+return_type.toString()+" "+name+"(");
		for(CtTypeReference<?> t: param_types) {
			sb.append(t.toString()+" ,");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		return sb.toString();
	}
	
	public String getInvocation(String[] paramNames) {
		if(paramNames.length != param_types.size())
			assertFalse("Error in code - GhostFunction.getInvocation",true);
		
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("(");
		for(String s :paramNames)
			sb.append(s).append(",");
		sb.deleteCharAt(sb.length()-1).append(")");
		return sb.toString();
	}
	
	public String getParentClassName() {
		return klassName;
	}
	
	public int getGroupSet() {
		return setGroup;
	}
	
	public boolean belongsToGroupSet() {
		return setGroup > 0;
	}
	
	public int getOrder() {
		return order;
	}
	
	public boolean hasOrder() {
		return order > 0;
	}

}
