package behaviours;

import agents.BasicAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Provides access to basicAgent().sendMessage()
 */
public abstract class BasicAgentBehaviour extends SimpleBehaviour {

  protected BasicAgentBehaviour(Agent a) {
    super(a);
  }

  protected BasicAgent basicAgent() { return (BasicAgent) myAgent; }

}
