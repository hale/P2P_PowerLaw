package ontology;

import ontology.actions.NeighboursResponse;
import ontology.actions.RequestConnect;
import ontology.actions.RequestNeighbours;
import ontology.P2PVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.PrimitiveSchema;

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
      add(as = new AgentActionSchema(REQUEST_NEIGHBOURS), RequestNeighbours.class);
      as.add(REQUEST_NEIGHBOURS_IS_SUPER, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

      add(as = new AgentActionSchema(NEIGHBOURS_RESPONSE), NeighboursResponse.class);
      as.add(NEIGHBOURS_RESPONSE_PEER_LIST, (PrimitiveSchema) getSchema(BasicOntology.STRING));

      add(as = new AgentActionSchema(REQUEST_CONNECT), RequestConnect.class);
    } catch (OntologyException e) {
      e.printStackTrace();
    }
  }
}
