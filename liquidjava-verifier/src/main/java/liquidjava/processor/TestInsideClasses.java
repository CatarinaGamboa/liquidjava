package liquidjava.processor;

import spoon.Launcher;

public class TestInsideClasses {
    public static void main(String[] args) {

        Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();

        // final Factory factory = launcher.getFactory();
        // RefinedVariable vi2 = new Variable("a",factory.Type().INTEGER_PRIMITIVE, new Predicate("a >
        // 0"));
        // CtTypeReference<?> intt = factory.Type().INTEGER_PRIMITIVE;
        // List<CtTypeReference<?>> l = new ArrayList<>();
        // l.add(intt);
        // GhostState s = new GhostState("green", l, intt, "A");
        // GhostState ss = new GhostState("yellow", l, intt, "A");
        // GhostState sss = new GhostState("red", l, intt, "A");
        // List<GhostState> gh = new ArrayList<>();
        // gh.add(s);gh.add(ss);gh.add(sss);
        // Predicate p = new Predicate("green(this) && red(this) == iio && u(3)");
        // System.out.println(p.getVariableNames());

    }
}
