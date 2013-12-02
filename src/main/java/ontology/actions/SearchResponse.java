package ontology.actions;

import jade.content.AgentAction;

/**
 * When a file is found, this message is sent back to the original requester.  SenderStack allows for
 * the message to be passed back to the original sender through the network.
 */
public class SearchResponse implements AgentAction {

  private String file;
  private String peer;

  // Semicolon delimited list of peer local names
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
