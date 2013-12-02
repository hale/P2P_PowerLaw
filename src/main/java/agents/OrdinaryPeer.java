package agents;

import behaviours.SendFileList;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

/**
 * Ordinary Peers have a limited set of functionality compared with Super Peers,
 * but are more common in the network. They have less bandwidth than Super Peers.
 */
public class OrdinaryPeer extends Peer {

  public static final String NAME = "OP";

  // Ordinary peers must register their files with a Super Peer when they first join the network,
  // and once thereafter every time they acquire a new file
  private boolean needsToSendFileList = true;

  @Override
  protected void setup() {
    super.setup();
    addBehaviour(new SendFileList(this));
  }

  /**
   * @return True if the peer has only just connected, or has a new shared file. False otherwise.
   */
  public boolean needsToSendFileList() {
    return needsToSendFileList;
  }

  /**
   * Stops the peer from sending a file list message again.
   */
  public void setNeedsToSendFileList(boolean needsToSendFileList) {
    this.needsToSendFileList = needsToSendFileList;
  }

  /**
   * Inherits the behaviour of all Peers, but additionally schedules
   * a Send File List message.
   *
   * @param file The file the peer has just required.
   */
  @Override
  public void receiveFile(String file) {
    super.receiveFile(file);
    setNeedsToSendFileList(true);
  }
}

