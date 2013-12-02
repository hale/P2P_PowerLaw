package behaviours;

import agents.Peer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.FileRequest;
import ontology.actions.SearchResponse;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Receives a Search Response message, and responds in one of several ways. If there
 * is an empty senderStack, then the message must be intended for us so we send a FileRequest message
 * to the peer with the file. On the other hand, if there is a senderStack then we need to
 * pass this message along to the next person in the chain.
 */
public class ReceiveSearchResponse extends BasicPeerBehaviour {

  public ReceiveSearchResponse(Peer p) {
    super(p);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(SearchResponse.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      SearchResponse response = (SearchResponse) actionFor(msg);
      ArrayList<String> senderStack = new ArrayList<String>(Arrays.asList(StringUtils.split(response.getSenderStack(), ';')));
      if (senderStack.isEmpty()) {
        FileRequest request = new FileRequest();
        request.setFile(response.getFile());
        AID result = new AID(response.getPeer(), AID.ISLOCALNAME);
        basicAgent().sendMessage(ACLMessage.REQUEST, request, result);
      } else {
        AID nextPeer = new AID(senderStack.remove(senderStack.size()-1), AID.ISLOCALNAME);
        response.setSenderStack(StringUtils.join(senderStack, ';'));
        basicAgent().sendMessage(ACLMessage.INFORM, response, nextPeer);
      }
    }
  }
}
