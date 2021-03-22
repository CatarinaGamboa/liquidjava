package regen.test.project;


@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
public class Order {
    @repair.regen.specification.RefinementPredicate("int totalPrice(Order o)")
    @repair.regen.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public Order() {
    }

    @repair.regen.specification.StateRefinement(from = "(empty(this) || addingItems(this))", to = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
    @repair.regen.specification.Refinement("_ == this")
    public regen.test.project.Order addItem(java.lang.String itemName, int price) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "addingItems(this)", to = "checkout(this) && (totalPrice(this) == totalPrice(old(this)))")
    public regen.test.project.Order pay(int cardNumber) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "checkout(this) && totalPrice(this) > 20", to = "checkout(this)")
    public regen.test.project.Order addGift() {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "checkout(this)", to = "closed(this)")
    public regen.test.project.Order sendToAddress(java.lang.String a) {
        return this;
    }

    public regen.test.project.Order getNewOrder() {
        return new regen.test.project.Order();
    }
}

