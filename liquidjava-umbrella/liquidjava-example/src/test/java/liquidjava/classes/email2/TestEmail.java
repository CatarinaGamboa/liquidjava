package liquidjava.classes.email2;

public class TestEmail {
	
	public static void main(String[] args) {

		Email e = new Email();
		e.from("me");
		//missing to
		e.subject("not important");
		e.body("body");
		e.build();

	}

}
