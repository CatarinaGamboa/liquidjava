package repair.regen.classes.order_gift_correct;


public class SimpleTest {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        repair.regen.classes.order_gift_correct.Order o = new repair.regen.classes.order_gift_correct.Order();
        repair.regen.classes.order_gift_correct.Order f = o.addItem("shirt", 60).getNewOrderPayThis().addItem("t", 6).addItem("t", 1);
        o.addGift();
        f.addItem("l", 1);
    }
}

