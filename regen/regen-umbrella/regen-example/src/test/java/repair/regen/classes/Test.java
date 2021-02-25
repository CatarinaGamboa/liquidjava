package repair.regen.classes;
import repair.regen.specification.Refinement;

public class Test {

	@Refinement("_ < 10")
	public static int getYear() {
		return 8;
	}

	public static void main(String[] args) {
		int a = 1998;
		Car c = new Car();
		c.setYear(a);

		@Refinement("_ < 11")
		int j = getYear();
		
	}

}
