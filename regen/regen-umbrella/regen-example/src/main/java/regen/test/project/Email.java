package regen.test.project;


import java.util.ArrayList;
import java.util.List;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;

//https://blog.sigplan.org/2021/03/02/fluent-api-practice-and-theory/
//Suppose there is only one acceptable order to construct the email
//add sender -> add multiple receivers -> add subject <optional> -> add body -> build()
public class Email {
	private String sender;
	private List<String> receiver;
	private String subject;
	private String body;
	
	@RefinementPredicate("int state(Email e)")
	@StateRefinement(to = "state(this) == 1")
	public Email() {
		receiver = new ArrayList<>();
	}
	
	@StateRefinement(from= "state(this) == 2", to = "state(this) == 2")
	public void from(String s) {
		sender = s;
	}
	
//	@Refinement("_ == 1")
//	public int f(int s) {
//		return 1;
//	}
	
//	@StateRefinement(from= "(state(this) == 2) || (state(this) == 3)", 
//					 to =  "state(this) == 3")
//	public void to(String s) {
//		receiver.add(s);
//	}
//	
//	@StateRefinement(from= "state(this) == 3",  to = "state(this) == 3")
//	public void subject(String s) {//optional
//		subject = s;
//	}
//	
//	@StateRefinement(from= "state(this) == 3", to = "state(this) == 4")
//	public void body(String s) {
//		body = s;
//	}
//	
//	@StateRefinement(from= "state(this) == 4", to = "state(this) == 4")
//	public String build() {
//		return "email...";//string with all the email
//	}
//	

}
