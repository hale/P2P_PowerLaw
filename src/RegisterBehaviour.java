import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

/**
 * Peers implementing this behaviour are able to join the network by registering with the host cache.
 * Once the Peer has asked to join the network, it waits until it receives a list of Super Peers to connect to.
 */
public class RegisterBehaviour extends PeerBehaviour{

  private boolean finished = false;

  public RegisterBehaviour(Peer p) {
    super(p);
  }

  public void action() {
    contactHostCache();
    myPeer.doWait();
    finished = true;
  }

  public boolean done() { return finished; }

  private void contactHostCache() {
    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST );

    try {
      msg.setContentObject(new Message(MessageType.REGISTER));
    } catch (IOException e) {
      e.printStackTrace();
    }
    msg.addReceiver(new AID( HostCache.NAME, AID.ISLOCALNAME));
    myPeer.send(msg);
  }

}
