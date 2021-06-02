

@repair.regen.specification.StateSet({ "empty", "senderSet", "receiverSet", "bodySet", "sent" })
public class Email {
    @repair.regen.specification.StateRefinement(to = "empty(this)")
    public Email() {
    }

    @repair.regen.specification.StateRefinement(from = "empty(this)", to = "senderSet(this)")
    public void from() {
    }

    @repair.regen.specification.StateRefinement(from = "(senderSet(this) || receiverSet(this))", to = "receiverSet(this)")
    public void to() {
    }

    public void subject() {
    }

    @repair.regen.specification.StateRefinement(from = "receiverSet(this)", to = "bodySet(this)")
    public void body(java.lang.String s) {
    }

    @repair.regen.specification.StateRefinement(from = "bodySet(this)", to = "sent(this)")
    public void send() {
    }
}

