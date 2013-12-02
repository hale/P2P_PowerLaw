package agents;

import com.google.common.collect.HashMultiset;
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
import ontology.actions.NetworkStats;

import java.util.logging.Level;

/**
 * A common set of behaviours for all agents in the system.
 */
public abstract class BasicAgent extends Agent implements P2PVocabulary {
  private Codec codec = new SLCodec();
  private Ontology ontology = P2POntology.getInstance();

  protected Logger logger = Logger.getJADELogger(this.getClass().getName());

  protected Object[] args;

  // cumulative count of the number of messages sent by this agent
  private HashMultiset<String> messageStats = HashMultiset.create();
  private int sentStatsMessages = 0;

  private int sendStatsEvery;

  @Override
  protected void setup() {
    args = getArguments();
    sendStatsEvery = (Integer) args[0];
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

    computeStatsFor(action);

    // every e.g. 100 messages, tell the inspector how many messages we've sent + other stats
    if (messageStats.size() % sendStatsEvery == 0) { sendStats(); }
  }

  private void computeStatsFor(AgentAction action) {
    String messageName = action.getClass().getSimpleName();
    messageStats.add(messageName, 1);

//    if (messageStats.count(messageName) > 1000) {
//      logger.log(Level.WARNING, getLocalName()+" is sending this a lot: "+messageName);
//    }
  }

  public void sendStats() {
    NetworkStats stats = new NetworkStats();
    stats.setCumMsgCount(messageStats.size() - sentStatsMessages);
    if (this instanceof Peer) {
      stats.setHasFoundFiles(!myPeer().hasWantedFile());
      stats.setIsConnected(myPeer().isConnected());
    }
    sendMessage(ACLMessage.INFORM, stats, new AID(Runner.NAME, AID.ISLOCALNAME));
    sentStatsMessages++;
  }

  private Peer myPeer() {
    return (Peer) this;
  }

}
