package ontology.actions;

import jade.content.AgentAction;

/**
 * A message send to the Runner with a bunch of information about the peer,
 * for statistics collection.
 */
public class NetworkStats implements AgentAction {

  private int cumMsgCount;
  private boolean isConnected;
  private boolean hasFoundFiles;

  public int getCumMsgCount() {
    return cumMsgCount;
  }

  public void setCumMsgCount(int cumMsgCount) {
    this.cumMsgCount = cumMsgCount;
  }

  public boolean getIsConnected() {
    return isConnected;
  }

  public void setIsConnected(boolean connected) {
    isConnected = connected;
  }

  public boolean getHasFoundFiles() {
    return hasFoundFiles;
  }

  public void setHasFoundFiles(boolean hasFoundFiles) {
    this.hasFoundFiles = hasFoundFiles;
  }

}
