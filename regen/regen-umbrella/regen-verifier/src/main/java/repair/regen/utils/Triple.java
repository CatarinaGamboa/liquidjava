package repair.regen.utils;

public class Triple <A, B, C>{
	A a;
	B b;
	C c;
	
	public Triple(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public A getFist() {
		return a;
	}
	
	public B getSecond() {
		return b;
	}
	
	public C getThird() {
		return c;
	}
	
	public String toString() {
		return "Triple ["+a.toString()+", "+b.toString()+","+c.toString()+"]";
	}

}
