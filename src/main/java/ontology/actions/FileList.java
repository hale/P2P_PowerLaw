package ontology.actions;

import jade.content.AgentAction;

/**
 * A list of filenames, to be sent to a super peer for indexing.
 */
public class FileList implements AgentAction {

  private String sharedFiles;

  public String getSharedFiles() {
    return sharedFiles;
  }

  public void setSharedFiles(String sharedFiles) {
    this.sharedFiles = sharedFiles;
  }
}
