package agents;

import behaviours.ReceiveNeighborsResponse;
import behaviours.SendNeighboursRequest;
import jade.core.AID;
import jade.util.Logger;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Behaviour of all peers, both Ordinary and Super
 */
public class Peer extends BasicAgent {

  private static final int MIN_KNOWN_PEERS = 1;
  private static final int MAX_KNOWN_PEERS = 5;
  public static String NAME = "PEER";
  private ArrayList<AID> knownPeers = new ArrayList<AID>();
  private boolean isSuper;
  private boolean hasRequestedPeers;

  @Override
  protected void setup() {
    super.setup();
    Object[] args = getArguments();
    isSuper = (Boolean) args[0];
    addBehaviour(new SendNeighboursRequest(this));
    addBehaviour(new ReceiveNeighborsResponse(this));
  }

  public boolean needsKnownPeers() {
    return (knownPeers.size() < MIN_KNOWN_PEERS &&
        knownPeers.size() <= MAX_KNOWN_PEERS);
  }

  public boolean isSuper() {
    return isSuper;
  }

  public void addKnownPeers(String peers) {
    for (String peer : StringUtils.split(peers,';')) {
      knownPeers.add(new AID(peer, AID.ISGUID));
      logger.log(Logger.INFO, toString() + " adds a knownPeer");
    }
  }

  public boolean hasRequestedPeers() {
    return hasRequestedPeers;
  }

  public void setRequestedPeers(boolean requestedPeers) {
    this.hasRequestedPeers = requestedPeers;
  }
}
