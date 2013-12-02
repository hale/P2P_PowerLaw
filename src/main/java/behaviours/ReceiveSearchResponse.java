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
      // if the sender stack is empty, this message is for us
      ArrayList<String> senderStack = new ArrayList<String>(Arrays.asList(StringUtils.split(response.getSenderStack(), ';')));
      if (senderStack.isEmpty()) {
//        logger.log(Level.INFO, myPeer().getLocalName()+" has found someone who has file they want");
        AID result = new AID(response.getPeer(), AID.ISLOCALNAME);
        String wantedFile = response.getFile();
        // ask for the file
        FileRequest request = new FileRequest();
        request.setFile(wantedFile);
        basicAgent().sendMessage(ACLMessage.REQUEST, request, result);
      } else {
        // we must be a super peer, and need to pass the message along
        AID nextPeer = new AID(senderStack.remove(senderStack.size()-1), AID.ISLOCALNAME);
        response.setSenderStack(StringUtils.join(senderStack, ';'));
        basicAgent().sendMessage(ACLMessage.INFORM, response, nextPeer);

      }
    }
  }
}
