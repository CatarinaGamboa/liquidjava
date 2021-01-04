package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSpecificArithmetic {
public static void main(String[] args) {
	@Refinement("\\v > 5")
	int a = 10;
	@Refinement("\\v > 10")
	int b = a+1;
	a = 6;
	b = a*2;
	@Refinement("\\v > 20")
	int c = b*-1;
}
}
