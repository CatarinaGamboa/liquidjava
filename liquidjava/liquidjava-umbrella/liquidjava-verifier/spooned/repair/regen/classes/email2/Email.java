package liquidjava.classes.email2;


// https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
// Suppose there is only one acceptable order to construct the email
// add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
// @RefinementAlias("EmptyEmail(Email e) { state(e) == 1}")
@java.lang.SuppressWarnings("unused")
public class Email {
    private java.lang.String sender;

    private java.util.List<java.lang.String> receiver;

    private java.lang.String subject;

    private java.lang.String body;

    @liquidjava.specification.RefinementPredicate("int state(Email e)")
    @liquidjava.specification.StateRefinement(to = "state(this) == 1")
    public Email() {
        receiver = new java.util.ArrayList<>();
    }

    @liquidjava.specification.StateRefinement(from = "state(this) == 1", to = "state(this) == 2")
    public void from(java.lang.String s) {
        sender = s;
    }

    @liquidjava.specification.StateRefinement(from = "(state(this) == 2) || (state(this) == 3)", to = "state(this) == 3")
    public void to(java.lang.String s) {
        receiver.add(s);
    }

    @liquidjava.specification.StateRefinement(from = "state(this) == 3", to = "state(this) == 3")
    public void subject(java.lang.String s) {
        // optional
        subject = s;
    }

    @liquidjava.specification.StateRefinement(from = "state(this) == 3", to = "state(this) == 4")
    public void body(java.lang.String s) {
        body = s;
    }

    @liquidjava.specification.StateRefinement(from = "state(this) == 4", to = "state(this) == 4")
    public java.lang.String build() {
        return "email...";// string with all the email

    }
}

