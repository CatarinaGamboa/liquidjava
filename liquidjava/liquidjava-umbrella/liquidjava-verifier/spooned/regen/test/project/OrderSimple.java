package regen.test.project;


// @StateRefinement(from="addingItems(this)", to = "checkout(this)")
// public Order pay(int cardNumber) {
// return this;
// }
// @StateRefinement(from="checkout(this) && priceNow(this) > 20", to = "checkout(this)")
// public Order addGift() {
// return this;
// }
// 
// @StateRefinement(from="checkout(this)", to = "closed(this)")
// public Order sendToAddress(String a) {
// return this;
// }
@liquidjava.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
public class OrderSimple {
    @liquidjava.specification.RefinementPredicate("int countItems(OrderSimple o)")
    @liquidjava.specification.StateRefinement(to = "(countItems(this) == 0) && empty(this)")
    public OrderSimple() {
    }

    // @Refinement("_ == this")
    @liquidjava.specification.StateRefinement(from = "(empty(this) || addingItems(this))", to = "((countItems(this) == (countItems(old(this)) + 1)) && addingItems(this))")
    public regen.test.project.OrderSimple addItem(java.lang.String itemName, int price) {
        return this;
    }

    @liquidjava.specification.StateRefinement(from = "((addingItems(this)) && (countItems(this) > 20))")
    public boolean hasGift() {
        return true;
    }
}

