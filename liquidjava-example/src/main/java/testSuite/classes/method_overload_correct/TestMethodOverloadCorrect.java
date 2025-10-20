package testSuite.classes.method_overload_correct;

import java.util.concurrent.Semaphore;

public class TestMethodOverloadCorrect {
	public static void main(String[] args) throws InterruptedException {
		Semaphore sem = new Semaphore(1);
		sem.acquire(1);
	}
}