

public class Main {
    public static void main(java.lang.String[] args) {
        Order order = new Order();
        order.addItem("shirt", 20);
        order.checkout();
        order.addGift();
        order.pay(123456789);
    }
}

