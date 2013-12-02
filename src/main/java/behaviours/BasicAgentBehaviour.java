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
 * Wrapper class for most behaviours in the network. Provides ease-of-use utility
 * methods for the slighty verbose Ontology features in JADE. (Trading flexibility
 * in the process).
 */
abstract class BasicAgentBehaviour extends SimpleBehaviour {

  // set this to true in any subclass, and the behaviour will terminate.
  protected boolean finished = false;

  // provides a logger for all behaviours
  final Logger logger = Logger.getJADELogger(this.getClass().getName());

  /**
   * Delegate to SimpleBehaviour
   */
  BasicAgentBehaviour(Agent a) {
    super(a);
  }

  /**
   * Shortcut to cast myAgent to a BasicAgent
   */
  BasicAgent basicAgent() {
    return (BasicAgent) myAgent;
  }

  @Override
  public boolean done() {
    return finished;
  }

  /**
   * Provides a template which can be used to only receive messages of a
   * given type.
   *
   * @param action Class of the action to match against.
   * @return A MessageTemplate matcher to pass to receive() in behaviours.
   */
  MessageTemplate templateFor(Class action) {
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
   * Given a message, returns the Action in its body.
   */
  Concept actionFor(ACLMessage msg) {
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
