package regen.test.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.spi.FileSystemProvider;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;

public class SimpleTest {

	@RefinementPredicate("int length(char[] a)")
	public static void main(String[] args) throws IOException{

		InputStreamReader isr = new InputStreamReader(new FileInputStream("test1.txt"));
		@Refinement("_ > -9")
		int a = isr.read();
		char[] arr = new char[20];		
		int b = isr.read(arr, 10, 5);
		System.out.println(arr);
	}
	
	
	
	
	
//	InputStreamReader isr = new InputStreamReader(System.in);
//	isr.read();
//	isr.read();
//	isr.read();
//	isr.close();
//	
//	//...
//	isr.read();

	




	//	@Refinement("_ > 0")
	//	public int fun (int[] arr) {
	//		return max(arr[0], 1);
	//	}
	//	



	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5];
	//K(.., ..)

	//	}


	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;





}