package ontology.actions;

import jade.content.AgentAction;

/**
 * Peers ask the Host Cache for Super Peers to connect to.
 */
public class RequestNeighbours implements AgentAction {

  private boolean isSuper;

  public boolean getIsSuper() {
    return isSuper;
  }

  public void setIsSuper(boolean aSuper) {
    isSuper = aSuper;
  }


}
