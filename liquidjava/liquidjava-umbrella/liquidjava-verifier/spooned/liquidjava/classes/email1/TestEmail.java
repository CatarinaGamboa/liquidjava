package liquidjava.classes.email1;


public class TestEmail {
    public static void main(java.lang.String[] args) {
        liquidjava.classes.email1.Email e = new liquidjava.classes.email1.Email();
        e.from("me");
        e.to("you");
        e.to("you2");
        e.to("you3");
        e.subject("not important");
        e.body("body");
        e.build();
    }
}
