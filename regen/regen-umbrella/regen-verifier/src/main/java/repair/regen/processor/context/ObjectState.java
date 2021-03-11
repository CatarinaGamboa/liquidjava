package repair.regen.processor.context;

import java.util.Optional;

import repair.regen.processor.constraints.Constraint;

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
	
	
	public Optional<Constraint> getFrom() {
		return from != null? Optional.of(from):Optional.empty();
	}
	public Optional<Constraint> getTo() {
		return to != null? Optional.of(to):Optional.empty();
	}
	
	public ObjectState clone() {
		return new ObjectState(from.clone(), to.clone());
	}

}
