import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



/**
 * Created with IntelliJ IDEA.
 * User: philiphale
 * Date: 26/11/2013
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class HostCacheTest {

  HostCache hc;

  /**
   * @return A random String, to mock a peer's ID.
   */
  private String peerID() {
    return UUID.randomUUID().toString();
  }

  @Before
  public void initializeWithOnePeer() {
    hc = new HostCache();
    hc.contact(peerID());
  }

  @Test
  public void testContactProvidesAtLeastOnePeer() {
    int lowerLimit = 1;
    int actual = hc.contact(peerID()).size();
    assertThat(actual, greaterThan(lowerLimit));
  }

  @Test
  public void testContactProvidesTenPeersOrLess() {
    int upperLimit = 10;
    int actual = hc.contact(peerID()).size();
    assertThat(actual, lessThan(upperLimit));
  }

  @Test
  public void testContactIncrementsPeerCount() {
    int sizeBefore = hc.peerCount();
    hc.contact(peerID());
    int sizeAfter = hc.peerCount();
    assertThat(sizeAfter, is(sizeBefore + 1));
  }

  @Test
  public void testContactOnlyAddsUnknownPeers() {
    int size = hc.peerCount();
    hc.contact("peer");
    hc.contact("peer");
    assertThat(hc.peerCount(), is(size + 1));
  }

}
