package agents;

import behaviours.ReceiveNeighboursRequestBehaviour;
import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontology.P2POntology;
import ontology.P2PVocabulary;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Host Cache has a single behaviour, responsible for adding the peers to a list of super peers.
 */
public class HostCache extends Agent implements P2PVocabulary {

  private static final int MAX_NEIGHBOURS = 5;
  private Codec codec = new SLCodec();
  private Ontology ontology = P2POntology.getInstance();

  public static String NAME = "HOST_CACHE";

  private HashMap<AID,Boolean> peerList = new HashMap<AID, Boolean>();

  @Override
  protected void setup() {
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontology);
    addBehaviour(new ReceiveNeighboursRequestBehaviour(this));
  }

  public boolean hasPeer(AID peer) {
    return peerList.containsKey(peer);
  }

  public void addPeer(AID peer, boolean isSuper) {
    peerList.put(peer, isSuper);
  }

  /**
   * @return Subset of peerList, where peer isSuper, set of size smaller or equal to MAX_NEIGHBOURS.
   */
  public ArrayList<AID> getNeighboursFor(AID peer) {
    ArrayList<AID> neighbours = new ArrayList<AID>();
    for (AID peerID : peerList.keySet()) {
      if (neighbours.size() > MAX_NEIGHBOURS) { break; }
      if (peerList.get(peerID)) {
        neighbours.add(peerID);
      }
    }
    return neighbours;
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

