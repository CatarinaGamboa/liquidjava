package repair.regen.classes.refs_from_superclass_error;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.spi.FileSystemProvider;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;

public class SimpleTest {
	
	public static void main(String[] args) throws IOException{
		Bus b = new Bus();
		b.setYear(1400);
	}

}