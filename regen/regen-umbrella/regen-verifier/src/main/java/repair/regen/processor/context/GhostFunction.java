package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;

import repair.regen.utils.Pair;
import repair.regen.utils.Triple;
import repair.regen.utils.Utils;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostFunction {
	
	private String name;
	private List<CtTypeReference<?>> param_types;
	private CtTypeReference<?> return_type;
	
	private String klassName;
	
	public GhostFunction(Triple<String, String, List<Pair<String,String>>> f, Factory factory, String path, String klass) {
		name = f.getSecond();
		return_type = Utils.getType(f.getFist().equals(klass)? path: f.getFist(), factory);
		param_types = new ArrayList<>();
		for(Pair<String,String> p : f.getThird()) {
			param_types.add(Utils.getType(p.getFirst().equals(klass)? path : p.getFirst(), factory));
		}
	}
	
	public GhostFunction(String name, List<String> param_types, CtTypeReference<?> return_type, 
			Factory factory, String path, String klass) {
		this.name = name;
		this.return_type = Utils.getType(return_type.toString().equals(klass)? path: return_type.toString(), factory);
		this.param_types = new ArrayList<>();
		for (int i = 0; i < param_types.size(); i++) {
			String mType = param_types.get(i).toString();
			this.param_types.add(Utils.getType(mType.equals(klass)? path : mType, factory));
		}
		this.klassName = klass;
	}

	
	protected GhostFunction(String name, List<CtTypeReference<?>> list, CtTypeReference<?> return_type, String klass) {
		this.name = name;
		this.return_type = return_type;
		this.param_types = new ArrayList<>();
		this.param_types = list;
		this.klassName = klass;
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
		
	public String getParentClassName() {
		return klassName;
	}

}
