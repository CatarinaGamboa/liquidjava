package repair.regen.utils;

import spoon.reflect.cu.SourcePosition;

public class ErrorListener {
	
	private String message;
	private SourcePosition position;
	private static ErrorListener instance;
	
	private ErrorListener() {}
	
	
	public static ErrorListener getInstance() {
		if(instance == null)
			return new ErrorListener();
		return instance;
	}
	
	public void addError(String msg, SourcePosition p) {
		message = msg;
		position = p;
	}
	
	public boolean foundError() {
		return message != null && position != null;
	}
	
	public void reset() {
		message = null;
		position = null;
	}
	

}
