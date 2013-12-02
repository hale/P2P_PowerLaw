package behaviours;

import agents.BasicAgent;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * Provides access to basicAgent().sendMessage()
 */
public abstract class BasicAgentBehaviour extends SimpleBehaviour {

  protected boolean finished = false;
  protected Logger logger = Logger.getJADELogger(this.getClass().getName());

  protected BasicAgentBehaviour(Agent a) {
    super(a);
  }

  protected BasicAgent basicAgent() {
    return (BasicAgent) myAgent;
  }

  @Override
  public boolean done() {
    return finished;
  }

  /**
   * Only match message if it is of type action
   */
  protected MessageTemplate templateFor(Class action) {
    return new MessageTemplate(new MessageTemplate.MatchExpression() {

      private Class action;

      public boolean match(ACLMessage msg) {
        ContentElement content = null;
        try {
          content = myAgent.getContentManager().extractContent(msg);
        } catch (Codec.CodecException e) {
          e.printStackTrace();
        } catch (OntologyException e) {
          e.printStackTrace();
        }
        Concept concept = ((Action)content).getAction();
        return action.isInstance(concept);
      }

      private MessageTemplate.MatchExpression init(Class aAction) {
        action = aAction;
        return this;
      }
    }.init(action));
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
