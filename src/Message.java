import java.io.Serializable;

/**
 * Wraps up the name of a message and its contents when communicating between agents.
 */
public class Message implements Serializable {

  private MessageType type;
  private Object payload;

  public Message(MessageType type) {
    this.type = type;
  }

  public Message(MessageType type, Object payload) {
    this.type = type;
    this.payload = payload;
  }

  public MessageType getType() {
    return type;
  }

  public Object getPayload() {
    return payload;
  }
}

