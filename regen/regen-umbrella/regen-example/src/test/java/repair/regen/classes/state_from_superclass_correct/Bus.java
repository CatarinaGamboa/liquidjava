package repair.regen.classes.state_from_superclass_correct;

import repair.regen.specification.StateRefinement;

public class Bus extends Car{

	@StateRefinement(to = "close(this)")
	public Bus() {
		
	}
	
	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}



	

}
