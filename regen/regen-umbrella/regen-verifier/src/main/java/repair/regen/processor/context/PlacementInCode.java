package repair.regen.processor.context;

import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

public class PlacementInCode {
	private String text;
	private SourcePosition position;
	
	private PlacementInCode(String t, SourcePosition s) {
		this.text = t;
		this.position = s;
	}

	public String getText() {
		return text;
	}

	public SourcePosition getPosition() {
		return position;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setPosition(SourcePosition position) {
		this.position = position;
	}

	public static PlacementInCode createPlacement(CtElement elem) {
		return new PlacementInCode(elem.toString(), elem.getPosition());
	}
	
	public String toString() {
		return text + "  at:" +position.getFile()+":"+position.getLine()+", "+position.getColumn();
	}

}
