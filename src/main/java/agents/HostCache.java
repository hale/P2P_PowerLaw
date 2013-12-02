package agents;

import behaviours.ReceiveNeighboursRequest;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * The Host Cache is responsible for adding new peers to a list of connected peers
 * and supplying peers with a list of Super Peers in the network.
 */
public class HostCache extends BasicAgent {

  public static final String NAME = "HOST_CACHE";

  // How many Super Peers the Host Cache should return when asked
  private static int MAX_NEIGHBOURS;

  // List of all peers in the network, and whether the peer is a Super Peer
  private final HashMap<AID,Boolean> peerList = new HashMap<AID, Boolean>();

  @Override
  protected void setup() {
    super.setup();
    MAX_NEIGHBOURS = (Integer) args[0];
    addBehaviour(new ReceiveNeighboursRequest(this));
  }

  /**
   * Adds the peer to the network (but only if it isn't already present)
   *
   * @param peer AID of the joining Peer.
   * @param isSuper Whether the Peer is a Super Peer or not.
   */
  public void addPeer(AID peer, boolean isSuper) {
    peerList.put(peer, isSuper);
  }

  /**
   * A list of Super Peer local names for another peer. Excludes the peer
   * itself. The Host Cache returns a random selection of peers, to introduce
   * evenness in the network.
   *
   * @param peer Requester (we want to exclude them from the list.)
   * @return semicolon-delimited list of local peer names.
   */
  public String getNeighboursFor(AID peer) {
    ArrayList<String> neighbours = new ArrayList<String>();
    List<AID> randomised = new ArrayList<AID>(peerList.keySet());
    Collections.shuffle(randomised);
    for (AID connectedPeer : randomised) {
      if (neighbours.size() > MAX_NEIGHBOURS) { break; }
      if (connectedPeer == peer) { continue; }
      if (peerList.get(connectedPeer)) {
        neighbours.add(connectedPeer.getLocalName());
      }
    }
    return StringUtils.join(neighbours, ';');
  }

}

