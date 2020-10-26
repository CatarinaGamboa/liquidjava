package repair.regen.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FunctionInfo {
	//TODO probably change to allow overloading
	private class Pair<K, V>{
		private K key;
		private V value;
		private Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
		private K getKey() {return key;}
		private V getValue() {return value;}
	}
	
	private String name;
	private List<Pair<String, String>> argRefinements;
	private String refReturn;
	private final String prefix = "FF";
	
	public FunctionInfo() {
		argRefinements= new ArrayList<>();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Pair<String, String>> getArgRefinements() {
		return argRefinements;
	}
	public void addArgRefinements(String varName, String refinement) {
		this.argRefinements.add(new Pair<>(varName, refinement));
	}
	public String getRefReturn() {
		return refReturn;
	}
	public void setRefReturn( String ref) {
		this.refReturn = ref;
	}
	
	public String getRenamedRefinements() {
		String update = refReturn;
		for(Pair p: argRefinements) {
			String var = (String) p.getKey();
			String newName = prefix+RefinementTypeChecker.counter++;
			//RefinementTypeChecker.addToContext(newName, t);
			update = update.replaceAll(var, newName);
		}
		return update;
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
