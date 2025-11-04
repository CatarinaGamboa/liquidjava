package testSuite.classes.method_overload_error;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.Refinement;

@ExternalRefinementsFor("java.util.concurrent.Semaphore")
public interface DummySemaphoreRefinements {

	public abstract void acquire();

	public abstract void acquire(@Refinement("_ >= 0") int permits) throws InterruptedException;
}