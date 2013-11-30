package agents;

import behaviours.ReceiveConnectRequest;
import jade.core.AID;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 */
public class SuperPeer extends Peer {

  private static final int MAX_CONNECTED_ORDINARY_PEERS = 2;
  public static String NAME = "SUPER PEER";
  private ArrayList<AID> connectedOrdinaryPeers = new ArrayList<AID>();

  @Override
  protected void setup() {
    super.setup();
    addBehaviour(new ReceiveConnectRequest(this));
  }

  public boolean canAcceptConnectRequest() {
    return connectedOrdinaryPeers.size() < MAX_CONNECTED_ORDINARY_PEERS;
  }

  public void addConnectedOrdinaryPeer(AID peer) {
    connectedOrdinaryPeers.add(peer);
    logger.log(Level.INFO, getLocalName() + " adds " + peer.getLocalName());
  }
}
