package behaviours;

import agents.SuperPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.RequestSearch;
import ontology.actions.SearchResponse;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Search requests are either responded to directly (if we have the file)
 * or forwarded using document routing.
 */
public class ReceiveSearchRequest extends BasicPeerBehaviour {

  public ReceiveSearchRequest(SuperPeer a) {
    super(a);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(RequestSearch.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      RequestSearch action = (RequestSearch) actionFor(msg);

      String wantedFile = action.getFile();
      if (mySuperPeer().canLocate(wantedFile)) {
        SearchResponse response = new SearchResponse();
        response.setFile(wantedFile);
        basicAgent().sendMessage(ACLMessage.INFORM, action, msg.getSender());
      } else {
        // add the sender to the senderStack.  If we can't reply, the next peer will need this.
        ArrayList<String> senderStack = (ArrayList<String>) Arrays.asList(StringUtils.split(action.getSenderStack(), ';'));
        senderStack.add(msg.getSender().getLocalName());
        action.setSenderStack(StringUtils.join(senderStack, ';'));

        // forward to closest peer.  IE connectedPeer with an AID closest to the file.
        AID fileID = new AID(wantedFile, AID.ISLOCALNAME);
        AID superPeer = mySuperPeer().closestConnectedPeer(fileID);

      }

    }
  }

  private SuperPeer mySuperPeer() {
    return (SuperPeer) myPeer();
  }
}
