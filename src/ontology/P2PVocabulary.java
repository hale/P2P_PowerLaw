package ontology;

/**
 * Part of the P2P Ontology
 *
 * Defines the constructs of messages between peers.
 *
 */
public interface P2PVocabulary {

  // ACTIONS
  public static final String REQUEST_NEIGHBOURS = "RequestNeighbours";
  public static final String REQUEST_NEIGHBOURS_IS_SUPER = "isSuper";

  public static final String NEIGHBOURS_RESPONSE = "NeighboursResponse";
  public static final String NEIGHBOURS_RESPONSE_PEER_LIST = "peerList";
}
