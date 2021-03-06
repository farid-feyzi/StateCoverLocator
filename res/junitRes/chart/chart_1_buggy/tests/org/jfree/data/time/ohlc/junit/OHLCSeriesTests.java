package org.jfree.data.time.ohlc.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.event.SeriesChangeEvent;
import org.jfree.data.event.SeriesChangeListener;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Year;
import org.jfree.data.time.ohlc.OHLCSeries;
/** 
 * Tests for the             {@link OHLCSeries} class.
 */
public class OHLCSeriesTests extends TestCase implements SeriesChangeListener {
  SeriesChangeEvent lastEvent;
  /** 
 * Records a change event.
 * @param event  the event.
 */
  public void seriesChanged(  SeriesChangeEvent event){
    this.lastEvent=event;
  }
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(OHLCSeriesTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public OHLCSeriesTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    OHLCSeries s1=new OHLCSeries("s1");
    OHLCSeries s2=new OHLCSeries("s1");
    assertTrue(s1.equals(s2));
    s1=new OHLCSeries("s2");
    assertFalse(s1.equals(s2));
    s2=new OHLCSeries("s2");
    assertTrue(s1.equals(s2));
    s1.add(new Year(2006),2.0,4.0,1.0,3.0);
    assertFalse(s1.equals(s2));
    s2.add(new Year(2006),2.0,4.0,1.0,3.0);
    assertTrue(s2.equals(s1));
    s1.add(new Year(2008),2.0,4.0,1.0,3.0);
    assertFalse(s1.equals(s2));
    s2.add(new Year(2008),2.0,4.0,1.0,3.0);
    assertTrue(s2.equals(s1));
    s1.remove(new Year(2008));
    assertFalse(s1.equals(s2));
    s2.remove(new Year(2008));
    assertTrue(s2.equals(s1));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    OHLCSeries s1=new OHLCSeries("s1");
    s1.add(new Year(2006),2.0,4.0,1.0,3.0);
    OHLCSeries s2=null;
    try {
      s2=(OHLCSeries)s1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(s1 != s2);
    assertTrue(s1.getClass() == s2.getClass());
    assertTrue(s1.equals(s2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    OHLCSeries s1=new OHLCSeries("s1");
    s1.add(new Year(2006),2.0,4.0,1.0,3.0);
    OHLCSeries s2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(s1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      s2=(OHLCSeries)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(s1,s2);
  }
  /** 
 * Simple test for the indexOf() method.
 */
  public void testIndexOf(){
    OHLCSeries s1=new OHLCSeries("s1");
    s1.add(new Year(2006),2.0,4.0,1.0,3.0);
    s1.add(new Year(2011),2.0,4.0,1.0,3.0);
    s1.add(new Year(2010),2.0,4.0,1.0,3.0);
    assertEquals(0,s1.indexOf(new Year(2006)));
    assertEquals(1,s1.indexOf(new Year(2010)));
    assertEquals(2,s1.indexOf(new Year(2011)));
  }
  /** 
 * Simple test for the remove() method.
 */
  public void testRemove(){
    OHLCSeries s1=new OHLCSeries("s1");
    s1.add(new Year(2006),2.0,4.0,1.0,3.0);
    s1.add(new Year(2011),2.1,4.1,1.1,3.1);
    s1.add(new Year(2010),2.2,4.2,1.2,3.2);
    assertEquals(3,s1.getItemCount());
    s1.remove(new Year(2010));
    assertEquals(new Year(2011),s1.getPeriod(1));
    s1.remove(new Year(2006));
    assertEquals(new Year(2011),s1.getPeriod(0));
  }
  /** 
 * If you add a duplicate period, an exception should be thrown.
 */
  public void testAdditionOfDuplicatePeriod(){
    OHLCSeries s1=new OHLCSeries("s1");
    s1.add(new Year(2006),1.0,1.0,1.0,1.0);
    boolean pass=false;
    try {
      s1.add(new Year(2006),1.0,1.0,1.0,1.0);
    }
 catch (    SeriesException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * A simple check that the maximumItemCount attribute is working.
 */
  public void testSetMaximumItemCount(){
    OHLCSeries s1=new OHLCSeries("s1");
    assertEquals(Integer.MAX_VALUE,s1.getMaximumItemCount());
    s1.setMaximumItemCount(2);
    assertEquals(2,s1.getMaximumItemCount());
    s1.add(new Year(2006),1.0,1.1,1.1,1.1);
    s1.add(new Year(2007),2.0,2.2,2.2,2.2);
    s1.add(new Year(2008),3.0,3.3,3.3,3.3);
    assertEquals(new Year(2007),s1.getPeriod(0));
    assertEquals(new Year(2008),s1.getPeriod(1));
  }
  /** 
 * Check that the maximum item count can be applied retrospectively.
 */
  public void testSetMaximumItemCount2(){
    OHLCSeries s1=new OHLCSeries("s1");
    s1.add(new Year(2006),1.0,1.1,1.1,1.1);
    s1.add(new Year(2007),2.0,2.2,2.2,2.2);
    s1.add(new Year(2008),3.0,3.3,3.3,3.3);
    s1.setMaximumItemCount(2);
    assertEquals(new Year(2007),s1.getPeriod(0));
    assertEquals(new Year(2008),s1.getPeriod(1));
  }
  /** 
 * Some checks for the clear() method.
 */
  public void testClear(){
    OHLCSeries s1=new OHLCSeries("S1");
    s1.addChangeListener(this);
    s1.clear();
    assertNull(this.lastEvent);
    assertTrue(s1.isEmpty());
    s1.add(new Year(2006),1.0,1.1,1.1,1.1);
    assertFalse(s1.isEmpty());
    s1.clear();
    assertNotNull(this.lastEvent);
    assertTrue(s1.isEmpty());
  }
}
