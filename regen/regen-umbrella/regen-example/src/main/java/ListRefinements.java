

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;

@ExternalRefinementsFor("java.util.ArrayList")
public interface ListRefinements {
	
	@Refinement("len( _ ) == 0")
	public void ArrayList();

}
