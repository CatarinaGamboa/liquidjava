

@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout" })
@repair.regen.specification.Ghost("int sum")
public class Order {
    private java.util.List<java.lang.String> products = new java.util.ArrayList();

    private java.util.List<java.lang.Integer> prices = new java.util.ArrayList();

    private boolean finish = false;

    // @StateRefinement(to ="empty(this) && (sum(this) == 0)") //default
    public Order() {
    }

    @repair.regen.specification.StateRefinement(from = "empty(this) || addingItems(this)", to = "addingItems(this) && (sum(this) == (sum(old(this)) + value))")
    public void addItem(java.lang.String productName, @repair.regen.specification.Refinement("_ > 0")
    int value) {
        products.add(productName);
        prices.add(value);
    }

    @repair.regen.specification.StateRefinement(from = "addingItems(this)", to = "checkout(this)")
    public void checkout() {
        finish = true;
    }

    @repair.regen.specification.StateRefinement(from = "checkout(this) && sum(this) > 20")
    public void addGift() {
        products.add("gift");
    }

    @repair.regen.specification.StateRefinement(from = "checkout(this)")
    public void pay(int cardNumber) {
        makePayment(cardNumber);
    }

    private void makePayment(int cardNumber) {
        // Invokes an external service to make the payment
    }
}

