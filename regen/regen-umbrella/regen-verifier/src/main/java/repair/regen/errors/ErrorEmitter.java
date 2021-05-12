package repair.regen.errors;

import java.util.HashMap;

import repair.regen.processor.context.PlacementInCode;
import spoon.reflect.cu.SourcePosition;

public class ErrorEmitter {
	
	private String titleMessage;
	private String fullMessage;
	private ErrorPosition position;
	private int errorStatus;
	private HashMap<String, PlacementInCode> map;
	private static ErrorEmitter instance;
	
	public ErrorEmitter() {}
	
	
//	public static ErrorEmitter getInstance() {
//		if(instance == null)
//			return new ErrorEmitter();
//		return instance;
//	}
	
	public void addError(String titleMessage, String msg, SourcePosition p, int errorStatus, HashMap<String, PlacementInCode> map) {
		this.titleMessage = titleMessage;
		fullMessage = msg;
		position = new ErrorPosition(p.getLine(), p.getColumn(), p.getEndLine(), p.getEndColumn());
		this.errorStatus = errorStatus;
		this.map = map;
	}
	
	public void addError(String titleMessage, String msg, SourcePosition p, int errorStatus) {
		this.titleMessage = titleMessage;
		fullMessage = msg;
		position = new ErrorPosition(p.getLine(), p.getColumn(), p.getEndLine(), p.getEndColumn());
		this.errorStatus = errorStatus;
	}
	
	public void addError(String titleMessage, String msg, int errorStatus) {
		this.titleMessage = titleMessage;
		fullMessage = msg;
		this.errorStatus = errorStatus;
	}
	
	public boolean foundError() {
		return fullMessage != null && position != null;
	}
	
	public void reset() {
		fullMessage = null;
		position = null;
		errorStatus = 0;
		map = null;
	}
	
	public String getMessage() {
		return fullMessage;
	}
	
	public ErrorPosition getPosition() {
		return position;
	}
	
	public int getErrorStatus() {
		return errorStatus;
	}

	public HashMap<String, PlacementInCode> getVCMap(){
		return map;
	}
}
