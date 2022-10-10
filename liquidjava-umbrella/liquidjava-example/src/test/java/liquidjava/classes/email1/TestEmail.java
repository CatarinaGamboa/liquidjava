package liquidjava.classes.email1;



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
