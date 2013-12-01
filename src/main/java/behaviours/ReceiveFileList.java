package behaviours;

import agents.SuperPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.actions.FileList;

/**
 * Add a peers shared file list to this super peer's file index.
 */
public class ReceiveFileList extends BasicPeerBehaviour {
  public ReceiveFileList(SuperPeer superPeer) {
    super(superPeer);
  }

  @Override
  public void action() {
    MessageTemplate mt = templateFor(FileList.class);
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      AID sender = msg.getSender();
      FileList action = (FileList) actionFor(msg);
      String fileList = action.getSharedFiles();
      mySuperPeer().addFiles(fileList, sender);
    }
  }

  private SuperPeer mySuperPeer() {
    return (SuperPeer) myPeer();
  }
}
