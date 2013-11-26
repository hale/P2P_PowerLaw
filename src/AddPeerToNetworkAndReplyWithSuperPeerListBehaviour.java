/**
 * To change this template use File | Settings | File Templates.
 */
public class AddPeerToNetworkAndReplyWithSuperPeerListBehaviour extends HostCacheBehaviour {

  private boolean finished = false;

  public AddPeerToNetworkAndReplyWithSuperPeerListBehaviour(HostCache hostCache) {
    super(hostCache);
  }

  public void action() {
    finished = true;
  }

  public boolean done() { return finished; }
}
