package liquidjava.api.tests;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestSeparationLogic extends TestAbstract {
    @Test
    public void correctSimpleFunctionCall() {
        testCorrect(testPath + "separation_logic/CorrectSimpleFunctionCall.java");
    }

    @Test
    public void errorSimpleFunctionCall() {
        testWrong(testPath + "separation_logic/ErrorSimpleFunctionCall.java");
    }

    @Test
    public void errorHeapShrinkAssign() {
        testWrong(testPath + "separation_logic/ErrorHeapShrinkAssign.java");
    }

    @Test
    public void correctHeapShrinkAssign() {
        testWrong(testPath + "separation_logic/CorrectHeapShrinkAssign.java");
    }

    @Test
    public void correctConstructor() {
        testCorrect(testPath + "separation_logic/CorrectConstructor.java");
    }

    @Test
    public void errorConstructor() {
        testWrong(testPath + "separation_logic/ErrorConstructor.java");
    }

    @Test
    public void errorSimpleIf() {
        testWrong(testPath + "separation_logic/ErrorSimpleIf.java");
    }

    // @Test
    // public void correctSimpleIf() {
    // testCorrect(testPath + "separation_logic/CorrectSimpleIf.java");
    // }

    private long meanTimeOfAnnSimpleFunctionCall(int sampleSize) {
        Long totalTime = Stream.generate(() -> 0).limit(sampleSize).map(i -> {
            long start = System.currentTimeMillis();
            correctSimpleFunctionCall();
            errorSimpleFunctionCall();
            return (System.currentTimeMillis() - start) / 2L;
        }).reduce(0L, Long::sum);
        return totalTime / sampleSize;
    }

    private long meanTimeOfSimpleFunctionCall(int sampleSize) {
        Long totalTime = Stream.generate(() -> 0).limit(sampleSize).map(i -> {
            long start = System.currentTimeMillis();
            testCorrect(testPath + "separation_logic/SimpleFunctionCall.java");
            testCorrect(testPath + "separation_logic/SimpleFunctionCall.java");
            return (System.currentTimeMillis() - start) / 2L;
        }).reduce(0L, Long::sum);
        return totalTime / sampleSize;
    }

    private long meanTimeOfSimpleBaseline(int sampleSize) {
        Long totalTime = Stream.generate(() -> 0).limit(sampleSize).map(i -> {
            long start = System.currentTimeMillis();
            testCorrect(testPath + "separation_logic/SimpleBaseline.java");
            testCorrect(testPath + "separation_logic/SimpleBaseline.java");
            return (System.currentTimeMillis() - start) / 2L;
        }).reduce(0L, Long::sum);
        return totalTime / sampleSize;
    }

    static private Stream<Integer> getSamples() {
        int maxSampleSize = 15;
        int step = 0;

        return Stream.iterate(30, i -> i + step).limit(maxSampleSize);
    }

    static String path = "benchmarks/chosen/";

    // @Test
    public void benchmarkSimpleFunctionCall() {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileOutputStream fos = new FileOutputStream(path + "no_ann_bench.txt");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            getSamples().map(this::meanTimeOfSimpleFunctionCall).forEach(mes -> {
                try {
                    outStream.writeBytes(mes + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // @Test
    public void benchmarkBaselineSimpleFunctionCall() {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileOutputStream fos = new FileOutputStream(path + "baseline_bench.txt");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            getSamples().map(this::meanTimeOfSimpleBaseline).forEach(mes -> {
                try {
                    outStream.writeBytes(mes + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // @Test
    public void benchmarkSimpleAnnFunctionCall() {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileOutputStream fos = new FileOutputStream(path + "ann_bench.txt");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            getSamples().map(this::meanTimeOfAnnSimpleFunctionCall).forEach(mes -> {
                try {
                    outStream.writeBytes(mes + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
