

import repair.regen.specification.Refinement;

public interface Automobile {
	
	void setYear(@Refinement("year >= 1672") int year);

}
