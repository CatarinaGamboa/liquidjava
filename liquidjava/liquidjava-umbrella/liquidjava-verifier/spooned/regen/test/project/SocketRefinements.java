package regen.test.project;


@liquidjava.specification.ExternalRefinementsFor("java.net.Socket")
@liquidjava.specification.StateSet({ "unconnected", "binded", "connected", "closed" })
public interface SocketRefinements {
    @liquidjava.specification.StateRefinement(to = "unconnected(this)")
    public void Socket();

    @liquidjava.specification.StateRefinement(from = "unconnected(this)", to = "binded(this)")
    public void bind(java.net.SocketAddress add);

    @liquidjava.specification.StateRefinement(from = "binded(this)", to = "connected(this)")
    public void connect(java.net.SocketAddress add);

    @liquidjava.specification.StateRefinement(from = "connected(this)")
    public void sendUrgentData(int n);

    @liquidjava.specification.StateRefinement(to = "closed(this)")
    public void close();
}

