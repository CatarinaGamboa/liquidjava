package repair.regen.classes.email2;


public class TestEmail {
    public static void main(java.lang.String[] args) {
        repair.regen.classes.email2.Email e = new repair.regen.classes.email2.Email();
        e.from("me");
        // missing to
        e.subject("not important");
        e.body("body");
        e.build();
    }
}

