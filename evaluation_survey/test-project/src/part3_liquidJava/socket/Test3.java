package part3_liquidJava.socket;

import java.util.ArrayDeque;

public class Test3 {
	
	public static void main(String[] args) {
		
            ArrayDeque<Integer> p = new ArrayDeque<>();
            p.add(2);
            p.remove();
            p.offerFirst(6);
            p.getLast();
            p.remove();
            p.getLast();
	
	}

}
