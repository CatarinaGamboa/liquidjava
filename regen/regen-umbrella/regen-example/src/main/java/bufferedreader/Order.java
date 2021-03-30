package bufferedreader;



import repair.regen.specification.Ghost;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"empty","addingItems", "checkout", "closed"})
@Ghost   ("int totalPrice")
public class Order {
	

	public Order() {}
	
	@StateRefinement(from = "(empty(this) || addingItems(this))", 
					 to   = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
	@Refinement("_ == this")
	public Order addItem(String itemName, @Refinement("_ > 0")int price) {		
		return this;
	}
	
	@StateRefinement(from = "addingItems(this)", 
					 to   = "checkout(this)")
	@Refinement("_ == this")
	public Order pay(int cardNumber) {
		return this;
	}
	
	@StateRefinement(from = "checkout(this) && totalPrice(this) > 20", to = "checkout(this)")
	@Refinement("_ == this")
	public Order addGift() {
		return this;
	}
	
	@StateRefinement(from = "checkout(this)", to = "totalPrice(this) == (totalPrice(old(this)) + 3)")
	@Refinement("_ == this")
	public Order addTransportCosts() {
		return this;
	}
	
	@StateRefinement(from="checkout(this)", to = "closed(this)")
	@Refinement("_ == this")
	public Order sendToAddress(String a) {
		return this;
	}

	@StateRefinement(to = "checkout(this)")
	@Refinement("(totalPrice(_) == 0) && empty(_)")
	public Order getNewOrderPayThis() {
		return new Order();
	}
	
}