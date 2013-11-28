package ontology.actions;

import jade.content.AgentAction;

/**
 * Peers ask the Host Cache for Super Peers to connect to.
 */
public class RequestNeighboursAction implements AgentAction {

  private boolean isSuper;

  public boolean isSuper() {
    return isSuper;
  }

  public void setSuper(boolean aSuper) {
    isSuper = aSuper;
  }


}
