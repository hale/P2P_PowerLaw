package behaviours;

import agents.OrdinaryPeer;
import agents.Peer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.actions.FileList;

/**
 * Ordinary peers send a file list when they join the network, and thereafter
 * whenever their sharedFiled list changes
 */
public class SendFileList extends BasicPeerBehaviour {

  public SendFileList(Peer peer) {
    super(peer);
  }

  @Override
  public void action() {
    if (myPeer().isConnected() && myOrdinaryPeer().needsToSendFileList()) {
      AID connectedPeer = myOrdinaryPeer().getConnectedPeer();
      String fileList = myOrdinaryPeer().getSharedFiles();
      FileList action = new FileList();
      action.setSharedFiles(fileList);
      basicAgent().sendMessage(ACLMessage.INFORM, action, connectedPeer);
      myOrdinaryPeer().setNeedsToSendFileList(false);
    }
  }

  private OrdinaryPeer myOrdinaryPeer() {
    return (OrdinaryPeer) myPeer();
  }

}
