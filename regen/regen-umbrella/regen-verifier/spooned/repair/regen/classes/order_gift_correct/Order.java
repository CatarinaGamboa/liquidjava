package repair.regen.classes.order_gift_correct;


@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
public class Order {
    @repair.regen.specification.RefinementPredicate("int totalPrice(Order o)")
    @repair.regen.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public Order() {
    }

    // @StateRefinement(from = "(empty(this) || addingItems(this))",
    // to   = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
    // @Refinement("_ == this")
    // public Order addItem(String itemName, @Refinement("_ > 0")int price) {
    // return this;
    // }
    // 
    // @StateRefinement(from = "addingItems(this)",
    // to   = "checkout(this) && (totalPrice(this) == totalPrice(old(this)))")
    // @Refinement("_ == this")
    // public Order pay(int cardNumber) {
    // return this;
    // }
    // 
    // @StateRefinement(from = "checkout(this) && totalPrice(this) > 20", to = "checkout(this)")
    // @Refinement("_ == this")
    // public Order addGift() {
    // return this;
    // }
    // 
    // @StateRefinement(from="checkout(this)", to = "closed(this)")
    // @Refinement("_ == this")
    // public Order sendToAddress(String a) {
    // return this;
    // }
    @repair.regen.specification.StateRefinement(to = "checkout(this) && (totalPrice(this) == totalPrice(old(this)))")
    @repair.regen.specification.Refinement("(totalPrice(_) == 0) && empty(_)")
    public repair.regen.classes.order_gift_correct.Order getNewOrderPayThis() {
        return new repair.regen.classes.order_gift_correct.Order();
    }
}

