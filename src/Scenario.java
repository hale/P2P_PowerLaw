import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Creates a P2P simulation scenario from an XML specification file.
 */
public class Scenario {
  public static void main(String[] args) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbf.newDocumentBuilder();
      Document doc = docBuilder.parse(new File("scenarios.xml"));
      doc.getDocumentElement().normalize();

      Node scenarioNode = doc.getElementsByTagName("Scenario").item(0);
      String scenarioName = scenarioNode.getAttributes().getNamedItem("name").getNodeValue();
      String scenarioDescr = scenarioNode.getAttributes().getNamedItem("description").getNodeValue();

      System.out.print("Loading scenario " + scenarioName + ": ");
      System.out.println(scenarioDescr);





    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
