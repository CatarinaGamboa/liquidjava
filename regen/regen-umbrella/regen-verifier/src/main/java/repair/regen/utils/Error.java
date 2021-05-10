package repair.regen.utils;

import java.util.HashMap;

import repair.regen.processor.context.PlacementInCode;
import spoon.reflect.cu.SourcePosition;

public class Error {
	
	private String message;
	private SourcePosition position;
	private int errorStatus;
	private HashMap<String, PlacementInCode> map;
	private static Error instance;
	
	private Error() {}
	
	
	public static Error getInstance() {
		if(instance == null)
			return new Error();
		return instance;
	}
	
	public void addError(String msg, SourcePosition p, int errorStatus, HashMap<String, PlacementInCode> map) {
		message = msg;
		position = p;
		this.errorStatus = errorStatus;
		this.map = map;
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

	public HashMap<String, PlacementInCode> getVCMap(){
		return map;
	}
}
