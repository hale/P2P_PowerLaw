package agents;

import behaviours.ReceiveConnectRequest;
import behaviours.ReceiveFileList;
import behaviours.ReceiveSearchRequest;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Super Peer has more bandwidth, and thus more responsibilities in the network. In
 * addition to the properties of all Peers, Super Peers maintain an extra list of ordinary
 * peer IDs and their list of shared files.
 */
public class SuperPeer extends Peer {

  public static final String NAME = "SP";

  // Once this limit is reached, this Super Peer is 'full' and denies further connection requests
  private static int MAX_CONNECTED_PEERS;

  // Peers that have connected to this Super Peer
  private final ArrayList<AID> connectionsToThisPeer = new ArrayList<AID>();

  // Maps the file names of indexed shared files with the Ordinary Peers that have it.
  private final HashMap<String, ArrayList<AID>> sharedFilesIndex = new HashMap<String, ArrayList<AID>>();

  @Override
  protected void setup() {
    super.setup();
    MAX_CONNECTED_PEERS = (Integer) args[6];
    addBehaviour(new ReceiveConnectRequest(this));
    addBehaviour(new ReceiveFileList(this));
    addBehaviour(new ReceiveSearchRequest(this));
  }

  /**
   * @return true if this peer has space free for Ordinary Peers, false otherwise.
   */
  public boolean canAcceptConnectRequest() {
    return connectionsToThisPeer.size() < MAX_CONNECTED_PEERS;
  }

  /**
   * Adds a Peer to the list of peers connected to this Super Peer.
   *
   * @param peer AID of the peer to add.
   */
  public void acceptNewConnection(AID peer) {
    connectionsToThisPeer.add(peer);
  }

  /**
   * Adds the given list of files to the shared files index. Note that the list of files
   * is a String, with each filename delimited by a semicolon.
   *
   * @param fileList semicolon (;) delimited list of file names.
   * @param sender Peer that shares these files.
   */
  public void addFiles(String fileList, AID sender) {
    for (String file : StringUtils.split(fileList, ';')) {
      ArrayList<AID> peersWithFile = sharedFilesIndex.get(file);
      if (peersWithFile == null) {
        peersWithFile = new ArrayList<AID>();
        peersWithFile.add(sender);
        sharedFilesIndex.put(file, peersWithFile);
      } else {
        peersWithFile.add(sender);
      }
    }
  }

  /**
   * Searches the local index for a file.
   *
   * @param file File name to search for.
   * @return true if the file is in this Super Peer's index, false otherwise.
   */
  public boolean canLocate(String file) {
    return sharedFilesIndex.containsKey(file);
  }

  /**
   * Gives the local name of a Peer who has this file, using the index. If there
   * is more than one Peer with this file, the first is returned.
   *
   * @param wantedFile A file name present in the index.
   * @return local name of a Peer with this file.
   */
  public String peerWith(String wantedFile) {
    return sharedFilesIndex.get(wantedFile).get(0).getLocalName();
  }

}
