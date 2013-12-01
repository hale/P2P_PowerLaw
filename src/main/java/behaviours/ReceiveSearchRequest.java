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
import java.util.logging.Level;

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
      RequestSearch request = (RequestSearch) actionFor(msg);
      String wantedFile = request.getFile();
      // in this super peer's index; route back the response of the peer with the file.
      if (mySuperPeer().canLocate(wantedFile)) {
        SearchResponse response = new SearchResponse();
        response.setFile(wantedFile);
        response.setPeer(mySuperPeer().peerWith(wantedFile).getLocalName());
        response.setSenderStack(request.getSenderStack());
        basicAgent().sendMessage(ACLMessage.INFORM, response, msg.getSender());
        logger.log(Level.INFO, mySuperPeer().getLocalName()+" found "+wantedFile+" for "+msg.getSender().getLocalName());
      } else { // forward to closest peer.
        // add the sender to the senderStack, so the message can find its way home.
        ArrayList<String> senderStack = new ArrayList<String>(Arrays.asList(StringUtils.split(request.getSenderStack(), ';')));
        senderStack.add(msg.getSender().getLocalName());
        request.setSenderStack(StringUtils.join(senderStack, ';'));

        AID superPeer = mySuperPeer().closestConnectedPeer(wantedFile);
        basicAgent().sendMessage(ACLMessage.REQUEST, request, superPeer);

      }

    }
  }

  private SuperPeer mySuperPeer() {
    return (SuperPeer) myPeer();
  }
}
