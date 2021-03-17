package repair.regen.processor.context;

import java.util.Optional;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;

public class ObjectState {
	
	Constraint from;
	Constraint to;
	
	public ObjectState() {}
	
	public ObjectState(Constraint from, Constraint to) {
		this.from = from;
		this.to = to;
	}
	
	public void setFrom(Constraint from) {
		this.from = from;
	}
	public void setTo(Constraint to) {
		this.to = to;
	}
	
	public boolean hasFrom() {
		return from!=null;
	}
	public boolean hasTo() {
		return to!=null;
	}
	
	public Constraint getFrom() {
		return from != null? from:new Predicate();
	}
	public Constraint getTo() {
		return to != null? to:new Predicate();
	}
	
	public ObjectState clone() {
		return new ObjectState(from.clone(), to.clone());
	}

	@Override
	public String toString() {
		return "ObjectState [from=" + from + ", to=" + to + "]";
	}

	
}
