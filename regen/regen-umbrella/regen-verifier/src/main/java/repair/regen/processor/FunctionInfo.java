package repair.regen.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.internal.core.nd.java.TypeRef;

import spoon.reflect.reference.CtTypeReference;

public class FunctionInfo {
	
	private String name;
	private List<VariableInfo> argRefinements;
	private CtTypeReference<?> type;
	private String refReturn;

	
	private Context context;
	private final String prefix = "FF";
	
	public FunctionInfo() {
		argRefinements= new ArrayList<>();
		context = Context.getInstance();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<VariableInfo> getArgRefinements() {
		return argRefinements;
	}
	public void addArgRefinements(String varName, CtTypeReference<?> type, String refinement) {
		VariableInfo v = new VariableInfo(varName, type, refinement);
		this.argRefinements.add(v);
		if(!v.hasIncognitoName()) {
			int a = context.getCounter();
			System.out.println("+++++++++++++ METHOD: "+name + ", FF"+a);
			v.setIncognitoName(prefix+a);
		}
	}
	
	public void addArgRefinements(VariableInfo vi) {
		if(!vi.hasIncognitoName()) vi.setIncognitoName(prefix+context.getCounter());
		this.argRefinements.add(vi);
	}
	
	public String getRefReturn() {
		return refReturn;
	}
	public void setRefReturn( String ref) {
		this.refReturn = ref;
	}
	public CtTypeReference<?> getType() {
		return type;
	}
	public void setType(CtTypeReference<?> type) {
		this.type = type;
	}
	
	
	public String getRenamedRefinements() {
		return getRenamedRefinements(getAllRefinements());
	}
	
	private String getRenamedRefinements(String place) {
		String update = place;
		for(VariableInfo p: argRefinements) {
			String var = p.getName();
			String newName = p.getIncognitoName();
			String newRefs = p.getRefinement().replaceAll(var, newName);
			context.addVarToContext(newName, p.getType(), newRefs);
			update = update.replaceAll(var, newName);
		}
		return update;
	}
	
	public String getRenamedReturn() {
		return getRenamedRefinements(this.refReturn);
	}

	public String getAllRefinements() {
		StringBuilder sb = new StringBuilder();
		for(VariableInfo p: argRefinements) {
			sb.append(p.getRefinement()+ " && ");
		}
		sb.append(refReturn);
		return sb.toString();
	}


	public String getRefinementsForParamIndex(int i) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i && j < argRefinements.size(); j++) {
			VariableInfo vi = argRefinements.get(i);
			sb.append(vi.getRefinement()+ " && ");
		}
		sb.append(argRefinements.get(i).getRefinement());
		return getRenamedRefinements(sb.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argRefinements == null) ? 0 : argRefinements.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((refReturn == null) ? 0 : refReturn.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionInfo other = (FunctionInfo) obj;
		if (argRefinements == null) {
			if (other.argRefinements != null)
				return false;
		} else if (!argRefinements.equals(other.argRefinements))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (refReturn == null) {
			if (other.refReturn != null)
				return false;
		} else if (!refReturn.equals(other.refReturn))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Function [name=" + name + ", argRefinements=" +
					argRefinements + ", refReturn=" + refReturn + "]";
	}
	

}
