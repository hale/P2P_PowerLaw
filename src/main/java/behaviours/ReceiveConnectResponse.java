package behaviours;

import agents.Peer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.ConnectResponse;

/**
 * Reply from a Super Peer. If response is positive, at this point peer has joined the network.
 */
public class ReceiveConnectResponse extends BasicPeerBehaviour {

  public ReceiveConnectResponse(Peer peer) {
    super(peer);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(ConnectResponse.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      AID sender = msg.getSender();
      ConnectResponse response = (ConnectResponse) actionFor(msg);
      // super peer has been contacted, so is no longer known or pending
      myPeer().removeKnownPeer(sender);
      myPeer().removePendingPeer(sender);
      if (response.getIsSuccess()) {
        myPeer().addConnectedPeer(sender);
      }
    }
  }
}
