package liquidjava.classes.email2;


public class TestEmail {
    public static void main(java.lang.String[] args) {
        liquidjava.classes.email2.Email e = new liquidjava.classes.email2.Email();
        e.from("me");
        // missing to
        e.subject("not important");
        e.body("body");
        e.build();
    }
}

