package regen.test.project;


// https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
// Suppose there is only one acceptable order to construct the email
// add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
// @Refinement("_ == 1")
// public int f(int s) {
// return 1;
// }
// @StateRefinement(from= "(state(this) == 2) || (state(this) == 3)",
// to =  "state(this) == 3")
// public void to(String s) {
// receiver.add(s);
// }
// 
// @StateRefinement(from= "state(this) == 3",  to = "state(this) == 3")
// public void subject(String s) {//optional
// subject = s;
// }
// 
// @StateRefinement(from= "state(this) == 3", to = "state(this) == 4")
// public void body(String s) {
// body = s;
// }
// 
// @StateRefinement(from= "state(this) == 4", to = "state(this) == 4")
// public String build() {
// return "email...";//string with all the email
// }
// 
public class Email {
    private java.lang.String sender;

    private java.util.List<java.lang.String> receiver;

    private java.lang.String subject;

    private java.lang.String body;

    @repair.regen.specification.RefinementPredicate("int state(Email e)")
    @repair.regen.specification.StateRefinement(to = "state(this) == 1")
    public Email() {
        receiver = new java.util.ArrayList<>();
    }

    @repair.regen.specification.StateRefinement(from = "state(this) == 2", to = "state(this) == 2")
    public void from(java.lang.String s) {
        sender = s;
    }
}

