package behaviours;

import agents.Peer;
import jade.core.Agent;

/**
 * Methods for all Peers
 */
abstract class BasicPeerBehaviour extends BasicAgentBehaviour {

  BasicPeerBehaviour(Agent a) {
    super(a);
  }

  /**
   * Sugar for casting myAgent to Peer. Isolates errors and forces ClassCastException
   */
  Peer myPeer() {
    return (Peer) myAgent;
  }
}
