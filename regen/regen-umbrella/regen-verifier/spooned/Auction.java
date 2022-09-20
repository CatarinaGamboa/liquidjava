

@repair.regen.specification.StateSet({ "openA", "closeA", "prizeSent" })
@repair.regen.specification.Ghost("int clientId")
@repair.regen.specification.Ghost("int currentVal")
public class Auction {
    @repair.regen.specification.StateRefinement(to = "openA(this) && clientId(this)==-1 && currentVal(this) == initialValue")
    public Auction(@repair.regen.specification.Refinement("_ >= 0")
    int initialValue) {
    }

    @repair.regen.specification.StateRefinement(from = "openA(this) && id != clientId(old(this))&&" + "value > currentVal(this)", to = "openA(this) && clientId(this) == id && currentVal(this) == value")
    public void bid(@repair.regen.specification.Refinement("_ >= 0")
    int id, @repair.regen.specification.Refinement("_ >= 0")
    int value) {
    }

    @repair.regen.specification.StateRefinement(from = "openA(this)", to = "closeA(this)")
    public void close() {
    }

    @repair.regen.specification.StateRefinement(from = "closeA(this) && clientId(this) != -1", to = "prizeSent(this)")
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

