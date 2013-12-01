package ontology.actions;

import jade.content.AgentAction;

/**
 * Created with IntelliJ IDEA.
 * User: philiphale
 * Date: 01/12/2013
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
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
