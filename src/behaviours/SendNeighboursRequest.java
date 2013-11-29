package behaviours;

import agents.BasicAgent;
import agents.HostCache;
import agents.Peer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.actions.RequestNeighbours;

/**
 * Created with IntelliJ IDEA.
 * User: philiphale
 * Date: 28/11/2013
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */
public class SendNeighboursRequest extends BasicAgentBehaviour {
  public SendNeighboursRequest(Peer peer) {
    super(peer);
  }

  @Override
  public void action() {
    if (myPeer().needsKnownPeers()) {
      RequestNeighbours action = new RequestNeighbours();
      action.setIsSuper(myPeer().isSuper());
      AID hostCache = new AID( HostCache.NAME, AID.ISLOCALNAME);
      basicAgent().sendMessage(ACLMessage.REQUEST, action, hostCache);
    }
  }

  private Peer myPeer() {
    return (Peer) myAgent;
  }
}
