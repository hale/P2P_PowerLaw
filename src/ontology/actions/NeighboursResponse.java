package ontology.actions;

import jade.content.AgentAction;

/**
 * Host Cache sends a list of Super Peers.
 */
public class NeighboursResponse implements AgentAction {

  private String peerList;

  public String getPeerList() {
    return peerList;
  }

  public void setPeerList(String peers) {
    this.peerList = peers;
  }
}
