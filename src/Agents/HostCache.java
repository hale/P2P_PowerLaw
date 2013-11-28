package agents;

import behaviours.ReceiveNeighboursRequestBehaviour;
import jade.core.AID;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Host Cache has a single behaviour, responsible for adding the peers to a list of super peers.
 */
public class HostCache extends BasicAgent {

  public static final int MAX_NEIGHBOURS = 5;
  public static String NAME = "HOST_CACHE";
  private HashMap<AID,Boolean> peerList = new HashMap<AID, Boolean>();

  @Override
  protected void setup() {
    super.setup();
    addBehaviour(new ReceiveNeighboursRequestBehaviour(this));
  }

  public boolean hasPeer(AID peer) {
    return peerList.containsKey(peer);
  }

  public void addPeer(AID peer, boolean isSuper) {
    peerList.put(peer, isSuper);
  }

  /**
   * @return Subset of peerList, where peer isSuper, set of size smaller or equal to MAX_NEIGHBOURS.
   *         The set is then serialised to String in order to simplify the messages.
   */
  public String getNeighboursFor(AID peer) {
    ArrayList<AID> neighbours = new ArrayList<AID>();
    for (AID connectedPeer : peerList.keySet()) {
      if (neighbours.size() > MAX_NEIGHBOURS) { break; }
      if (connectedPeer != peer && peerList.get(connectedPeer)) {
        neighbours.add(connectedPeer);
      }
    }
    return StringUtils.join(neighbours, ';');
  }

}

