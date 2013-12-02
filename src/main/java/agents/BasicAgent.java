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
 * A common set of behaviours for all agents in the system. Abstracts details like
 * setting the Ontology, and provides a single location for sending messages.
 *
 * Classes extending BasicAgent will also periodically send statistical information
 * to the Runner.
 */
public abstract class BasicAgent extends Agent implements P2PVocabulary {

  // All peers must be associated with the Ontology files in order
  // to send and receive messages.
  private final Codec codec = new SLCodec();
  private final Ontology ontology = P2POntology.getInstance();

  // Boilerplate for logging.
  protected Logger logger = Logger.getJADELogger(this.getClass().getName());


  // cumulative count of the number of messages sent by this agent
  private final HashMultiset<String> messageStats = HashMultiset.create();

  // cumulative count of the messages sent to the Runner - must be excluded from the
  // main count.
  private int sentStatsMessages = 0;

  // Every sendStatsEvery number of messages send by this agent, send
  // an additional message to the Runner with info about this peer.
  private int sendStatsEvery;

  // On creation, peers can be assigned optional args.
  Object[] args;

  @Override
  protected void setup() {
    args = getArguments();
    sendStatsEvery = (Integer) args[0];
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontology);
  }

  /**
   * Wraps send(), making it slightly easier for peers to send messages.
   *
   * @param performative Integer representing the FIPA name of this message.
   * @param action Messages are filled with an action from the Ontology.
   * @param recipient Who to send the message to.
   */
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

    // keep a simple count of how many messages this peer has sent
    messageStats.add(action.getClass().getSimpleName(), 1);

    // periodically inform the Runner about how many messages have been sent
    if (messageStats.size() % sendStatsEvery == 0) { sendStats(); }
  }

  /**
   * Sends statistical information to the Runner.
   */
  public void sendStats() {
    NetworkStats stats = new NetworkStats();
    stats.setCumMsgCount(messageStats.size() - sentStatsMessages);
    if (this instanceof Peer) { // Host Cache doesn't count here
      stats.setHasFoundFiles(!myPeer().hasWantedFile());
      stats.setIsConnected(myPeer().isConnected());
    }
    sendMessage(ACLMessage.INFORM, stats, new AID(Runner.NAME, AID.ISLOCALNAME));
    sentStatsMessages++;
  }

  /**
   * Shortcut for casting to a Peer
   */
  private Peer myPeer() {
    return (Peer) this;
  }

}
