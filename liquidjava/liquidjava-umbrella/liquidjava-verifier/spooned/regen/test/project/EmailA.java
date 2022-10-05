package regen.test.project;


// https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
// Suppose there is only one acceptable order to construct the email
// add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
@liquidjava.specification.RefinementAlias("EmptyEmail(Email e)  { state(e) == 1}")
@liquidjava.specification.RefinementAlias("SenderSet(Email e)   { state(e) == 2}")
@liquidjava.specification.RefinementAlias("ReceiverSet(Email e) { state(e) == 3}")
@liquidjava.specification.RefinementAlias("BodySet(Email e)     { state(e) == 4}")
public class EmailA {
    private java.lang.String sender;

    private java.util.List<java.lang.String> receiver;

    private java.lang.String subject;

    private java.lang.String body;

    @liquidjava.specification.RefinementPredicate("int state(Email e)")
    @liquidjava.specification.StateRefinement(to = "EmptyEmail(this)")
    public EmailA() {
        receiver = new java.util.ArrayList<>();
    }

    @liquidjava.specification.StateRefinement(from = "EmptyEmail(this)", to = "SenderSet(this)")
    public void from(java.lang.String s) {
        sender = s;
    }

    @liquidjava.specification.StateRefinement(from = "(SenderSet(this)) || (ReceiverSet(this))", to = "ReceiverSet(this)")
    public void to(java.lang.String s) {
        receiver.add(s);
    }

    @liquidjava.specification.StateRefinement(from = "ReceiverSet(this)", to = "ReceiverSet(this)")
    public void subject(java.lang.String s) {
        // optional
        subject = s;
    }

    @liquidjava.specification.StateRefinement(from = "ReceiverSet(this)", to = "BodySet(this)")
    public void body(java.lang.String s) {
        body = s;
    }

    @liquidjava.specification.StateRefinement(from = "BodySet(this)", to = "BodySet(this)")
    public java.lang.String build() {
        return "email...";// string with all the email

    }
}

