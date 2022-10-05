package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectSpecificFunctionInvocation {
    @liquidjava.specification.Refinement(" _ > 0")
    public static int doubleBiggerThanTen(@liquidjava.specification.Refinement("a > 10")
    int a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("a > 0")
        int a = 50;
        int b = liquidjava.CorrectSpecificFunctionInvocation.doubleBiggerThanTen(a);
    }
}

