package behaviours;

import agents.BasicAgent;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.RequestNeighbours;

/**
 * Provides access to basicAgent().sendMessage()
 */
public abstract class BasicAgentBehaviour extends SimpleBehaviour {

  protected boolean finished = false;

  protected BasicAgentBehaviour(Agent a) {
    super(a);
  }

  protected BasicAgent basicAgent() {
    return (BasicAgent) myAgent;
  }

  @Override
  public boolean done() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return finished;
  }

  /**
   * Only match message if it is of type action
   */
  protected MessageTemplate templateFor(Class action) {
    return new MessageTemplate(new MessageTemplate.MatchExpression() {
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
        return action instanceof RequestNeighbours;
      }
    });
  }

  /**
   * Given a message, returns the Action in its contents payload.
   */
  protected Concept actionFor(ACLMessage msg) {
    ContentElement content = null;
    try {
      content = myAgent.getContentManager().extractContent(msg);
    } catch (Codec.CodecException e) {
      e.printStackTrace();
    } catch (OntologyException e) {
      e.printStackTrace();
    }
    return ((Action)content).getAction();
  }

}
