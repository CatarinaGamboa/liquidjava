package socket;

import java.net.SocketAddress;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@ExternalRefinementsFor("java.net.Socket")
@StateSet({"unconnected", "binded", "connected", "closed"})
public interface SocketRefinements {
    
	@StateRefinement(to="unconnected(this)")
	public void Socket();
	
	@StateRefinement(from="unconnected(this)",to="binded(this)")
	public void bind(SocketAddress add);
	
	@StateRefinement(from="binded(this)", to="connected(this)")
	public void connect(SocketAddress add);
	
	@StateRefinement(from="connected(this)")
	public void sendUrgentData(int n);
	
	@StateRefinement(to="closed(this)")
	public void close();

}