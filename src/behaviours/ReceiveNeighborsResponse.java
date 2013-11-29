package behaviours;

import agents.Peer;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.NeighboursResponse;

/**
 * Response from the Host Cache to the Neighbours Request
 */
public class ReceiveNeighborsResponse extends BasicAgentBehaviour {

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
      myPeer().setRequestedPeers(false); // to ensure we can receive more peers
    }


  }

  private Peer myPeer() {
    return (Peer) myAgent;
  }
}

