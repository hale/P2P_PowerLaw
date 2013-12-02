package ontology.actions;

import jade.content.AgentAction;

/**
 * Asks for some Super Peer IDs, and adds the sender to the host cache.
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
