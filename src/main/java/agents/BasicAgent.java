package agents;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import ontology.P2POntology;
import ontology.P2PVocabulary;

import java.util.logging.Level;

/**
 * A common set of behaviours for all agents in the system.
 */
public abstract class BasicAgent extends Agent implements P2PVocabulary {
  private Codec codec = new SLCodec();
  private Ontology ontology = P2POntology.getInstance();

  protected Logger logger = Logger.getJADELogger(this.getClass().getName());

  protected Object[] args;

  // cumulative count of the number of messages sent by all agents extending BasicAgent
  private long sentMessages = 0;

  @Override
  protected void setup() {
    args = getArguments();
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontology);
  }

  public void sendMessage(int performative, AgentAction action, AID recipient) {
    ACLMessage msg = new ACLMessage(performative);
    msg.setLanguage(codec.getName());
    msg.setOntology(ontology.getName());
    try {
      getContentManager().fillContent(msg, new Action(recipient, action));
    } catch (Exception e) {
      e.printStackTrace();
    }
    msg.addReceiver(recipient);
    send(msg);

    sentMessages++;
    // log every 1000 messages
    if (sentMessages % 100 == 0) {
      logger.log(Level.INFO, sentMessages+" messages sent");
    }


  }

}
