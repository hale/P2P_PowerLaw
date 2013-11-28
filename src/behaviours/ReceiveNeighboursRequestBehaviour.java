package behaviours;

import ontology.actions.RequestNeighboursAction;
import agents.HostCache;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
//import jade.domain.introspection.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;
import ontology.actions.SendNeighboursResponseAction;

/**
 * The Host Cache continually waits for a REQUEST_NEIGHBOURS message.
 * On receipt of such a message, it adds the sender to the list of Peers
 * in the network, and returns a list of neighbours.
 */
public class ReceiveNeighboursRequestBehaviour extends BasicAgentBehaviour {

  private boolean finished = false;

  public ReceiveNeighboursRequestBehaviour(Agent a) {
    super(a);
  }

  @Override
  public void action() {

    // Only match message if it is of type REQUEST_NEIGHBOURS
    MessageTemplate mt = new MessageTemplate(new MessageTemplate.MatchExpression() {
      @Override
      public boolean match(ACLMessage msg) {
        ContentElement content = null;
        try {
          content = myAgent.getContentManager().extractContent(msg);
        } catch (Codec.CodecException e) {
          e.printStackTrace();
        } catch (OntologyException e) {
          e.printStackTrace();
        }
        Concept action = ((Action)content).getAction();
        return action instanceof RequestNeighboursAction;
      }
    });
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {

      // add the peer to the host cache peerList
      ContentElement content = null;
      try {
        content = myAgent.getContentManager().extractContent(msg);
      } catch (Codec.CodecException e) {
        e.printStackTrace();
      } catch (OntologyException e) {
        e.printStackTrace();
      }
      RequestNeighboursAction action = (RequestNeighboursAction) ((Action)content).getAction();
      boolean senderIsSuper = action.isSuper();
      AID sender = msg.getSender();
      if (!hostCache().hasPeer(sender)) {
        hostCache().addPeer(sender, senderIsSuper);
      }

      // reply to the message with a list of peers from the host cache.
      String neighbours = hostCache().getNeighboursFor(sender);
      SendNeighboursResponseAction responseAction = new SendNeighboursResponseAction();
      responseAction.setPeerList(neighbours);
      basicAgent().sendMessage(ACLMessage.INFORM, responseAction, sender);
    }
  }// Never finish

  private HostCache hostCache() {
    return (HostCache) myAgent;
  }

  @Override
  public boolean done() {
    return finished;
  }
}
