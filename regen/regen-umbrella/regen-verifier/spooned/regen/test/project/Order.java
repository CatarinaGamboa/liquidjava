package regen.test.project;


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
// 
// @StateRefinement(to = "checkout(this) && (totalPrice(this) == totalPrice(old(this)))")
// @Refinement("(totalPrice(_) == 0) && empty(_)")
// public Order getNewOrderPayThis() {
// return new Order();
// }
// 
@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
public class Order {
    @repair.regen.specification.RefinementPredicate("int totalPrice(Order o)")
    @repair.regen.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public Order() {
    }

    @repair.regen.specification.StateRefinement(from = "(empty(this) && addingItems(this))", to = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
    @repair.regen.specification.Refinement("_ == this")
    public regen.test.project.Order addItem(java.lang.String itemName, @repair.regen.specification.Refinement("_ > 0")
    int price) {
        return this;
    }
}

