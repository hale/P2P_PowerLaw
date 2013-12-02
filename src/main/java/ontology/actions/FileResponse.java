package ontology.actions;

import jade.content.AgentAction;

/**
 * Simulates the transfer of a file.
 */
public class FileResponse implements AgentAction {

  private String file;

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }
}
