import jade.core.behaviours.SimpleBehaviour;

/**
 * Supported behaviours for Peers.
 */
public abstract class PeerBehaviour extends SimpleBehaviour {

  protected Peer myPeer = (Peer) myAgent;

  protected PeerBehaviour(Peer peer) {
    super(peer);
  }

}
