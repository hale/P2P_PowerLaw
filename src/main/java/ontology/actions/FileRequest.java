package ontology.actions;

import jade.content.AgentAction;

/**
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
