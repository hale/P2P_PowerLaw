package ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import ontology.actions.*;

/**
 * Defines the language used for Agent communication.
 */
public class P2POntology extends Ontology implements P2PVocabulary {

  private static final String ONTOLOGY_NAME = "P2P-Ontology";
  private static final Ontology instance = new P2POntology();
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

      add(as = new AgentActionSchema(CONNECT_RESPONSE), ConnectResponse.class);
      as.add(CONNECT_RESPONSE_IS_SUCCESS, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

      add(as = new AgentActionSchema(FILE_LIST), FileList.class);
      as.add(FILE_LIST_SHARED_FILES, (PrimitiveSchema) getSchema(BasicOntology.STRING));

      add(as = new AgentActionSchema(REQUEST_SEARCH), RequestSearch.class);
      as.add(REQUEST_SEARCH_FILE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
      as.add(REQUEST_SEARCH_SENDER_STACK, (PrimitiveSchema) getSchema(BasicOntology.STRING));

      add(as = new AgentActionSchema(SEARCH_RESPONSE), SearchResponse.class);
      as.add(SEARCH_RESPONSE_FILE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
      as.add(SEARCH_RESPONSE_PEER, (PrimitiveSchema) getSchema(BasicOntology.STRING));
      as.add(SEARCH_RESPONSE_SENDER_STACK, (PrimitiveSchema) getSchema(BasicOntology.STRING));

      add(as = new AgentActionSchema(FILE_REQUEST), FileRequest.class);
      as.add(FILE_REQUEST_FILE, (PrimitiveSchema) getSchema(BasicOntology.STRING));

      add(as = new AgentActionSchema(FILE_RESPONSE), FileResponse.class);
      as.add(FILE_RESPONSE_FILE, (PrimitiveSchema) getSchema(BasicOntology.STRING));

      add(as = new AgentActionSchema(NETWORK_STATS), NetworkStats.class);
      as.add(NETWORK_STATS_CUM_MSG_COUNT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
      as.add(NETWORK_STATS_IS_CONNECTED, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
      as.add(NETWORK_STATS_HAS_FOUND_FILES, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    } catch (OntologyException e) {
      e.printStackTrace();
    }
  }
}
