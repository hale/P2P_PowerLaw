package behaviours;

import agents.BasicAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Provides access to basicAgent().sendMessage()
 */
public abstract class BasicAgentBehaviour extends SimpleBehaviour {

  protected boolean finished = false;

  protected BasicAgentBehaviour(Agent a) {
    super(a);
  }

  protected BasicAgent basicAgent() {
    return (BasicAgent) myAgent;
  }

  @Override
  public boolean done() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return finished;
  }

}
