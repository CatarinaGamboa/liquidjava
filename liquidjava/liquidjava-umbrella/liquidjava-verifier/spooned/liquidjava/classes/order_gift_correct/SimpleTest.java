package liquidjava.classes.order_gift_correct;


public class SimpleTest {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        liquidjava.classes.order_gift_correct.Order o = new liquidjava.classes.order_gift_correct.Order();
        liquidjava.classes.order_gift_correct.Order f = o.addItem("shirt", 60).getNewOrderPayThis().addItem("t", 6).addItem("t", 1);
        o.addGift();
        f.addItem("l", 1);
    }
}

