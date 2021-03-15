package regen.test.project;


@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
public class Order {
    @repair.regen.specification.RefinementPredicate("int priceNow(Order o)")
    @repair.regen.specification.StateRefinement(to = "priceNow(this) == 0 && empty(this)")
    public Order() {
    }

    // @StateRefinement(from="addingItems(this) || empty(this)",
    // to="(priceNow(this) == (priceNow(old(this)) + price)) && addingItems(this)")
    @repair.regen.specification.StateRefinement(from = "empty(this)", to = "addingItems(this)")
    @repair.regen.specification.StateRefinement(from = "addingItems(this)", to = "addingItems(this)")
    public regen.test.project.Order addItem(java.lang.String itemName, int price) {
        return this;
    }

    @repair.regen.specification.StateRefinement(from = "addingItems(this)", to = "checkout(this)")
    public regen.test.project.Order pay(int cardNumber) {
        return this;
    }

    // @StateRefinement(from="checkout(this) && priceNow(this) > 20", to = "checkout(this)")
    // public Order addGift() {
    // return this;
    // }
    @repair.regen.specification.StateRefinement(from = "checkout(this)", to = "closed(this)")
    public regen.test.project.Order sendToAddress(java.lang.String a) {
        return this;
    }
}

