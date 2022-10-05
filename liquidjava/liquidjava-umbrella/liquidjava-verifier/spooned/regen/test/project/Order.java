package regen.test.project;


@liquidjava.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
@liquidjava.specification.Ghost("int totalPrice")
public class Order {
    @liquidjava.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public Order() {
    }

    @liquidjava.specification.StateRefinement(from = "(empty(this) || addingItems(this))", to = "((totalPrice(this) == (totalPrice(old(this)) + price)) && addingItems(this))")
    @liquidjava.specification.Refinement("_ == this")
    public regen.test.project.Order addItem(java.lang.String itemName, @liquidjava.specification.Refinement("_ > 0")
    int price) {
        return this;
    }

    @liquidjava.specification.StateRefinement(from = "addingItems(this)", to = "checkout(this)")
    @liquidjava.specification.Refinement("_ == this")
    public regen.test.project.Order pay(int cardNumber) {
        return this;
    }

    @liquidjava.specification.StateRefinement(from = "checkout(this) && totalPrice(this) > 20", to = "checkout(this)")
    @liquidjava.specification.Refinement("_ == this")
    public regen.test.project.Order addGift() {
        return this;
    }

    @liquidjava.specification.StateRefinement(from = "checkout(this)", to = "closed(this)")
    @liquidjava.specification.Refinement("_ == this")
    public regen.test.project.Order sendToAddress(java.lang.String a) {
        return this;
    }

    @liquidjava.specification.StateRefinement(to = "checkout(this)")
    @liquidjava.specification.Refinement("(totalPrice(_) == 0) && empty(_)")
    public regen.test.project.Order getNewOrderPayThis() {
        return new regen.test.project.Order();
    }
}

