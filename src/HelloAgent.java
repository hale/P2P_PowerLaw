import jade.core.Agent;

/**
 * Demonstrates how to create and launch agents using JADE.
 *
 * java jade.Boot fred:HelloAgent
 * => "Hello, World!"
 * => "My name is fred"
 */
public class HelloAgent extends Agent {
  protected void setup() {
    System.out.println("Hello, World!");
    System.out.println("My name is " + getLocalName());
  }

}
