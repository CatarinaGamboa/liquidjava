package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSpecificFunctionInvocation {
	@Refinement("{a > 10}->{ _ > 0}")
	public static int doubleBiggerThanTen(int a){
		return a*2;
	}
	public static void main(String[] args) {
		@Refinement("a > 0")
		int a = 50;
		int b = doubleBiggerThanTen(a);
	}
}
