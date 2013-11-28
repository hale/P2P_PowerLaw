package ontology;

import ontology.actions.RequestNeighboursAction;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.PrimitiveSchema;
import ontology.actions.SendNeighboursResponseAction;

/**
 * Defines the language used for Agent communication.
 */
public class P2POntology extends Ontology implements P2PVocabulary {

  public static final String ONTOLOGY_NAME = "P2P-Ontology";
  private static Ontology instance = new P2POntology();
  public static Ontology getInstance() { return instance; }

  private P2POntology() {
    super(ONTOLOGY_NAME, BasicOntology.getInstance());

    AgentActionSchema as;
    try {
      add(as = new AgentActionSchema(P2PVocabulary.REQUEST_NEIGHBOURS), RequestNeighboursAction.class);
      as.add(P2PVocabulary.PEER_CLASS, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

      add(as = new AgentActionSchema(P2PVocabulary.SEND_PEERS), SendNeighboursResponseAction.class);
      as.add(P2PVocabulary.PEER_LIST, (PrimitiveSchema) getSchema(BasicOntology.STRING));
    } catch (OntologyException e) {
      e.printStackTrace();
    }
  }
}
