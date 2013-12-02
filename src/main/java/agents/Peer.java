package agents;

import behaviours.*;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Behaviour of all peers, both Ordinary and Super
 */
public abstract class Peer extends BasicAgent {

  // The peer will keep trying to connect to Super Peers until this limit is reached
  private static int MIN_CONNECTED_PEERS;

  // The Peer will stop sending Connect Requests when this number is reached
  private static int MAX_CONNECTED_PEERS;

  // Super Peers from the Host Cache which have not yet been contacted
  private final ArrayList<AID> knownPeers = new ArrayList<AID>();

  // Super Peers which the peer is connected to
  private final ArrayList<AID> connectedPeers = new ArrayList<AID>();

  // Super Peers which have been sent a Connect Request, but not yet responded
  private final ArrayList<AID> connectPendingPeers = new ArrayList<AID>();

  // Files that can be sent from this Peer
  private ArrayList<String> sharedFiles;

  // Files this Peer will search for
  private ArrayList<String> wantedFiles = new ArrayList<String>();

  // Records the state of HostCache requests, to prevent spamming
  private boolean hasRequestedPeers;

  @Override
  protected void setup() {
    super.setup();
    MIN_CONNECTED_PEERS = (Integer) args[1];
    MAX_CONNECTED_PEERS = (Integer) args[2];
    sharedFiles = new ArrayList<String>(Arrays.asList((String[]) args[3]));
    wantedFiles = new ArrayList<String>(Arrays.asList((String[]) args[4]));
    addBehaviour(new SendNeighboursRequest(this));
    addBehaviour(new ReceiveNeighborsResponse(this));
    addBehaviour(new SendConnectRequest(this));
    addBehaviour(new ReceiveConnectResponse(this));
    addBehaviour(new SendSearchRequest(this, (Integer) args[5]));
    addBehaviour(new ReceiveSearchResponse(this));
    addBehaviour(new ReceiveFileRequest(this));
    addBehaviour(new ReceiveFileResponse(this));
  }

  /**
   * Adds a semicolon delimited list of local Super Peer names to the knownPeers list.
   * Only adds peers which are not already known to this peer.
   *
   * @param peers List of peers
   */
  public void addKnownPeers(String peers) {
    String[] peerList = StringUtils.split(peers, ';');
    for (String peer : peerList) {
      AID knownPeer = new AID(peer, AID.ISLOCALNAME);
      // skip if already connected or contacted
      if (connectedPeers.contains(knownPeer) || connectPendingPeers.contains(knownPeer)) { continue; }
      knownPeers.add(knownPeer);
    }
  }

  /**
   * Whether or not the Peer has requested peers from the Host Cache/
   */
  public boolean hasRequestedPeers() {
    return hasRequestedPeers;
  }

  /**
   * Set to true when sending a Request Neighbours, and false when we get a response.
   */
  public void setRequestedPeers(boolean requestedPeers) {
    this.hasRequestedPeers = requestedPeers;
  }

  /**
   * @return True if knownPeers contains an element, false otherwise.
   */
  public boolean hasKnownPeers() {
    return !knownPeers.isEmpty();
  }

  /**
   * @return true if we still need a connection, and there are no Peers left to ask
   */
  public boolean needsKnownPeers() {
    return needsConnectedPeers() && knownPeers.isEmpty();
  }

  /**
   * Setting a MAX prevents the peer from spamming connect requests
   *
   * @return true if the peer's number of connected peers is below the minimum.
   */
  public boolean needsConnectedPeers() {
    return (connectedPeers.size() + connectPendingPeers.size()) < MIN_CONNECTED_PEERS &&
        (connectedPeers.size() + connectPendingPeers.size()) <= MAX_CONNECTED_PEERS;
  }

  /**
   * @return The next known peer in the list.
   */
  public AID getKnownPeer() {
    return knownPeers.get(0);
  }

  /**
   * When a Super Peer is contacted, move them to a pending state
   */
  public void addConnectPendingPeer(AID peer) {
    connectPendingPeers.add(peer);
    knownPeers.remove(peer);
  }

  /**
   * Upon hearing back from a contacted Super Peer, they are removed from the pending peer list.
   */
  public void removePendingPeer(AID peer) {
    connectPendingPeers.remove(peer);
  }

  /**
   * If the request is successful, they are added to the connected list.
   */
  public void addConnectedPeer(AID peer) {
    connectedPeers.add(peer);
  }

  /**
   * @return True if the peer has at least one Connected peer, false otherwise
   */
  public boolean isConnected() {
    return !connectedPeers.isEmpty();
  }

  /**
   * @return Any file in the wantedFiles list
   */
  public String getWantedFile() {
    return wantedFiles.get(0);
  }

  /**
   * @return Any peer in the connectedPeers list
   */
  public AID getConnectedPeer() {
    return connectedPeers.get(0);
  }

  /**
   * @return true if the peer has at least wanted file, false otherwise
   */
  public boolean hasWantedFile() {
    return !wantedFiles.isEmpty();
  }

  /**
   * Simulate file transfer - for these purposes, the name of the file and the file itself are the same thing.
   */
  public String getFile(String fileName) {
    return sharedFiles.get(sharedFiles.indexOf(fileName));
  }

  /**
   * Simulated file transfer - in this case changing the file from state 'wanted' to 'shared'
   */
  public void receiveFile(String file) {
    wantedFiles.remove(file);
    sharedFiles.add(file);
  }

  /**
   * 'Closest' as defined by the document routing procedure.  The universal set of
   *  AIDs have total ordering. This allows us to implement Document Routing, where
   *  the chosen destination for a forwarded message is to the peer with an ID that's
   *  adjacent to the AID of the file.
   *
   *  Strictly speaking the file AID should be generated when the file is published - since
   *  we informally require files to have unique local names, we can safely generate the
   *  AID dynamically.
   *
   *  @param file File to match against the list of peers; the target.
   *  @return ID of the peer with an AID closest to the file AID
   */
  public AID closestConnectedPeer(String file) {
    if (connectedPeers.size() == 1) { return connectedPeers.get(0); }

    int listLength = connectedPeers.size()+1;
    AID[] idList = new AID[listLength];

    idList[0] = new AID(file, AID.ISLOCALNAME);
    for (int i = 1; i < listLength; i++) {
      idList[i] = connectedPeers.get(i-1);
    }

    Arrays.sort(idList);

    int fileIndex = Arrays.binarySearch(idList, new AID(file, AID.ISLOCALNAME));
    if (fileIndex == 0) { // first element
      return idList[1];
    } else if(fileIndex == listLength -1) { // last element
      return idList[listLength-2];
    } else { // list must contain >2 elems
      return idList[fileIndex+1];
    }
  }

  /**
   * @return semicolon delimited list of shared filed for this peer
   */
  public String getSharedFiles() {
    return StringUtils.join(sharedFiles, ';');
  }
 }
