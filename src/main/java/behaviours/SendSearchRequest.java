package behaviours;

import agents.BasicAgent;
import agents.Peer;
import agents.SuperPeer;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.actions.FileRequest;
import ontology.actions.RequestSearch;

/**
 * Searches for file.  Encapsulation slightly breaks down here,
 * since we intervene and search the local index for Super Peers.
 */
public class SendSearchRequest extends TickerBehaviour {

  public SendSearchRequest(Peer p, int delay) {
    super(p, delay);
  }

  @Override
  public void onTick() {
    if (myPeer().hasWantedFile() && myPeer().isConnected()) {
      String wantedFile = myPeer().getWantedFile();
      if (myAgent instanceof SuperPeer && mySuperPeer().canLocate(wantedFile)) {
        String result = mySuperPeer().peerWith(wantedFile);
        FileRequest request = new FileRequest();
        request.setFile(wantedFile);
        basicAgent().sendMessage(ACLMessage.REQUEST, request, new AID(result, AID.ISLOCALNAME));
      } else {
        AID superPeer = myPeer().closestConnectedPeer(wantedFile);
        RequestSearch action = new RequestSearch();
        action.setFile(wantedFile);
        myPeer().sendMessage(ACLMessage.REQUEST, action, superPeer);
      }
    }
  }

  private BasicAgent basicAgent() {
    return (BasicAgent) myAgent;
  }

  private Peer myPeer() {
    return (Peer) myAgent;
  }

  private SuperPeer mySuperPeer() {
    return (SuperPeer) myAgent;
  }

}

