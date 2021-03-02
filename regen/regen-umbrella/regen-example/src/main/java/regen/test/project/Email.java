package regen.test.project;

import java.util.ArrayList;
import java.util.List;

import repair.regen.specification.RefinementFunction;
import repair.regen.specification.StateRefinement;

//https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
//Suppose there is only one acceptable order to construct the email
//add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
public class Email {
	private String sender;
	private List<String> receiver;
	private String subject;
	private String body;
	
	@RefinementFunction("int state(Email e)")
	@StateRefinement(to = "state(this) == 1")
	public Email() {
		receiver = new ArrayList<>();
	}
	
	@StateRefinement(from= "state(this) == 1", to = "state(this) == 2")
	public Email from(String s) {
		sender = s;
		return this;
	}
	
	@StateRefinement(from= "(state(this) == 2) || (state(this) == 3)", 
					 to =  "state(this) == 3")
	public Email to(String s) {
		receiver.add(s);
		return this;
	}
	
	@StateRefinement(from= "state(this) == 3",  to = "state(this) == 3")
	public Email subject(String s) {//optional
		subject = s;
		return this;
	}
	
	@StateRefinement(from= "state(this) == 3", to = "state(this) == 4")
	public Email body(String s) {
		body = s;
		return this;
	}
	
	@StateRefinement(from= "state(this) == 4", to = "state(this) == 4")
	public String build() {
		return "email...";//string with all the email
	}
	

}
