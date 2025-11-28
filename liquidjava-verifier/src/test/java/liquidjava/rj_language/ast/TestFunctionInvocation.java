package liquidjava.rj_language.ast;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

import liquidjava.rj_language.opt.Test;

public class TestFunctionInvocation {
    @Test
    public void testFunctionInvocationEqualsAndHashcode(){
        Expression exp1 = new LiteralString("./testFiles/file.txt");
        Expression exp2 = new LiteralString("./testFiles/file_fake.txt");

        FunctionInvocation f1 = new FunctionInvocation("java.nio.file.Paths.get", Arrays.asList(exp1));
        FunctionInvocation f2 = new FunctionInvocation("java.nio.file.Paths.get", Arrays.asList(exp1));
        FunctionInvocation f3 = new FunctionInvocation("java.nio.file.Paths.get", Arrays.asList(exp2));

        assertTrue(f1.equals(f2) && !f1.equals(f3));
        assertTrue(f1.hashCode() == f2.hashCode() && f1.hashCode() != f3.hashCode());
    }
    
}
