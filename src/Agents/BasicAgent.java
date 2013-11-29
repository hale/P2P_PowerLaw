package agents;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontology.P2POntology;
import ontology.P2PVocabulary;

/**
 * A common set of behaviours for all agents in the system.
 */
public abstract class BasicAgent extends Agent implements P2PVocabulary {
  private Codec codec = new SLCodec();
  private Ontology ontology = P2POntology.getInstance();

  @Override
  protected void setup() {
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
  }


}
