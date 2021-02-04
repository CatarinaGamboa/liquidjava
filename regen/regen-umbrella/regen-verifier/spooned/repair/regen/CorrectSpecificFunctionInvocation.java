package repair.regen;


public class CorrectSpecificFunctionInvocation {
    @repair.regen.specification.Refinement("{a > 10}->{ _ > 0}")
    public static int doubleBiggerThanTen(int a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 0")
        int a = 50;
        int b = repair.regen.CorrectSpecificFunctionInvocation.doubleBiggerThanTen(a);
    }
}

