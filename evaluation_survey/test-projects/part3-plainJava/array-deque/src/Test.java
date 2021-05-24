import java.io.IOException;
import java.util.ArrayDeque;

public class Test {
	
	public static void main(String[] args) throws IOException{
                ArrayDeque<Integer> p = new ArrayDeque<>();
                p.add(2);
                p.remove();
                p.offerFirst(6);
                p.getLast();
                p.remove();
                p.getLast();
                p.add(78);
		p.add(8);
		p.getFirst();
        }

}
