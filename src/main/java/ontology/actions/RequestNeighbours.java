package ontology.actions;

import jade.content.AgentAction;

/**
 * Host Cache supplies some Super Peer AIDs.
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
