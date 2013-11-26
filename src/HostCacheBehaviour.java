import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Supported behaviours for the Host Cache.
 */
public abstract class HostCacheBehaviour extends SimpleBehaviour {

  protected HostCache myHostCache = (HostCache) myAgent;

  public HostCacheBehaviour(Agent a) {
    super(a);
  }
}
