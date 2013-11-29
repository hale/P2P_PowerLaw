package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontology.actions.RequestConnect;

import java.util.logging.Level;

/**
 * Establishes a connection with a Super Peer
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
      logger.log(Level.INFO, myPeer().getLocalName() + " request connect to " + superPeer.getLocalName());
    }
  }
}
