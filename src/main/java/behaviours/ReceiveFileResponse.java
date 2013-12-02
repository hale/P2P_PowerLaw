package behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.FileResponse;

/**
 * Completes the process of acquiring files - the peer at this point has a file.
 * Additionally, sends a stats message to the Runner so that it knows the peer
 * has the file.
 */
public class ReceiveFileResponse extends BasicPeerBehaviour {

  public ReceiveFileResponse(Agent a) {
    super(a);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(FileResponse.class);
    ACLMessage msg = basicAgent().receive(mt);
    if( msg != null) {
      FileResponse response = (FileResponse) actionFor(msg);
      String file = response.getFile();
      myPeer().receiveFile(file);
      myPeer().sendStats();
    }
  }
}
