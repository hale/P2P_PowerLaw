import agents.HostCache;
import agents.Peer;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Runs the P2P simulation
 *
 * java jade.Boot -agents runner:Runner(peerCount)
 *
 * The argument given to Runner specify in turn:
 *
 * peerCount: The number of peers to create
 */
public class Runner extends Agent {
  protected void setup() {
    try {
      Object[] args = getArguments();

      ContainerController container = getContainerController();
      AgentController hostCacheController =
          container.createNewAgent( HostCache.NAME, HostCache.class.getName(), null);
      hostCacheController.start();

      // instantiate peers
      int peerCount = Integer.parseInt((String) args[0]);
      Object[] peerArgs = new Object[] {true};
      for (int i = 0; i < peerCount; i++) {
        AgentController peerController = container.createNewAgent(
            Peer.NAME+i, Peer.class.getName(), peerArgs
        );
        peerController.start();
      }

    } catch (StaleProxyException e) {
      e.printStackTrace();
    }
  }
}
