package testSuite.classes.email_correct;

public class TestEmail {

    public static void main(String[] args) {

        Email e = new Email();
        e.from("me");
        e.to("you");
        e.to("you2");
        e.to("you3");
        e.subject("not important");
        e.body("body");
        e.build();
    }
}
