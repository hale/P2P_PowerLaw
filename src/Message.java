import java.io.Serializable;

/**
 * Wraps up the name of a message and its contents when communicating between agents.
 */
public class Message implements Serializable {

  private MessageType type;
  private Object payload;
  private boolean success;

  public Message(MessageType type, boolean success) {
    this.type = type;
    this.success = success;
  }

  public Message(MessageType type, Object payload) {
    this.type = type;
    this.payload = payload;
  }

  public Message(MessageType type, Object payload, boolean success) {
    this.type = type;
    this.payload = payload;
    this.success = success;
  }

  public MessageType getType() {
    return type;
  }

  public Object getPayload() {
    return payload;
  }

  public boolean isSuccess() {
    return success;
  }

  public boolean isFail() {
    return !success;
  }
}

