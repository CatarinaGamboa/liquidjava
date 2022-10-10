package liquidjava.classes.email3;


// https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
// Suppose there is only one acceptable order to construct the email
// add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
@java.lang.SuppressWarnings("unused")
@liquidjava.specification.StateSet({ "emptyEmail", "senderSet", "receiverSet", "bodySet" })
public class Email {
    private java.lang.String sender;

    private java.util.List<java.lang.String> receiver;

    private java.lang.String subject;

    private java.lang.String body;

    @liquidjava.specification.StateRefinement(to = "emptyEmail(this) && receiverSet(this)")
    public Email() {
        receiver = new java.util.ArrayList<>();
    }

    @liquidjava.specification.StateRefinement(from = "emptyEmail(this)", to = "senderSet(this)")
    public void from(java.lang.String s) {
        sender = s;
    }

    @liquidjava.specification.StateRefinement(from = "(senderSet(this)) || (receiverSet(this))", to = "receiverSet(this)")
    public void to(java.lang.String s) {
        receiver.add(s);
    }

    @liquidjava.specification.StateRefinement(from = "receiverSet(this)", to = "receiverSet(this)")
    public void subject(java.lang.String s) {
        // optional
        subject = s;
    }

    @liquidjava.specification.StateRefinement(from = "receiverSet(this)", to = "bodySet(this)")
    public void body(java.lang.String s) {
        body = s;
    }

    @liquidjava.specification.StateRefinement(from = "bodySet(this)", to = "bodySet(this)")
    public java.lang.String build() {
        return "email: ";// string with all the email

    }
}

