package behaviours;

import agents.Runner;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.NetworkStats;

/**
 * Runner receives stats package from peers and adds the data to the stats collections
 */
public class ReceiveNetworkStats extends BasicAgentBehaviour {


  public ReceiveNetworkStats(Agent a) {
    super(a);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(NetworkStats.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      NetworkStats stats = (NetworkStats) actionFor(msg);
      boolean isConnected = stats.getIsConnected();
      boolean hasFoundFiles = stats.getHasFoundFiles();
      int cumMsgCount = stats.getCumMsgCount();
      myRunner().updateStats(msg.getSender(), isConnected, hasFoundFiles, cumMsgCount);
    }
  }

  private Runner myRunner() {
    return (Runner) myAgent;
  }

}
