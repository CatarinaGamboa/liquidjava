package repair.regen;


public class CorrectFPArithmetic {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 5.0")
        double a = 5.5;
        @repair.regen.specification.Refinement("_ == 10.0")
        double b = 10.0;
        @repair.regen.specification.Refinement("_ != 10.0")
        double c = 5.0;
        @repair.regen.specification.Refinement("t > 0.0")
        double t = a + 1.0;
        @repair.regen.specification.Refinement("_ >= 3.0")
        double k = a - 1.0;
        @repair.regen.specification.Refinement("_ > 0.0")
        double l = k * t;
        @repair.regen.specification.Refinement("_ > 0.0")
        double m = l / 2.0;
        @repair.regen.specification.Refinement("_ < 4.0")
        double n = 6.0 % 4.0;
        @repair.regen.specification.Refinement("_ < 0.0")
        double p = -5.0;
        @repair.regen.specification.Refinement("_ <= 0.0")
        double p1 = -a;
        @repair.regen.specification.Refinement("_ < -1.0")
        double p3 = p;
        @repair.regen.specification.Refinement("_ < -5.5")
        double d = (-a) - 2.0;
    }
}

