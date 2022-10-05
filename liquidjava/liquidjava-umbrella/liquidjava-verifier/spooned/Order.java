

@liquidjava.specification.StateSet({ "empty", "addingItems", "checkout" })
@liquidjava.specification.Ghost("int sum")
public class Order {
    private java.util.List<java.lang.String> products = new java.util.ArrayList();

    private java.util.List<java.lang.Integer> prices = new java.util.ArrayList();

    private boolean finish = false;

    // @StateRefinement(to ="empty(this) && (sum(this) == 0)") //default
    public Order() {
    }

    @liquidjava.specification.StateRefinement(from = "empty(this) || addingItems(this)", to = "addingItems(this) && (sum(this) == (sum(old(this)) + value))")
    public void addItem(java.lang.String productName, @liquidjava.specification.Refinement("_ > 0")
    int value) {
        products.add(productName);
        prices.add(value);
    }

    @liquidjava.specification.StateRefinement(from = "addingItems(this)", to = "checkout(this)")
    public void checkout() {
        finish = true;
    }

    @liquidjava.specification.StateRefinement(from = "checkout(this) && sum(this) > 20")
    public void addGift() {
        products.add("gift");
    }

    @liquidjava.specification.StateRefinement(from = "checkout(this)")
    public void pay(int cardNumber) {
        makePayment(cardNumber);
    }

    private void makePayment(int cardNumber) {
        // Invokes an external service to make the payment
    }
}

