package org.jfree.chart.urls.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.DefaultXYDataset;
/** 
 * Tests for the             {@link TimeSeriesURLGenerator} class.
 */
public class TimeSeriesURLGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(TimeSeriesURLGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public TimeSeriesURLGeneratorTests(  String name){
    super(name);
  }
  /** 
 * A basic check for the generateURL() method.
 */
  public void testGenerateURL(){
    TimeSeriesURLGenerator g=new TimeSeriesURLGenerator();
    DefaultXYDataset dataset=new DefaultXYDataset();
    dataset.addSeries("Series '1'",new double[][]{{1.0,2.0},{3.0,4.0}});
    String s=g.generateURL(dataset,0,0);
    assertTrue(s.startsWith("index.html?series=Series+%271%27&amp;item="));
  }
  /** 
 * Check that the equals() method can distinguish all fields.
 */
  public void testEquals(){
    TimeSeriesURLGenerator g1=new TimeSeriesURLGenerator();
    TimeSeriesURLGenerator g2=new TimeSeriesURLGenerator();
    assertTrue(g1.equals(g2));
    g1=new TimeSeriesURLGenerator(new SimpleDateFormat("yyyy"),"prefix","series","item");
    assertFalse(g1.equals(g2));
    g2=new TimeSeriesURLGenerator(new SimpleDateFormat("yyyy"),"prefix","series","item");
    assertTrue(g1.equals(g2));
    g1=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix","series","item");
    assertFalse(g1.equals(g2));
    g2=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix","series","item");
    assertTrue(g1.equals(g2));
    g1=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix1","series","item");
    assertFalse(g1.equals(g2));
    g2=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix1","series","item");
    assertTrue(g1.equals(g2));
    g1=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix1","series1","item");
    assertFalse(g1.equals(g2));
    g2=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix1","series1","item");
    assertTrue(g1.equals(g2));
    g1=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix1","series1","item1");
    assertFalse(g1.equals(g2));
    g2=new TimeSeriesURLGenerator(new SimpleDateFormat("yy"),"prefix1","series1","item1");
    assertTrue(g1.equals(g2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    TimeSeriesURLGenerator g1=new TimeSeriesURLGenerator();
    TimeSeriesURLGenerator g2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(g1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      g2=(TimeSeriesURLGenerator)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(g1,g2);
  }
  /** 
 * Checks that the class does not implement PublicCloneable (the generator is immutable).
 */
  public void testPublicCloneable(){
    TimeSeriesURLGenerator g1=new TimeSeriesURLGenerator();
    assertFalse(g1 instanceof PublicCloneable);
  }
}
