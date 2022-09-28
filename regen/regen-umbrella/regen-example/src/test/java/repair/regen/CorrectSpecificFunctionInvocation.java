package repair.regen;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class CorrectSpecificFunctionInvocation {
	@Refinement(" _ > 0")
	public static int doubleBiggerThanTen(@Refinement("a > 10") int a){
		return a*2;
	}
	public static void main(String[] args) {
		@Refinement("a > 0")
		int a = 50;
		int b = doubleBiggerThanTen(a);
	}
}
