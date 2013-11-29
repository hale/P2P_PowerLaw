package behaviours;

import ontology.actions.NeighboursResponse;
import ontology.actions.RequestNeighbours;
import agents.HostCache;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

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

      // add the peer to the host cache peerList
      if (!hostCache().hasPeer(sender)) {
        hostCache().addPeer(sender, action.getIsSuper());
      }

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