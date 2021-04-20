package repair.regen.classes.order_gift_error;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;

import bufferedreader.OrderSimple;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateSet;

public class SimpleTest {
	
	public static void main(String[] args) throws IOException{
		Order o = new Order();
		Order f = o.addItem("shirt", 60)
				.getNewOrderPayThis()
				.addItem("t", 6)
				.addItem("t", 1);
		o.addGift();
		f.addItem("l", 1).addGift();
	}




}