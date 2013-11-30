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
      int maxNeighbours = config.getInt("max_neighbours");
      int maxPeersForSuperPeer = config.getInt("max_peers_for_super_peer");

      AgentController peerController;
      AgentController hostCacheController;

      hostCacheController = container.createNewAgent(
          HostCache.NAME, HostCache.class.getName(), new Object[]{maxNeighbours}
      );
      hostCacheController.start();

      // give us some time to configure the sniffer....
      Thread.sleep(10000);

      Object[] peerArgs = null;

      // instantiate ordinary peers
      for (int i = 0; i < ordinaryPeerCount; i++) {
        peerController = container.createNewAgent(
            OrdinaryPeer.NAME+i, OrdinaryPeer.class.getName(), peerArgs
        );
        peerController.start();
      }

      // instantiate super peers
      for (int i = 0; i < superPeerCount; i++) {
        peerController = container.createNewAgent(
            SuperPeer.NAME+i, SuperPeer.class.getName(), peerArgs
        );
        peerController.start();
      }

    } catch (StaleProxyException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ConfigurationException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
