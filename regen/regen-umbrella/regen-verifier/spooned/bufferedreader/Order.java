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
public class Order {
    @repair.regen.specification.RefinementPredicate("int totalPrice(Order o)")
    @repair.regen.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public Order() {
    }

    // @Refinement("_ == this")
    @repair.regen.specification.StateRefinement(from = "(empty(this) || addingItems(this))", to = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
    public bufferedreader.Order addItem(java.lang.String itemName, int price) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "((addingItems(this)) && (totalPrice(this) > 20))")
    public boolean hasGift() {
        return true;
    }
}

