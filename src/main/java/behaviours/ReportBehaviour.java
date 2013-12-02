package behaviours;

import agents.Runner;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ReportBehaviour extends TickerBehaviour {

  Runner runner;
  private Logger logger = Logger.getJADELogger(this.getClass().getName());
  StringBuilder sb = new StringBuilder();

  public ReportBehaviour(Agent a, long period) {
    super(a, period);
    runner = (Runner) a;
    sb.append("\n");
    sb.append("== BEGINNING SIMULATION ==");
    sb.append("\n");
    sb.append("Total Peers:\t" + runner.totalPeersSize());
    sb.append("\n");
    sb.append("Super Peers:\t" + runner.totalSuperPeersSize());
    sb.append("\n");
    logger.log(Level.INFO, sb.toString());
  }

  @Override
  protected void onTick() {
    sb = new StringBuilder();
    sb.append("== STATS ==");
    sb.append("\n");
    sb.append("Total messages sent:\t" + runner.totalMessagesSent());
    sb.append("\n");
    sb.append("Total connected peers:\t" + runner.totalConnectedPeers());
    sb.append("\n");
    sb.append("Total finished peers:\t" + runner.totalFinishedPeers());
    sb.append("\n");
    sb.append("\n");
    logger.log(Level.INFO, sb.toString());

    if (runner.simulationComplete()) {
      logEndSummary();
      stop();
    }
  }

  private void logEndSummary() {
    sb = new StringBuilder();
    sb.append("== SIMULATION FINISHED ==");
    sb.append("\n");
    sb.append("PEER\tMESSAGES SENT");
    sb.append("\n");
    ImmutableMultiset<AID> msgCounts = runner.getOrderedMsgCounts();
    for (Multiset.Entry<AID> count : msgCounts.entrySet()) {
      String name = count.getElement().getLocalName();
      sb.append(name);
      sb.append("\t");
      sb.append(count.getCount());
      sb.append("\n");
    }
    logger.log(Level.INFO, sb.toString());
  }


}
