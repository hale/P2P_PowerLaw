package behaviours;

import agents.Peer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.ConnectResponse;

/**
 * Connect Request reply from a Super Peer. If response is positive, at this point peer has joined the network.
 *
 * Peer additionally sends some stats to the Runner, so that it knows this peer is Connected.
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
      myPeer().removePendingPeer(sender);
      if (response.getIsSuccess()) {
        myPeer().addConnectedPeer(sender);
        myPeer().sendStats();
      }
    }
  }
}
