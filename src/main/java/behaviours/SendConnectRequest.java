package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontology.actions.RequestConnect;

/**
 * Asks a Super Peer to connect. Sends whenever the peer has uncontacted
 * peers, and needs to establsh more connections.
 */
public class SendConnectRequest extends BasicPeerBehaviour {

  public SendConnectRequest(Agent a) {
    super(a);
  }

  @Override
  public void action() {
    if (myPeer().hasKnownPeers() && myPeer().needsConnectedPeers()) {
      RequestConnect action = new RequestConnect();
      AID superPeer = myPeer().getKnownPeer();
      basicAgent().sendMessage(ACLMessage.REQUEST, action, superPeer);
      myPeer().addConnectPendingPeer(superPeer);
    }
  }
}
