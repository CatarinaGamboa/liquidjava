package repair.regen.processor.constraints;

import spoon.reflect.reference.CtTypeReference;

public class VCImplication{
	String name;
	CtTypeReference<?> type;
	Constraint refinement;
	VCImplication next;
	
	public VCImplication(String name, CtTypeReference type, Constraint ref) {
		this.name = name;
		this.type = type;
		this.refinement = ref;
	}
	
	public VCImplication(Constraint ref) {
		this.refinement = ref;
	}
	
	public void setNext(VCImplication c) {
		next = c;
	}
	
	public String toString() {
		if(name!=null && type!= null) {
			String qualType = type.getQualifiedName();
			String simpleType = qualType.contains(".")?
					qualType.substring(qualType.lastIndexOf(".")+1):qualType;
			return String.format("%-20s %s => \n%s", "∀"+name+":"+ 
					simpleType+",", refinement.toString(), 
					next != null?next.toString(): "");
		}else
			return String.format("%-20s %s", "",refinement.toString());
	}
	
	public Constraint toConjunctions() {
		Constraint c = new Predicate();
		c = auxConjunction(c);
		return c;
	}
	private Constraint auxConjunction(Constraint c) {
		Constraint t = Conjunction.createConjunction(c, refinement);
		if(next == null)
			return t;
		t = next.auxConjunction(t);
		return t;
	}
	
}
