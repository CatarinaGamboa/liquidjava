package regen.test.project;
import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;

@ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements <E>{
	
	@RefinementPredicate("int size(ArrayList l)")
	@Refinement("size(this) == 0")
	public void ArrayList();
	
	@Refinement("size(this) == (size(old(this)) + 1)")
	public void add(E e);
	
	@Refinement("size(_) == size(this)")
	public Object clone();
	

}
