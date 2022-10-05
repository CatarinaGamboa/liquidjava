package regen.test.project;


public class AccClient {
    public void transfer(regen.test.project.Account f1, regen.test.project.Account f2, regen.test.project.Account to, int x) {
        f1.withdraw(x);
        f2.withdraw(x);
        to.add(x);
        to.add(x);
    }
}

