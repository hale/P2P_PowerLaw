package ontology.actions;

import jade.content.AgentAction;

/**
 * YES or NO
 */
public class ConnectResponse implements AgentAction {

  private boolean isSuccess;

  public boolean getIsSuccess() {
    return isSuccess;
  }

  public void setIsSuccess(boolean aSuccess) {
    isSuccess = aSuccess;
  }
}
