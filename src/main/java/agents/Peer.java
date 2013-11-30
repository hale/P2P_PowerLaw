package agents;

import behaviours.*;
import jade.core.AID;
import jade.util.Logger;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Behaviour of all peers, both Ordinary and Super
 */
public abstract class Peer extends BasicAgent {

  private static int MIN_CONNECTED_PEERS;
  private static int MAX_CONNECTED_PEERS;

  private ArrayList<AID> knownPeers = new ArrayList<AID>();
  protected ArrayList<AID> connectedPeers = new ArrayList<AID>();
  private ArrayList<AID> connectPendingPeers = new ArrayList<AID>();

  protected ArrayList<String> sharedFiles;

  private boolean hasRequestedPeers;

  @Override
  protected void setup() {
    super.setup();
    MIN_CONNECTED_PEERS = (Integer) args[0];
    MAX_CONNECTED_PEERS = (Integer) args[1];
    sharedFiles = new ArrayList<String>(Arrays.asList( (String[]) args[2]));
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

  /**
   * Setting a MAX prevents the peer from spamming connect requests
   */
  public boolean needsConnectedPeers() {
    return (connectedPeers.size() + connectPendingPeers.size()) < MIN_CONNECTED_PEERS &&
        (connectedPeers.size() + connectPendingPeers.size()) <= MAX_CONNECTED_PEERS;
  }

  public AID getKnownPeer() {
    return knownPeers.get(0);
  }

  public void addConnectPendingPeer(AID peer) {
    connectPendingPeers.add(peer);
    knownPeers.remove(peer);
  }

  public void removePendingPeer(AID peer) {
    connectPendingPeers.remove(peer);
  }

  public void addConnectedPeer(AID peer) {
    connectedPeers.add(peer);
    logger.log(Level.INFO, getLocalName() + " adds " + peer.getLocalName() + " to connectedPeers. \n" +
        getLocalName()+" now has this many connectedPeers: " + connectedPeers.size());
  }

  public boolean isConnected() {
    return !connectedPeers.isEmpty();
  }
}
