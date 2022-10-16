package liquidjava.classes.email3;

import java.util.ArrayList;
import java.util.List;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

//https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
//Suppose there is only one acceptable order to construct the email
//add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
@SuppressWarnings("unused")
@StateSet({"emptyEmail", "senderSet", "receiverSet", "bodySet"})
public class Email {
	private String sender;
	private List<String> receiver;
	private String subject;
	private String body;

	@StateRefinement(to = "emptyEmail(this) && receiverSet(this)")
	public Email() {
		receiver = new ArrayList<>();
	}
	
	@StateRefinement(from= "emptyEmail(this)", to = "senderSet(this)")
	public void from(String s) {
		sender = s;
	}

	@StateRefinement(from= "(senderSet(this)) || (receiverSet(this))", 
					 to =  "receiverSet(this)")
	public void to(String s) {
		receiver.add(s);
	}
	
	@StateRefinement(from= "receiverSet(this)",  to = "receiverSet(this)")
	public void subject(String s) {//optional
		subject = s;
	}
	
	@StateRefinement(from= "receiverSet(this)", to = "bodySet(this)")
	public void body(String s) {
		body = s;
	}
	
	@StateRefinement(from= "bodySet(this)", to = "bodySet(this)")
	public String build() {
		return "email: ";//string with all the email
	}
	

}
