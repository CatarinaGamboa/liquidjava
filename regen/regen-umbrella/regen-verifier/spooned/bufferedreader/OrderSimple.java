package bufferedreader;


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
@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
public class OrderSimple {
    @repair.regen.specification.RefinementPredicate("int countItems(OrderSimple o)")
    @repair.regen.specification.StateRefinement(to = "(countItems(this) == 0) && empty(this)")
    public OrderSimple() {
    }

    // @Refinement("_ == this")
    @repair.regen.specification.StateRefinement(from = "(empty(this) || addingItems(this))", to = "((countItems(this) == (countItems(old(this)) + 1)) && addingItems(this))")
    public bufferedreader.OrderSimple addItem(java.lang.String itemName, int price) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "((addingItems(this)) && (countItems(this) > 20))")
    public boolean hasGift() {
        return true;
    }
}

