package agents;

import behaviours.SendNeighboursRequest;
import jade.core.AID;

import java.util.ArrayList;

/**
 * Behaviour of all peers, both Ordinary and Super
 */
public class Peer extends BasicAgent {

  private static final int MIN_PEERS = 1;
  public static String NAME = "PEER";
  private ArrayList<AID> knownPeers = new ArrayList<AID>();
  private boolean isSuper;

  @Override
  protected void setup() {
    super.setup();
    Object[] args = getArguments();
    isSuper = (Boolean) args[0];
    addBehaviour(new SendNeighboursRequest(this));
  }

  public boolean needsKnownPeers() {
    return (knownPeers.size() < MIN_PEERS);
  }

  public boolean isSuper() {
    return isSuper;
  }
}
