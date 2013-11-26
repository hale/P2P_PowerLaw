import com.sun.jdi.NativeMethodException;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * All peers can do the following:
 *
 *   Join the network by registering with the Host Cache.
 *   Give the Super Peer its list of shared files.
 *   Request the Super Peer for a shared file.
 *   Simulate transferal of a file from/to another Peer.

 */
public class Peer extends Agent {

  public static final String NAME = "PEER";
  private ArrayList<Peer> peers = new ArrayList<Peer>();

  protected void setup() {
    addBehaviour(new RegisterBehaviour(this));
  }

  public ArrayList<String> getFileList() {
    return new ArrayList<String>(Arrays.asList("First", "Second"));
  }

  public Peer getNextPeer() {
    return peers.get(0);
  }



}
