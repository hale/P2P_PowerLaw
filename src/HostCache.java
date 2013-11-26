import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: philiphale
 * Date: 26/11/2013
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class HostCache {
  private ArrayList<String> peers;

  public HostCache() {
    this.peers = new ArrayList<String>();
  }

  /**
   * Peers invoke this method in order to join the network.
   * Adds the peer to the list of peers
   *
   * @param peerID The unique ID of the peer
   * @return A list of IDs of Super Peers which the peer can connect to.
   */
  public ArrayList<String> contact(String peerID) {
    addPeer(peerID);
    return peers;
  }

  public int peerCount() {
    return peers.size();
  }

  /**
   * Adds the peer to the hostcache.  Only adds if the peer is not present.
   *
   * @param id The ID of the peer.
   */
  private void addPeer(String id) {
    if (!peers.contains(id)) {
      peers.add(id);
    }
  }

}

