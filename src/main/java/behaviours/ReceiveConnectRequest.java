package behaviours;

import agents.SuperPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.ConnectResponse;
import ontology.actions.RequestConnect;

/**
 * SuperPeers manage OrdinaryPeers - they forward search requests and have a list of their shared files.
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
        mySuperPeer().addConnectedOrdinaryPeer(sender);
        response.setIsSuccess(true);
      } else {
//        logger.log(Level.WARNING, mySuperPeer().getLocalName()+" cannot accept new peers");
        response.setIsSuccess(false);
      }
      basicAgent().sendMessage(ACLMessage.INFORM, response, sender);
    }
  }

  public SuperPeer mySuperPeer() {
    return (SuperPeer) myPeer();
  }
}
