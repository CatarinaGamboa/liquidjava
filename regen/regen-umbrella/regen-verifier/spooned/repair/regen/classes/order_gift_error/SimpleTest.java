package repair.regen.classes.order_gift_error;


public class SimpleTest {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        repair.regen.classes.order_gift_error.Order o = new repair.regen.classes.order_gift_error.Order();
        repair.regen.classes.order_gift_error.Order f = o.addItem("shirt", 60).getNewOrderPayThis().addItem("t", 6).addItem("t", 1);
        o.addGift();
        f.addItem("l", 1).addGift();
    }
}

