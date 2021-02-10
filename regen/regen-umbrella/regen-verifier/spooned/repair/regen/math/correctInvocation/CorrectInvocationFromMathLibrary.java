package repair.regen.math.correctInvocation;


public class CorrectInvocationFromMathLibrary {
    public static void main(java.lang.String[] args) {
        // Math.abs(...)
        @repair.regen.specification.Refinement("b > 0")
        int b = java.lang.Math.abs(6);
        @repair.regen.specification.Refinement("_ == 6")
        int a = java.lang.Math.abs(6);
        @repair.regen.specification.Refinement("_ > 4")
        int d = java.lang.Math.abs((-6));
        @repair.regen.specification.Refinement("_ == -6")
        int e = -(java.lang.Math.abs((-d)));
        @repair.regen.specification.Refinement("_ > 4")
        int d1 = java.lang.Math.abs((-6));
        @repair.regen.specification.Refinement("_ == -6")
        int e1 = -(java.lang.Math.abs((-d1)));
        @repair.regen.specification.Refinement("_ == -6")
        int f1 = -(java.lang.Math.abs(e1));
        @repair.regen.specification.Refinement("_ == -6")
        int f2 = -(java.lang.Math.abs(f1));
        // addExact(...)
        @repair.regen.specification.Refinement("_ == 11")
        int a3 = java.lang.Math.addExact(5, 6);
        // decrementExact
        @repair.regen.specification.Refinement("_ > 5")
        int a7 = 10;
        @repair.regen.specification.Refinement("_ > 4")
        int a8 = java.lang.Math.decrementExact(a7);
        @repair.regen.specification.Refinement("_ == 9")
        int a9 = java.lang.Math.decrementExact(a7);
        // incrementExact
        @repair.regen.specification.Refinement("_ > 6")
        int a12 = java.lang.Math.incrementExact(a7);
        @repair.regen.specification.Refinement("_ == 11")
        int a13 = java.lang.Math.incrementExact(a7);
        @repair.regen.specification.Refinement("_ > 5")
        int prim = 10;
        @repair.regen.specification.Refinement("_ > 6")
        int seg = java.lang.Math.incrementExact(prim);
        @repair.regen.specification.Refinement("_ == 12")
        int ter = java.lang.Math.incrementExact(seg);
        // max
        @repair.regen.specification.Refinement("_ == 5")
        int m1 = java.lang.Math.max(4, 5);
        @repair.regen.specification.Refinement("_ > 5")
        int m2 = java.lang.Math.max(100, m1);
        @repair.regen.specification.Refinement("_ == 100")
        int m3 = java.lang.Math.max(100, m2);
        @repair.regen.specification.Refinement("_ == -100")
        int m4 = java.lang.Math.max((-1000), (-m2));
        // min
        @repair.regen.specification.Refinement("_ == 4")
        int m5 = java.lang.Math.min(4, 5);
        @repair.regen.specification.Refinement("_ < 5")
        int m6 = java.lang.Math.min(100, m5);
        @repair.regen.specification.Refinement("_ == 4")
        int m7 = java.lang.Math.min(100, m6);
        @repair.regen.specification.Refinement("_ == -1000")
        int m8 = java.lang.Math.min((-1000), (-m6));
        // multiplyExact
        @repair.regen.specification.Refinement("_ == 40")
        int mul = java.lang.Math.multiplyExact(5, 8);
        @repair.regen.specification.Refinement("_ == -mul")
        int mul1 = java.lang.Math.multiplyExact(mul, (-1));
        @repair.regen.specification.Refinement("_ > mul")
        int mul2 = java.lang.Math.multiplyExact(mul1, mul1);
        // negateExact
        @repair.regen.specification.Refinement("_ == 40")
        int negE = java.lang.Math.negateExact((-40));
        @repair.regen.specification.Refinement("_ < 0")
        int negEx = java.lang.Math.negateExact(negE);
        @repair.regen.specification.Refinement("_ > 39")
        int negExa = java.lang.Math.negateExact(negEx);
        // subtractExact
        @repair.regen.specification.Refinement("_ < -40")
        int subE = java.lang.Math.subtractExact((-40), 5);
        @repair.regen.specification.Refinement("_ > 0")
        int subEx = java.lang.Math.subtractExact(0, subE);
        @repair.regen.specification.Refinement("_ == 0")
        int subExa = java.lang.Math.subtractExact(subEx, subEx);
    }

    private void notIntMethods() {
        // working but taking lot of time
        // //Math.random()
        // @Refinement("_ >= 0")
        // double c = Math.random();
        // @Refinement("_ < 0")
        // double f = -Math.random();
        // @Refinement("true")
        // double r1 = Math.random();
        // @Refinement("_ > 0")
        // double r2 = r1*5;
        // 
        // 
        // @Refinement("_ > 0")
        // double a1 = Math.abs(15.3);
        // @Refinement("_ > 10")
        // long b1 = Math.abs(-13);
        // @Refinement("_ > 10")
        // float c1 = Math.abs(-13f);
        // 
        // @Refinement("_ > 10")
        // long b3 = Math.addExact(5l, 6l);
        // 
        // 
        // @Refinement("_ < 4")
        // double a5 = Math.acos(0.5);
        // @Refinement("_ < 2")
        // double a6 = Math.asin(a5);
        // 
        // 
        // @Refinement("_ > 5")
        // int a7 = 10;
        // @Refinement("_ > 4")
        // long a10 = Math.decrementExact(a7);
        // @Refinement("_ == 9")
        // long a11 = Math.decrementExact(a7);
        // copySign
        // @Refinement("(_ == -5)")
        // float prim = Math.copySign(-5, -500);
        // @Refinement("_ == -656")
        // float ter = Math.copySign(656, prim);
        // Constants
        @repair.regen.specification.Refinement("_ > 3")
        double a2 = java.lang.Math.PI;
        @repair.regen.specification.Refinement("_ > 2")
        double b2 = java.lang.Math.E;
        @repair.regen.specification.Refinement("_ == 30")
        double radius = 30;
        @repair.regen.specification.Refinement("perimeter > 1")
        double perimeter = (2 * (java.lang.Math.PI)) * radius;
    }
}

