package repair.regen.utils;

import spoon.reflect.cu.SourcePosition;

public class Error {
	
	private String message;
	private SourcePosition position;
	private int errorStatus;
	private static Error instance;
	
	private Error() {}
	
	
	public static Error getInstance() {
		if(instance == null)
			return new Error();
		return instance;
	}
	
	public void addError(String msg, SourcePosition p, int errorStatus) {
		message = msg;
		position = p;
		this.errorStatus = errorStatus;
	}
	
	public boolean foundError() {
		return message != null && position != null;
	}
	
	public void reset() {
		message = null;
		position = null;
	}
	
	public String getMessage() {
		return message;
	}
	
	public SourcePosition getPosition() {
		return position;
	}
	
	public int getErrorStatus() {
		return errorStatus;
	}

}
