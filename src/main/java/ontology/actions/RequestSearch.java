package ontology.actions;

import jade.content.AgentAction;

/**
 * Search request for a file.
 */
public class RequestSearch implements AgentAction {

  private String file;
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
