package liquidjava.processor.context;

import liquidjava.rj_language.Predicate;

public class ObjectState {

    Predicate from;
    Predicate to;

    public ObjectState() {
    }

    public ObjectState(Predicate from, Predicate to) {
        this.from = from;
        this.to = to;
    }

    public void setFrom(Predicate from) {
        this.from = from;
    }

    public void setTo(Predicate to) {
        this.to = to;
    }

    public boolean hasFrom() {
        return from != null;
    }

    public boolean hasTo() {
        return to != null;
    }

    public Predicate getFrom() {
        return from != null ? from : new Predicate();
    }

    public Predicate getTo() {
        return to != null ? to : new Predicate();
    }

    public ObjectState clone() {
        return new ObjectState(from.clone(), to.clone());
    }

    @Override
    public String toString() {
        return "ObjectState [from=" + from + ", to=" + to + "]";
    }

}
