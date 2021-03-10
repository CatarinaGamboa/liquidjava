import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;

@ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements <E>{
	
	@RefinementPredicate("int lengthA(ArrayList l)")
	@Refinement("lengthA(this) == 0")
	public void ArrayList();
	
	@Refinement("lengthA(this) == (lengthA(old(this)) + 1)")//?
	public void add(E e);
	
	@Refinement("lengthA(_) == lengthA(this)")
	public Object clone();
	

}
