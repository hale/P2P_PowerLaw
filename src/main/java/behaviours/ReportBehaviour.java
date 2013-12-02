package behaviours;

import agents.Runner;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.util.Logger;

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

//    if (runner.allHaveFoundFiles()) {
//      logEndSummary();
//    }
  }

  private void logEndSummary() {
    sb = new StringBuilder();
    sb.append("== STATS ==");
//    HashMap<AID, NetworkStats>runner.getAllStats();


    sb.append("\n");
    logger.log(Level.INFO, sb.toString());
  }


}
