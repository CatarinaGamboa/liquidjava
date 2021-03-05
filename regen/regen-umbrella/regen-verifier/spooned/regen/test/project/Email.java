package regen.test.project;


// https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
// Suppose there is only one acceptable order to construct the email
// add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
public class Email {
    private java.lang.String sender;

    private java.util.List<java.lang.String> receiver;

    private java.lang.String subject;

    private java.lang.String body;

    @repair.regen.specification.RefinementPredicate("boolean emptyEmail(Email e)")
    @repair.regen.specification.StateRefinement(to = "emptyEmail(this)")
    public Email() {
        receiver = new java.util.ArrayList<>();
    }

    @repair.regen.specification.RefinementPredicate("boolean senderSet(Email e)")
    @repair.regen.specification.StateRefinement(from = "emptyEmail(this)", to = "senderSet(this)")
    public void from(java.lang.String s) {
        sender = s;
    }

    @repair.regen.specification.RefinementPredicate("boolean receiverSet (Email e)")
    @repair.regen.specification.StateRefinement(from = "(senderSet(this)) || (receiverSet(this))", to = "receiverSet(this)")
    public void to(java.lang.String s) {
        receiver.add(s);
    }

    @repair.regen.specification.StateRefinement(from = "receiverSet(this)", to = "receiverSet(this)")
    public void subject(java.lang.String s) {
        // optional
        subject = s;
    }

    @repair.regen.specification.RefinementPredicate("boolean bodySet(Email e)")
    @repair.regen.specification.StateRefinement(from = "receiverSet(this)", to = "bodySet(this)")
    public void body(java.lang.String s) {
        body = s;
    }

    @repair.regen.specification.StateRefinement(from = "bodySet(this)", to = "bodySet(this)")
    public java.lang.String build() {
        return "email: ";// string with all the email

    }
}

