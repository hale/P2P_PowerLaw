package agents;

import behaviours.ReceiveConnectRequest;
import jade.core.AID;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 */
public class SuperPeer extends Peer {

  private static int MAX_CONNECTED_ORDINARY_PEERS;
  public static String NAME = "SUPER PEER";
  private ArrayList<AID> connectedOrdinaryPeers = new ArrayList<AID>();

  @Override
  protected void setup() {
    super.setup();
    MAX_CONNECTED_ORDINARY_PEERS = (Integer) args[3]; // 1 because 0,1,2 used in Peer. Sorry.
    addBehaviour(new ReceiveConnectRequest(this));
  }

  public boolean canAcceptConnectRequest() {
    return connectedOrdinaryPeers.size() < MAX_CONNECTED_ORDINARY_PEERS;
  }

  public void addConnectedOrdinaryPeer(AID peer) {
    connectedOrdinaryPeers.add(peer);
//    logger.log(Level.INFO, getLocalName() + " adds " + peer.getLocalName());

  }
}
