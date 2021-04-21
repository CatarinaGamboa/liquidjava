package regen.test.project;


@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
@repair.regen.specification.Ghost("int totalPrice")
public class Order {
    @repair.regen.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public Order() {
    }

    @repair.regen.specification.StateRefinement(from = "(empty(this) || addingItems(this))", to = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
    @repair.regen.specification.Refinement("_ == this")
    public regen.test.project.Order addItem(java.lang.String itemName, @repair.regen.specification.Refinement("_ > 0")
    int price) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "addingItems(this)", to = "checkout(this)")
    @repair.regen.specification.Refinement("_ == this")
    public regen.test.project.Order pay(int cardNumber) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "checkout(this) && totalPrice(this) > 20", to = "checkout(this)")
    @repair.regen.specification.Refinement("_ == this")
    public regen.test.project.Order addGift() {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "checkout(this)", to = "closed(this)")
    @repair.regen.specification.Refinement("_ == this")
    public regen.test.project.Order sendToAddress(java.lang.String a) {
        return this;
    }

    @repair.regen.specification.StateRefinement(to = "checkout(this)")
    @repair.regen.specification.Refinement("(totalPrice(_) == 0) && empty(_)")
    public regen.test.project.Order getNewOrderPayThis() {
        return new regen.test.project.Order();
    }
}

