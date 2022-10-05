

@liquidjava.specification.StateSet({ "openA", "closeA", "prizeSent" })
@liquidjava.specification.Ghost("int clientId")
@liquidjava.specification.Ghost("int currentVal")
public class Auction {
    @liquidjava.specification.StateRefinement(to = "openA(this) && clientId(this)==-1 && currentVal(this) == initialValue")
    public Auction(@liquidjava.specification.Refinement("_ >= 0")
    int initialValue) {
    }

    @liquidjava.specification.StateRefinement(from = "openA(this) && id != clientId(old(this))&&" + "value > currentVal(this)", to = "openA(this) && clientId(this) == id && currentVal(this) == value")
    public void bid(@liquidjava.specification.Refinement("_ >= 0")
    int id, @liquidjava.specification.Refinement("_ >= 0")
    int value) {
    }

    @liquidjava.specification.StateRefinement(from = "openA(this)", to = "closeA(this)")
    public void close() {
    }

    @liquidjava.specification.StateRefinement(from = "closeA(this) && clientId(this) != -1", to = "prizeSent(this)")
    public void sendPrize() {
    }

    public static void main(java.lang.String[] args) {
        // Auction a = new Auction(50);
        // a.bid(1, 200);
        // a.bid(1, 100);
        // a.close();
        // a.sendPrize();
    }
}

