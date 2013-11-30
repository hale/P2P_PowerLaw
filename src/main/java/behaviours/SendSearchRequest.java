package behaviours;

import agents.Peer;
import agents.SuperPeer;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import ontology.actions.RequestSearch;

/**
 * Searches for file.  Encapsulation slightly breaks down here,
 * since we intervene and search the local index for Super Peers.
 */
public class SendSearchRequest extends BasicPeerBehaviour {
  public SendSearchRequest(Peer p) {
    super(p);
  }

  @Override
  public void action() {
    if (myPeer().hasWantedFile()) {
      String wantedFile = myPeer().getWantedFile();
      if (myAgent instanceof SuperPeer) {
        // search local index for wanted file and send file request message to peer with file.
      } else {
        AID superPeer = myPeer().getConnectedPeer();
        RequestSearch action = new RequestSearch();
        action.setFile(wantedFile);
        basicAgent().sendMessage(ACLMessage.REQUEST, action, superPeer);
      }
    } else {
      finished = true;
    }

  }

}

