package bufferedreader;



import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;
@StateSet({"empty","addingItems", "checkout", "closed"})
public class Order {
	
	@RefinementPredicate("int totalPrice(Order o)")
	@StateRefinement(to="(totalPrice(this) == 0) && empty(this)")
	public Order() {}
	
	@StateRefinement(from="empty(this)", 
					 to="((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
	@Refinement("_ == this")
	public Order addItem(String itemName, int price) {		
		return this;
	}
	
	@StateRefinement(from="((addingItems(this)) && (countItems(this) == 3))")
	public boolean hasThree() {return false;}
	
//	@StateRefinement(from="addingItems(this)", to = "checkout(this)")
//	public Order pay(int cardNumber) {
//		return this;
//	}
	
//	@StateRefinement(from="checkout(this) && priceNow(this) > 20", to = "checkout(this)")
//	public Order addGift() {
//		return this;
//	}
//	
//	@StateRefinement(from="checkout(this)", to = "closed(this)")
//	public Order sendToAddress(String a) {
//		return this;
//	}

}
