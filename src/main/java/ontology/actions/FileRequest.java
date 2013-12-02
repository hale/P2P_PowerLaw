package ontology.actions;

import jade.content.AgentAction;

/**
 * Asks a peer to reply with the file.
 */
public class FileRequest implements AgentAction  {

  private String file;

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }
}
