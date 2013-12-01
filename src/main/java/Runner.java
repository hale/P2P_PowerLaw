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
      AgentController agentController;

      int maxPeersForSuperPeer = config.getInt("super_peer.max_peers");


      // instantiate host cache
      int maxNeighbours = config.getInt("host_cache.max_neighbours");
      Object[] hcArgs = new Object[]{maxNeighbours};
      agentController = container.createNewAgent(
          HostCache.NAME, HostCache.class.getName(), hcArgs
      );
      agentController.start();

      // settings for all peers
      int minConnectedPeers = config.getInt("peer.min_connected_peers");
      int maxConnectedPeers = config.getInt("peer.max_connected_peers");
      String[] sharedFiles = config.getStringArray("peer.shared_files");
      String[] wantedFiles = config.getStringArray("peer.wanted_files");
      Object[] peerArgs = new Object[]{minConnectedPeers, maxConnectedPeers, sharedFiles, wantedFiles};

      // instantiate super peers
      int superPeerCount = config.getInt("peers.super");
      Object[] sPeerArgs = ArrayUtils.addAll(peerArgs, new Object[]{maxPeersForSuperPeer});
      for (int i = 0; i < superPeerCount; i++) {
        agentController = container.createNewAgent(
            SuperPeer.NAME+i, SuperPeer.class.getName(), sPeerArgs
        );
        agentController.start();
      }

      // give us some time to configure the sniffer....
      Thread.sleep(15);

      // instantiate ordinary peers
      int ordinaryPeerCount = config.getInt("peers.ordinary");
      Object[] oPeerArgs = ArrayUtils.addAll(peerArgs, new Object[]{}); // no extra args. need a peer factory!
      for (int i = 0; i < ordinaryPeerCount; i++) {
        agentController = container.createNewAgent(
            OrdinaryPeer.NAME+i, OrdinaryPeer.class.getName(), oPeerArgs
        );
        agentController.start();
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
