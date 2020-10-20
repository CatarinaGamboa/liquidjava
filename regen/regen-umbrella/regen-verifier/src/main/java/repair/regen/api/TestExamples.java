package repair.regen.api;

import static org.junit.Assert.fail;

import java.security.Permission;

import org.junit.Test;

public class TestExamples {

	String testPath = "../regen-example/src/test/java/repair/regen/";

	private void testCorrect(String filename) {   
		MySecurityManager secManager = new MySecurityManager();
	    System.setSecurityManager(secManager);
	    try {
	    	CommandLineLauncher.process(filename);    
	    } catch (SecurityException e) {
	      fail();
	    }
	}
	
	private void testWrong(String filename) {    
		MySecurityManager secManager = new MySecurityManager();
	    System.setSecurityManager(secManager);
	    try {
	    	CommandLineLauncher.process(filename);    
	    } catch (SecurityException e) {
	      return;
	    }
	    fail();
	}
	
	@Test
	public void correctSimpleAssignment() {
		testCorrect(testPath+"CorrectSimpleAssignment.java");
	}

	@Test
	public void errorSimpleAssignment() {
		testWrong(testPath+"ErrorSimpleAssignment.java");
	}
	
	
	class MySecurityManager extends SecurityManager {
		//Handles exit(1) when the refinements are not respected
		  @Override public void checkExit(int status) {
			  if(status == 1)
				  throw new SecurityException();
		  }

		  @Override public void checkPermission(Permission perm) {
		      // Allow other activities by default
		  }
	}

}
