package agents;

import behaviours.ReceiveConnectRequest;
import behaviours.ReceiveFileList;
import behaviours.ReceiveSearchRequest;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 */
public class SuperPeer extends Peer {

  private static int MAX_CONNECTED_ORDINARY_PEERS;
  public static String NAME = "SUPER PEER";
  private ArrayList<AID> connectedOrdinaryPeers = new ArrayList<AID>();
  private HashMap<String, ArrayList<AID>> sharedFilesIndex = new HashMap<String, ArrayList<AID>>();

  @Override
  protected void setup() {
    super.setup();
    MAX_CONNECTED_ORDINARY_PEERS = (Integer) args[4]; // 1 because 0,1,2,3 used in Peer. Sorry.
    addBehaviour(new ReceiveConnectRequest(this));
    addBehaviour(new ReceiveFileList(this));
    addBehaviour(new ReceiveSearchRequest(this));
  }

  public boolean canAcceptConnectRequest() {
    return connectedOrdinaryPeers.size() < MAX_CONNECTED_ORDINARY_PEERS;
  }

  public void addConnectedOrdinaryPeer(AID peer) {
    connectedOrdinaryPeers.add(peer);
//    logger.log(Level.INFO, getLocalName() + " adds " + peer.getLocalName());

  }

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
    logger.log(Level.INFO, getLocalName()+" indexed "+StringUtils.split(fileList,';').length+" files for "+sender.getLocalName());
  }

  public boolean canLocate(String file) {
    return sharedFilesIndex.containsKey(file);
  }

  public String peerWith(String wantedFile) {
    return sharedFilesIndex.get(wantedFile).get(0).getLocalName();
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
