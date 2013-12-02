package behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.FileRequest;
import ontology.actions.FileResponse;

/**
 * Responds by sending the file back.
 */
public class ReceiveFileRequest extends BasicPeerBehaviour {

  public ReceiveFileRequest(Agent a) {
    super(a);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(FileRequest.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      FileRequest request = (FileRequest) actionFor(msg);
      String wantedFile = request.getFile();
      String file = myPeer().getFile(wantedFile);
      FileResponse response = new FileResponse();
      response.setFile(file);
      basicAgent().sendMessage(ACLMessage.INFORM, response, msg.getSender());
    }
  }
}
