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

  private static int MIN_CONNECTED_PEERS;
  private static int MAX_CONNECTED_PEERS;

  private ArrayList<AID> knownPeers = new ArrayList<AID>();
  protected ArrayList<AID> connectedPeers = new ArrayList<AID>();
  private ArrayList<AID> connectPendingPeers = new ArrayList<AID>();

  protected ArrayList<String> sharedFiles;
  private ArrayList<String> wantedFiles = new ArrayList<String>();

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
    addBehaviour(new SendSearchRequest(this));
    addBehaviour(new ReceiveSearchResponse(this));
    addBehaviour(new ReceiveFileRequest(this));
    addBehaviour(new ReceiveFileResponse(this));
  }

  public void addKnownPeers(String peers) {
    String[] peerList = StringUtils.split(peers, ';');
    for (String peer : peerList) {
      AID knownPeer = new AID(peer, AID.ISLOCALNAME);
      // skip if already connected
      if (connectedPeers.contains(knownPeer) || connectPendingPeers.contains(knownPeer)) { continue; }
      knownPeers.add(knownPeer);
    }
  }

  public boolean hasRequestedPeers() {
    return hasRequestedPeers;
  }

  public void setRequestedPeers(boolean requestedPeers) {
    this.hasRequestedPeers = requestedPeers;
  }

  public boolean hasKnownPeers() {
    return !knownPeers.isEmpty();
  }

  public boolean needsKnownPeers() {
    return needsConnectedPeers() && knownPeers.isEmpty();
  }

  /**
   * Setting a MAX prevents the peer from spamming connect requests
   */
  public boolean needsConnectedPeers() {
    return (connectedPeers.size() + connectPendingPeers.size()) < MIN_CONNECTED_PEERS &&
        (connectedPeers.size() + connectPendingPeers.size()) <= MAX_CONNECTED_PEERS;
  }

  public AID getKnownPeer() {
    return knownPeers.get(0);
  }

  public void addConnectPendingPeer(AID peer) {
    connectPendingPeers.add(peer);
    knownPeers.remove(peer);
  }

  public void removePendingPeer(AID peer) {
    connectPendingPeers.remove(peer);
  }

  public void addConnectedPeer(AID peer) {
    connectedPeers.add(peer);
  }

  public boolean isConnected() {
    return !connectedPeers.isEmpty();
  }

  public String getWantedFile() {
    return wantedFiles.get(0);
  }

  public AID getConnectedPeer() {
    return connectedPeers.get(0);
  }

  public boolean hasWantedFile() {
    return !wantedFiles.isEmpty();
  }

  /**
   * Simulate file transfer - for these purposes, the name of the file and the file itself are the same thing.
   */
  public String getFile(String fileName) {
    return sharedFiles.get(sharedFiles.indexOf(fileName));
  }

  public void receiveFile(String file) {
    wantedFiles.remove(file);
    sharedFiles.add(file);
  }

  /**
   * Document routing
   */
  public AID closestConnectedPeer(String file) {
    if (connectedPeers.size() == 0) { }
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
 }
