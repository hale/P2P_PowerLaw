package agents;

import behaviours.ReceiveNetworkStats;
import behaviours.ReportBehaviour;
import com.google.common.collect.*;
import jade.content.ContentException;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
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
  private HashMultiset<AID> messageCounts = HashMultiset.create();

  private static final String CONNECTED_PEERS = "Connected Peers";
  private static final String HAS_FOUND_FILES = "Peers that have found all their files.";
  private int totalMessageLimit;

  @Override
  protected void setup() {
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontology);
    try {
      Configuration config = new PropertiesConfiguration("P2PPowerLaw.properties");
      totalMessageLimit = config.getInt("runner.total_message_limit", Integer.MAX_VALUE);

      // instantiate host cache
      Object[] hcArgs = new Object[]{
          config.getInt("agent.send_stats_every"),
          config.getInt("host_cache.max_neighbours")
      };
      newPeer(HostCache.NAME, HostCache.class.getName(), hcArgs);

      // super peers
      Object[] sPeerArgs = new Object[] {
          config.getInt("agent.send_stats_every"),
          config.getInt("super_peer.min_connections"),
          config.getInt("super_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files"),
          config.getInt("peer.search_interval_ms"),
          config.getInt("super_peer.max_peers")
      };

      // ordinary peers
      Object[] oPeerArgs = new Object[] {
          config.getInt("agent.send_stats_every"),
          config.getInt("ordinary_peer.min_connections"),
          config.getInt("ordinary_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files"),
          config.getInt("peer.search_interval_ms")

      };
      Random rand = new Random();

      // support for overriding an individual peer.
      if (config.getBoolean("peer.override")) {
        Object[] overriddenArgs = new Object[]{
            config.getInt("agent.send_stats_every"),
            config.getInt("ordinary_peer.min_connections"),
            config.getInt("ordinary_peer.max_connections"),
            config.getStringArray("peer.override.shared_files"),
            config.getStringArray("peer.wanted_files"),
            config.getInt("peer.search_interval_ms")
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
    messageCounts.add(sender, cumMsgCount);
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
    return messageCounts.size();
  }

  public int totalConnectedPeers() {
    return peerGroups.get(CONNECTED_PEERS).size();
  }


  public int totalFinishedPeers() {
    return peerGroups.get(HAS_FOUND_FILES).size();
  }

  /**
   * Determines when to 'stop'.
   *
   * @return True if either everyone has acquired their files, or message limit reached.
   */
  public boolean simulationComplete() {
    return ((totalConnectedPeers() == totalFinishedPeers()) || (totalMessagesSent() > totalMessageLimit));
  }

  public ArrayList<AID> getPeers() {
    return peers;
  }

  public ImmutableMultiset<AID> getOrderedMsgCounts() {
    return Multisets.copyHighestCountFirst(messageCounts);
  }
}
