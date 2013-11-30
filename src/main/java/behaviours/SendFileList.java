package behaviours;

import agents.OrdinaryPeer;
import agents.Peer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.actions.FileList;

/**
 * Assumes no disconnection. Once we have a connected peer, send the file list and finish.
 */
public class SendFileList extends BasicPeerBehaviour {

  public SendFileList(Peer peer) {
    super(peer);
  }

  @Override
  public void action() {
    if (myPeer().isConnected()) {
      AID connectedPeer = myOrdinaryPeer().getConnectedPeer();
      String fileList = myOrdinaryPeer().getSharedFiles();
      FileList action = new FileList();
      action.setSharedFiles(fileList);
      basicAgent().sendMessage(ACLMessage.INFORM, action, connectedPeer);
      finished = true;
    }
  }

  private OrdinaryPeer myOrdinaryPeer() {
    return (OrdinaryPeer) myPeer();
  }

}
