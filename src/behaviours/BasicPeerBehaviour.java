package behaviours;

import agents.Peer;
import jade.core.Agent;

/**
 * Common behaviour amongst all peer behaviours.
 */
public abstract class BasicPeerBehaviour extends BasicAgentBehaviour {

  protected BasicPeerBehaviour(Agent a) {
    super(a);
  }

  protected Peer myPeer() {
    return (Peer) myAgent;
  }
}
