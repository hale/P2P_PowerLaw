import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.UUID;

/**
 */
public class HostCache extends Agent {

  public static String NAME = "HOST_CACHE";
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

  /**
   * @return The count of peers registered in the host service.
   */
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

