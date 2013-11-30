package ontology.actions;

import jade.content.AgentAction;

/**
 * Created with IntelliJ IDEA.
 * User: philiphale
 * Date: 30/11/2013
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class SearchResponse implements AgentAction {

  private String file;
  private String peer;

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
}
