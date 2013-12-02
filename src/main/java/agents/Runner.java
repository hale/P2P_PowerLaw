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
 * Runs the simulation, and keeps track of information about the network.
 *
 * Run the simulation by launching JADE with this agent as a command line paramater.  E.g.:
 *
 *     java jade.Boot -gui runner:agents.Runner
 *
 * Will fail unless a file called P2PPowerLaw.properties is found in the top level directory
 * of the project. This specifies required options for the Runner and the Agents in the system.
 */
public class Runner extends Agent {
  private final Codec codec = new SLCodec();
  private final Ontology ontology = P2POntology.getInstance();

  public static final String NAME = "RUNNER";

  // All peers in the network
  private final ArrayList<AID> peers = new ArrayList<AID>();

  // Group peers by connected, finished.
  private static final String CONNECTED_PEERS = "Connected Peers";
  private static final String HAS_FOUND_FILES = "Peers that have found all their files.";
  private final HashMultimap<String, AID> peerGroups = HashMultimap.create();

  // A cumulative count of the number of messages each peer has sent. This is an
  // estimate, the precision of which can be controlled in the config gile.
  private final HashMultiset<AID> messageCounts = HashMultiset.create();

  // The simulation will stop when this limit is reached.
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

      // super peer arguments
      Object[] sPeerArgs = new Object[] {
          config.getInt("agent.send_stats_every"),
          config.getInt("super_peer.min_connections"),
          config.getInt("super_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files"),
          config.getInt("peer.search_interval_ms"),
          config.getInt("super_peer.max_peers")
      };

      // ordinary peer arguments
      Object[] oPeerArgs = new Object[] {
          config.getInt("agent.send_stats_every"),
          config.getInt("ordinary_peer.min_connections"),
          config.getInt("ordinary_peer.max_connections"),
          config.getStringArray("peer.shared_files"),
          config.getStringArray("peer.wanted_files"),
          config.getInt("peer.search_interval_ms")

      };
      Random rand = new Random();

      // Create a one-off Ordinary peer as defined in the properties file.
      if (config.getBoolean("custom_peer")) {
        Object[] overriddenArgs = new Object[]{
            config.getInt("agent.send_stats_every"),
            config.getInt("ordinary_peer.min_connections"),
            config.getInt("ordinary_peer.max_connections"),
            config.getStringArray("custom_peer.shared_files"),
            config.getStringArray("peer.wanted_files"),
            config.getInt("peer.search_interval_ms")
        };
        newPeer(OrdinaryPeer.NAME + "X", OrdinaryPeer.class.getName(), overriddenArgs);
      }

      // Create a number of Ordinary and Super peers, depending on their bandwidth
      int i = config.getBoolean("custom_peer") ? 1 : 0; // one less peer if custom_peer
      for ( ; i < config.getInt("total_peers"); i++) {
        int peerBandwidth = rand.nextInt(100);
        if (peerBandwidth >= (100 - config.getInt("super_peer_percentage"))) {
          newPeer(SuperPeer.NAME+i, SuperPeer.class.getName(), sPeerArgs);
        } else {
          newPeer(OrdinaryPeer.NAME+i, OrdinaryPeer.class.getName(), oPeerArgs);
        }
      }

      // Report on the network state based on info sent from the peers.
      addBehaviour(new ReportBehaviour(this, config.getLong("runner.report_interval_ms")));
      addBehaviour(new ReceiveNetworkStats(this));

    } catch (StaleProxyException e) {
      e.printStackTrace();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Spawn a new Peer and start the thread.
   *
   * @param name Unique name for the agent. Used to generate AID.
   * @param klass Type of Peer to create
   * @param args Any arguments to pass along to the Peer
   * @throws StaleProxyException
   */
  private void newPeer(String name, String klass, Object[] args) throws StaleProxyException {
    ContainerController container = getContainerController();
    AgentController agentController = container.createNewAgent(name, klass, args);
    agentController.start();
    peers.add(new AID(name, AID.ISLOCALNAME));
  }

  /**
   * Stats messages are sent from Peers - add this information to the peer stats and message counts.
   * @param sender Peer that's sending the stats
   * @param isConnected If the Peer has at least (min_connections) connected super peers
   * @param hasFoundFiles If the Peer has 'finished', i.e. transferred all their wanted files.
   * @param cumMsgCount Total number of messages sent by the Peer
   */
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

  /**
   * @return size of the network, including the Host Cache
   */
  public int totalPeersSize() {
    return peers.size();
  }

  /**
   * @return total number of Super Peers in the network.
   */
  public int totalSuperPeersSize() {
    ArrayList<AID> superPeers = new ArrayList<AID>();
    for (AID peer : peers) {
      if (peer.getLocalName().charAt(0) == 'S') {
        superPeers.add(peer);
      }
    }
    return superPeers.size();
  }

  /**
   * @return Unique messages sent by all peers, including the Host Cache
   */
  public long totalMessagesSent() {
    return messageCounts.size();
  }

  /**
   * @return The subset of all peers which have at least (min_connections) connected Super Peers
   */
  public int totalConnectedPeers() {
    return peerGroups.get(CONNECTED_PEERS).size();
  }


  /**
   * @return The subset of peers who have no wanted files left.
   */
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

  /**
   * @return A sorted copy of the messageCounts map.
   */
  public ImmutableMultiset<AID> getOrderedMsgCounts() {
    return Multisets.copyHighestCountFirst(messageCounts);
  }
}
