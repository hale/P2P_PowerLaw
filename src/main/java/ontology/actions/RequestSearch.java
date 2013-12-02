package ontology.actions;

import jade.content.AgentAction;

/**
 * Search request for a file. SenderStack is added to in the event that this request needs to be forwarded to other super
 * peers in the network.
 */
public class RequestSearch implements AgentAction {

  private String file;

  // Semicolon delimited list of peer local names
  private String senderStack = "";

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getSenderStack() {
    return senderStack;
  }

  public void setSenderStack(String senderStack) {
    this.senderStack = senderStack;
  }
}
