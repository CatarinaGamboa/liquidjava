

@liquidjava.specification.StateSet({ "empty", "senderSet", "receiverSet", "bodySet", "sent" })
public class Email {
    @liquidjava.specification.StateRefinement(to = "empty(this)")
    public Email() {
    }

    @liquidjava.specification.StateRefinement(from = "empty(this)", to = "senderSet(this)")
    public void from() {
    }

    @liquidjava.specification.StateRefinement(from = "(senderSet(this) || receiverSet(this))", to = "receiverSet(this)")
    public void to() {
    }

    public void subject() {
    }

    @liquidjava.specification.StateRefinement(from = "receiverSet(this)", to = "bodySet(this)")
    public void body(java.lang.String s) {
    }

    @liquidjava.specification.StateRefinement(from = "bodySet(this)", to = "sent(this)")
    public void send() {
    }
}

