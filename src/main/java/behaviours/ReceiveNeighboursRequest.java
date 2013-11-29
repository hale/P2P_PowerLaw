package behaviours;

import agents.HostCache;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.NeighboursResponse;
import ontology.actions.RequestNeighbours;

/**
 * The Host Cache continually waits for a REQUEST_NEIGHBOURS message.
 * On receipt of such a message, it adds the sender to the list of Peers
 * in the network, and returns a list of neighbours.
 */
public class ReceiveNeighboursRequest extends BasicAgentBehaviour {

  public ReceiveNeighboursRequest(HostCache hc) {
    super(hc);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(RequestNeighbours.class);
    ACLMessage msg = myAgent.receive(mt);

    if (msg != null) {
      RequestNeighbours action = (RequestNeighbours) actionFor(msg);
      AID sender = msg.getSender();

      // add peer to network
      hostCache().addPeer(sender, action.getIsSuper());

      // reply to the message with a list of peers from the host cache.
      NeighboursResponse response = new NeighboursResponse();
      String neighbours = hostCache().getNeighboursFor(sender);
      response.setPeerList(neighbours);
      basicAgent().sendMessage(ACLMessage.INFORM, response, sender);
    }
  }// Never finish

  private HostCache hostCache() {
    return (HostCache) myAgent;
  }
}
