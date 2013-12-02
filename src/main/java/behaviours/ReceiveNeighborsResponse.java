package behaviours;

import agents.Peer;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.NeighboursResponse;

/**
 * Adds to knownPeers the response from the Host Cache
 */
public class ReceiveNeighborsResponse extends BasicPeerBehaviour {

  public ReceiveNeighborsResponse(Peer p) {
    super(p);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(NeighboursResponse.class);
    ACLMessage msg = myAgent.receive(mt);

    if (msg != null) {
      NeighboursResponse action = (NeighboursResponse) actionFor(msg);
      String peers = action.getPeerList();
      myPeer().addKnownPeers(peers);
      myPeer().setRequestedPeers(false);
    }
  }
}

