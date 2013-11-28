package ontology.actions;

import jade.content.AgentAction;
import jade.core.AID;

import java.util.ArrayList;

/**
 * Host Cache sends a list of Super Peers.
 */
public class SendNeighboursResponseAction implements AgentAction {

  private String peerList;

  public String getPeerList() {
    return peerList;
  }

  public void setPeerList(String peers) {
    this.peerList = peers;
  }
}
