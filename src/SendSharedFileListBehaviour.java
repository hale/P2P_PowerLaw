import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

/**
 * Agents implementing this send their list of files to connected Super Peers.
 */
public class SendSharedFileListBehaviour extends PeerBehaviour {

  private boolean finished = false;

  public SendSharedFileListBehaviour(Peer p) {
    super(p);
  }

  public void action() {
    sendSharedFileList();
    finished = true;
  }

  public boolean done() { return finished; }

  private void sendSharedFileList() {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM );
    Message msgContents = new Message(MessageType.SEND_FILE_LIST, myPeer.getFileList());

    try {
      msg.setContentObject(msgContents);
    } catch (IOException e) {
      e.printStackTrace();
    }

    msg.addReceiver(myPeer.getNextPeer().getAID());
    myPeer.send(msg);
  }


}

