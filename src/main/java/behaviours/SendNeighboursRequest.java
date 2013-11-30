package behaviours;

import agents.HostCache;
import agents.Peer;
import agents.SuperPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import ontology.actions.RequestNeighbours;

/**
 *  When peers join the network (or need more contactable neighbours),
 *  they request Super Peer AIDs from the Host Cache.
 */
public class SendNeighboursRequest extends BasicAgentBehaviour {
  public SendNeighboursRequest(Peer peer) {
    super(peer);
  }

  @Override
  public void action() {
    if (myPeer().needsKnownPeers() && !myPeer().hasRequestedPeers()) {
      RequestNeighbours action = new RequestNeighbours();
      action.setIsSuper(myPeer() instanceof SuperPeer);
      AID hostCache = new AID( HostCache.NAME, AID.ISLOCALNAME);
      basicAgent().sendMessage(ACLMessage.REQUEST, action, hostCache);
      myPeer().setRequestedPeers(true);
      logger.log(Logger.INFO, myPeer().getLocalName() + " asks HostCache for peers");
    }
  }

  private Peer myPeer() {
    return (Peer) myAgent;
  }
}
