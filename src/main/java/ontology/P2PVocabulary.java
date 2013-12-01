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

  public static final String REQUEST_CONNECT = "RequestConnect";

  public static final String CONNECT_RESPONSE = "ConnectResponse";
  public static final String CONNECT_RESPONSE_IS_SUCCESS = "isSuccess";

  public static final String FILE_LIST = "FileList";
  public static final String FILE_LIST_SHARED_FILES = "sharedFiles";

  public static final String REQUEST_SEARCH = "RequestSearch";
  public static final String REQUEST_SEARCH_FILE = "file";
  public static final String REQUEST_SEARCH_SENDER_STACK = "senderStack";

  public static final String SEARCH_RESPONSE = "SearchResponse";
  public static final String SEARCH_RESPONSE_FILE = "file";
  public static final String SEARCH_RESPONSE_PEER = "peer";
  public static final String SEARCH_RESPONSE_SENDER_STACK = "senderStack";

  public static final String FILE_REQUEST = "FileRequest";
  public static final String FILE_REQUEST_FILE = "file";

}
