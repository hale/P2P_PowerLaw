package behaviours;

import agents.SuperPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.ConnectResponse;
import ontology.actions.RequestConnect;

/**
 * Responds to Connect Requests from other peers with either yes or no - if yes, add them
 * to a list of peers connected to the Super Peer
 */
public class ReceiveConnectRequest extends BasicPeerBehaviour {

  public ReceiveConnectRequest(SuperPeer p) {
    super(p);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(RequestConnect.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      AID sender = msg.getSender();
      ConnectResponse response = new ConnectResponse();
      if (mySuperPeer().canAcceptConnectRequest()) {
        mySuperPeer().acceptNewConnection(sender);
        response.setIsSuccess(true);
      } else {
        response.setIsSuccess(false);
      }
      basicAgent().sendMessage(ACLMessage.INFORM, response, sender);
    }
  }

  SuperPeer mySuperPeer() {
    return (SuperPeer) myPeer();
  }
}
