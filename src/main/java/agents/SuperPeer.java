package agents;

import behaviours.ReceiveConnectRequest;
import behaviours.ReceiveFileList;
import behaviours.ReceiveSearchRequest;
import jade.core.AID;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
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
    MAX_CONNECTED_ORDINARY_PEERS = (Integer) args[3]; // 1 because 0,1,2 used in Peer. Sorry.
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

  public AID peerWith(String wantedFile) {
    return sharedFilesIndex.get(wantedFile).get(0);
  }

  public AID closestConnectedPeer(AID fileID) {
    // ??
  }
}
