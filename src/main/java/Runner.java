import agents.HostCache;
import agents.OrdinaryPeer;
import agents.SuperPeer;
import jade.core.Agent;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

/**
 * Runs the P2P simulation. Required a config file.
 */
public class Runner extends Agent {

  Logger logger = Logger.getJADELogger(this.getClass().getName());

  protected void setup() {
    try {
      ContainerController container = getContainerController();
      Configuration config = new PropertiesConfiguration("P2PPowerLaw.properties");
      AgentController agentController;

      // instantiate host cache
      Object[] hcArgs = new Object[]{config.getInt("host_cache.max_neighbours")};
      agentController = container.createNewAgent(
          HostCache.NAME, HostCache.class.getName(), hcArgs
      );
      agentController.start();


      // super peers
      Object[] sPeerArgs = new Object[] {
          config.getInt("super_peer.min_connections"),
          config.getInt("super_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files"),
          config.getInt("super_peer.max_peers")
      };

      // ordinary peers
      Object[] oPeerArgs = new Object[] {
          config.getInt("ordinary_peer.min_connections"),
          config.getInt("ordinary_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files")

      };
      Random rand = new Random();

      // support for overriding an individual peer.
      if (config.getBoolean("peer.override")) {
        Object[] overriddenArgs = new Object[]{
            config.getInt("ordinary_peer.min_connections"),
            config.getInt("ordinary_peer.max_connections"),
            config.getStringArray("peer.override.shared_files"),
            config.getStringArray("peer.wanted_files")
        };
        agentController = container.createNewAgent(
            OrdinaryPeer.NAME + "X", OrdinaryPeer.class.getName(), overriddenArgs
        );
        agentController.start();
      }

      int i = config.getBoolean("peer.override") ? 0 : 1; // stick to the total_peers arg.
      for ( ; i < config.getInt("total_peers"); i++) {
        int peerBandwidth = rand.nextInt(100);
        if (peerBandwidth >= (100 - config.getInt("super_peer_percentage"))) {
          agentController = container.createNewAgent(
              SuperPeer.NAME + i, SuperPeer.class.getName(), sPeerArgs
          );
        } else {
          agentController = container.createNewAgent(
              OrdinaryPeer.NAME + i, OrdinaryPeer.class.getName(), oPeerArgs
          );
        }
        agentController.start();
      }

    } catch (StaleProxyException e) {
      e.printStackTrace();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
  }
}
