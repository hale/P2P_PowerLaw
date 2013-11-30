package agents;

import behaviours.ReceiveConnectResponse;
import behaviours.ReceiveNeighborsResponse;
import behaviours.SendConnectRequest;
import behaviours.SendNeighboursRequest;
import jade.core.AID;
import jade.util.Logger;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Behaviour of all peers, both Ordinary and Super
 */
public abstract class Peer extends BasicAgent {

  private static int MIN_CONNECTED_PEERS;
  private int MAX_CONNECTED_PEERS;

  private ArrayList<AID> knownPeers = new ArrayList<AID>();
  private ArrayList<AID> connectedPeers = new ArrayList<AID>();
  private ArrayList<AID> connectPendingPeers = new ArrayList<AID>();

  private boolean hasRequestedPeers;

  @Override
  protected void setup() {
    super.setup();
    MIN_CONNECTED_PEERS = (Integer) args[0];
    MAX_CONNECTED_PEERS = (Integer) args[1];
    addBehaviour(new SendNeighboursRequest(this));
    addBehaviour(new ReceiveNeighborsResponse(this));
    addBehaviour(new SendConnectRequest(this));
    addBehaviour(new ReceiveConnectResponse(this));
  }

  public void addKnownPeers(String peers) {
    String[] peerList = StringUtils.split(peers, ';');
    for (String peer : peerList) {
      AID knownPeer = new AID(peer, AID.ISLOCALNAME);
      // skip if already connected
      if (connectedPeers.contains(knownPeer) || connectPendingPeers.contains(knownPeer)) { continue; }
      knownPeers.add(knownPeer);
    }
    logger.log(Logger.INFO, getLocalName() + " now has  " + knownPeers.size() + " knownPeers");
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

  public boolean needsKnownPeers() {
    return needsConnectedPeers() && knownPeers.isEmpty();
  }

  public boolean needsConnectedPeers() {
    return (connectedPeers.size() + connectPendingPeers.size()) < MIN_CONNECTED_PEERS &&
        (connectedPeers.size() + connectPendingPeers.size()) <= MAX_CONNECTED_PEERS;
  }

  public AID getKnownPeer() {
    return knownPeers.get(0);
  }

  public void addConnectPendingPeer(AID peer) {
    connectPendingPeers.add(peer);
  }

  public void removeKnownPeer(AID peer) {
    knownPeers.remove(peer);
  }

  public void removePendingPeer(AID peer) {
    connectPendingPeers.remove(peer);
  }

  public void addConnectedPeer(AID peer) {
    connectedPeers.add(peer);
    logger.log(Level.INFO, getLocalName() + " adds " + peer.getLocalName() + " to connectedPeers");
    logger.log(Level.INFO, getLocalName()+" now has this many connectedPeers: " + connectedPeers.size());
  }
}
