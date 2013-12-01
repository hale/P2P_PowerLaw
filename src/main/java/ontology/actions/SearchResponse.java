package ontology.actions;

import jade.content.AgentAction;

/**
 */
public class SearchResponse implements AgentAction {

  private String file;
  private String peer;
  private String senderStack;

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getPeer() {
    return peer;
  }

  public void setPeer(String peer) {
    this.peer = peer;
  }

  public String getSenderStack() {
    return senderStack;
  }

  public void setSenderStack(String senderStack) {
    this.senderStack = senderStack;
  }
}
