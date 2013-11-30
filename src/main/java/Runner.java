import agents.HostCache;
import agents.OrdinaryPeer;
import agents.Peer;
import agents.SuperPeer;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;

/**
 * Runs the P2P simulation
 *
 * java jade.Boot -agents runner:Runner(options)
 *
 * The options order is:
 *     ordinaryPeerCount, superPeerCount, neighboursCount
 *
 * neighboursCount defines how many peers the Host Cache returns
 * when asked to supply the addresses of connected Super Nodes.
 */
public class Runner extends Agent {
  protected void setup() {
    try {
      ContainerController container = getContainerController();

      Configuration config = new PropertiesConfiguration("src/P2PPowerLaw.properties");
      int ordinaryPeerCount = config.getInt("peers.ordinary");
      int superPeerCount = config.getInt("peers.super");
      int maxNeighbours = config.getInt("host_cache.max_neighbours");
      int maxPeersForSuperPeer = config.getInt("super_peer.max_peers");
      int minConnectedPeers = config.getInt("peer.min_connected_peers");
      int maxConnectedPeers = config.getInt("peer.max_connected_peers");
      Object[] agentArgs;

      AgentController peerController;
      AgentController hostCacheController;

      agentArgs = new Object[]{maxNeighbours};
      hostCacheController = container.createNewAgent(
          HostCache.NAME, HostCache.class.getName(), agentArgs
      );
      hostCacheController.start();

      // settings for all peers
      agentArgs = new Object[]{minConnectedPeers, maxConnectedPeers};

      // instantiate super peers
      agentArgs = ArrayUtils.addAll(agentArgs, new Object[]{maxPeersForSuperPeer});
      for (int i = 0; i < superPeerCount; i++) {
        peerController = container.createNewAgent(
            SuperPeer.NAME+i, SuperPeer.class.getName(), agentArgs
        );
        peerController.start();
      }

      // give us some time to configure the sniffer....
      Thread.sleep(15000);

      // instantiate ordinary peers
      agentArgs = ArrayUtils.addAll(agentArgs, new Object[]{});
      for (int i = 0; i < ordinaryPeerCount; i++) {
        peerController = container.createNewAgent(
            OrdinaryPeer.NAME+i, OrdinaryPeer.class.getName(), agentArgs
        );
        peerController.start();
      }

    } catch (StaleProxyException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
  }
}
