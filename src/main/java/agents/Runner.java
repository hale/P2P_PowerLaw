package agents;

import behaviours.ReceiveNetworkStats;
import behaviours.ReportBehaviour;
import com.google.common.collect.HashMultimap;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import ontology.P2POntology;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Runs the P2P simulation. Required a config file.
 */
public class Runner extends Agent {
  private Codec codec = new SLCodec();
  private Ontology ontology = P2POntology.getInstance();

  public static final String NAME = "RUNNER";

  ArrayList<AID> peers = new ArrayList<AID>();
  private HashMultimap<String, AID> peerGroups = HashMultimap.create();
  private HashMap<AID, Long> messageCounts = new HashMap<AID, Long>();

  private static final String CONNECTED_PEERS = "Connected Peers";
  private static final String HAS_FOUND_FILES = "Peers that have found all their files.";

  @Override
  protected void setup() {
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontology);
    try {
      Configuration config = new PropertiesConfiguration("P2PPowerLaw.properties");

      // instantiate host cache
      Object[] hcArgs = new Object[]{
          config.getInt("agent.send_stats_every"),
          config.getInt("host_cache.max_neighbours")
      };
      newPeer(HostCache.NAME, HostCache.class.getName(), hcArgs);

      Thread.sleep(20);

      // super peers
      Object[] sPeerArgs = new Object[] {
          config.getInt("agent.send_stats_every"),
          config.getInt("super_peer.min_connections"),
          config.getInt("super_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files"),
          config.getInt("super_peer.max_peers")
      };

      // ordinary peers
      Object[] oPeerArgs = new Object[] {
          config.getInt("agent.send_stats_every"),
          config.getInt("ordinary_peer.min_connections"),
          config.getInt("ordinary_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files")

      };
      Random rand = new Random();

      // support for overriding an individual peer.
      if (config.getBoolean("peer.override")) {
        Object[] overriddenArgs = new Object[]{
            config.getInt("agent.send_stats_every"),
            config.getInt("ordinary_peer.min_connections"),
            config.getInt("ordinary_peer.max_connections"),
            config.getStringArray("peer.override.shared_files"),
            config.getStringArray("peer.wanted_files")
        };
        newPeer(OrdinaryPeer.NAME + "X", OrdinaryPeer.class.getName(), overriddenArgs);
      }

      int i = config.getBoolean("peer.override") ? 1 : 0; // stick to the total_peers arg.
      for ( ; i < config.getInt("total_peers"); i++) {
        int peerBandwidth = rand.nextInt(100);
        if (peerBandwidth >= (100 - config.getInt("super_peer_percentage"))) {
          newPeer(SuperPeer.NAME+i, SuperPeer.class.getName(), sPeerArgs);
        } else {
          newPeer(OrdinaryPeer.NAME+i, OrdinaryPeer.class.getName(), oPeerArgs);
        }
      }

      addBehaviour(new ReportBehaviour(this, config.getLong("runner.report_interval_ms")));
      addBehaviour(new ReceiveNetworkStats(this));

    } catch (StaleProxyException e) {
      e.printStackTrace();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  private void newPeer(String name, String klass, Object[] args) throws StaleProxyException {
    ContainerController container = getContainerController();
    AgentController agentController = container.createNewAgent(name, klass, args);
    agentController.start();
    peers.add(new AID(name, AID.ISLOCALNAME));
  }

  public void updateStats(AID sender, boolean isConnected, boolean hasFoundFiles, int cumMsgCount) {
    if (!(sender == new AID(HostCache.NAME, AID.ISLOCALNAME))) {
      if (isConnected) {
        peerGroups.put(CONNECTED_PEERS, sender);
      }
      if (hasFoundFiles) {
        peerGroups.put(HAS_FOUND_FILES, sender);
      }
    }
    messageCounts.put(sender, Long.valueOf(cumMsgCount));
  }

  public int totalPeersSize() {
    return peers.size();
  }

  public int totalSuperPeersSize() {
    ArrayList<AID> superPeers = new ArrayList<AID>();
    for (AID peer : peers) {
      if (peer.getLocalName().charAt(0) == 'S') {
        superPeers.add(peer);
      }
    }
    return superPeers.size();

  }

  public long totalMessagesSent() {
    long total = 0;
    for (long count : messageCounts.values()) {
      total += count;
    }
    return total;
  }

  public int totalConnectedPeers() {
    return peerGroups.get(CONNECTED_PEERS).size();
  }


  public int totalFinishedPeers() {
    return peerGroups.get(HAS_FOUND_FILES).size();
  }

}
