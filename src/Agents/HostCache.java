package agents;

import behaviours.ReceiveNeighboursRequest;
import jade.core.AID;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Host Cache has a single behaviour, responsible for adding the peers to a list of super peers.
 */
public class HostCache extends BasicAgent {

  public static final int MAX_NEIGHBOURS = 1;
  public static String NAME = "HOST_CACHE";
  private HashMap<AID,Boolean> peerList = new HashMap<AID, Boolean>();

  @Override
  protected void setup() {
    super.setup();
    addBehaviour(new ReceiveNeighboursRequest(this));
  }

  /**
   * Adds the peer to the network (but only if it isn't already present)
   */
  public void addPeer(AID peer, boolean isSuper) {
    peerList.put(peer, isSuper);
  }

  /**
   * @param peer Requester - we want to exclude them from the list.
   * @return semicolon delimited list of local peer names.
   */
  public String getNeighboursFor(AID peer) {
    ArrayList<String> neighbours = new ArrayList<String>();
    for (AID connectedPeer : peerList.keySet()) {
      if (neighbours.size() > MAX_NEIGHBOURS) { break; }
      if (connectedPeer == peer) { continue; }
      if (peerList.get(connectedPeer)) {
        neighbours.add(connectedPeer.getLocalName());
      }
    }
    return StringUtils.join(neighbours, ';');
  }

}

