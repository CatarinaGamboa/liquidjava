package repair.regen.classes.email3;

import java.util.ArrayList;
import java.util.List;

import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;

//https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
//Suppose there is only one acceptable order to construct the email
//add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
//DIFFERENCE
public class Email {
	private String sender;
	private List<String> receiver;
	private String subject;
	private String body;
	
	
	@RefinementPredicate("boolean emptyEmail(Email e)")
	@StateRefinement(to = "emptyEmail(this)")
	public Email() {
		receiver = new ArrayList<>();
	}
	
	@RefinementPredicate("boolean senderSet(Email e)")
	@StateRefinement(from= "emptyEmail(this)", to = "senderSet(this)")
	public void from(String s) {
		sender = s;
	}

	@RefinementPredicate("boolean receiverSet (Email e)")
	@StateRefinement(from= "(senderSet(this)) || (receiverSet(this))", 
					 to =  "receiverSet(this)")
	public void to(String s) {
		receiver.add(s);
	}
	
	@StateRefinement(from= "receiverSet(this)",  to = "receiverSet(this)")
	public void subject(String s) {//optional
		subject = s;
	}
	
	@RefinementPredicate("boolean bodySet(Email e)")
	@StateRefinement(from= "receiverSet(this)", to = "bodySet(this)")
	public void body(String s) {
		body = s;
	}
	
	@StateRefinement(from= "bodySet(this)", to = "bodySet(this)")
	public String build() {
		return "email: ";//string with all the email
	}
	

}
