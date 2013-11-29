package agents;

import behaviours.ReceiveNeighborsResponse;
import behaviours.SendConnectRequest;
import behaviours.SendNeighboursRequest;
import jade.core.AID;
import jade.util.Logger;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

/**
 * Behaviour of all peers, both Ordinary and Super
 */
public class Peer extends BasicAgent {

  public static String NAME = "PEER";
  private boolean isSuper;

  private static final int MIN_KNOWN_PEERS = 1;
  private static final int MIN_CONNECTED_PEERS = 1;

  private ArrayList<AID> knownPeers = new ArrayList<AID>();
  private ArrayList<AID> connectedPeers = new ArrayList<AID>();
  private ArrayList<AID> connectPendingPeers = new ArrayList<AID>();

  private boolean hasRequestedPeers;

  @Override
  protected void setup() {
    super.setup();
    Object[] args = getArguments();
    isSuper = (Boolean) args[0];
    addBehaviour(new SendNeighboursRequest(this));
    addBehaviour(new ReceiveNeighborsResponse(this));
    addBehaviour(new SendConnectRequest(this));
  }

  public boolean needsKnownPeers() {
    return (knownPeers.size() < MIN_KNOWN_PEERS);
  }

  public boolean isSuper() {
    return isSuper;
  }

  public void addKnownPeers(String peers) {
    String[] peerList = StringUtils.split(peers, ';');
    for (String peer : peerList) {
      AID knownPeer = new AID(peer, AID.ISLOCALNAME);
      knownPeers.add(knownPeer);
    }
    logger.log(Logger.INFO, getLocalName() + " adds " + peerList.length + " knownPeers");
  }

  public boolean hasRequestedPeers() {
    return hasRequestedPeers;
  }

  public void setRequestedPeers(boolean requestedPeers) {
    this.hasRequestedPeers = requestedPeers;
  }

  public boolean hasKnownPeers() {
    return !knownPeers.isEmpty();
  }

  public boolean needsConnectedPeers() {
    return (connectedPeers.size() + connectPendingPeers.size()) < MIN_CONNECTED_PEERS;
  }

  public AID getKnownPeer() {
    return knownPeers.get(0);
  }

  public void addConnectPendingPeer(AID peer) {
    connectPendingPeers.add(peer);
  }
}
