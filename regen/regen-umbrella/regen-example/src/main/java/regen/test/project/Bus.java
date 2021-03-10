package regen.test.project;

import repair.regen.specification.PrivateRefinement;
import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;

public class Bus extends Car{

	@StateRefinement(to = "!isOpen(this)")
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
