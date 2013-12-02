package agents;

import behaviours.SendFileList;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

/**
 */
public class OrdinaryPeer extends Peer {

  public static String NAME = "OP";
  private boolean needsToSendFileList = true;

  @Override
  protected void setup() {
    super.setup();
    addBehaviour(new SendFileList(this));
  }

  public AID getConnectedPeer() {
    return connectedPeers.get(0);
  }

  public String getSharedFiles() {
    return StringUtils.join(sharedFiles, ';');
  }

  public boolean needsToSendFileList() {
    return needsToSendFileList;
  }

  public void setNeedsToSendFileList(boolean needsToSendFileList) {
    this.needsToSendFileList = needsToSendFileList;
  }

  @Override
  public void receiveFile(String file) {
    super.receiveFile(file);
    setNeedsToSendFileList(true);

  }
}

