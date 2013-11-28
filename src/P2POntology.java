import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
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
      add(as = new AgentActionSchema(REQUEST_NEIGHBOURS), RequestNeighboursAction.class);
      as.add(PEER_CLASS, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
    } catch (OntologyException e) {
      e.printStackTrace();
    }
  }
}
