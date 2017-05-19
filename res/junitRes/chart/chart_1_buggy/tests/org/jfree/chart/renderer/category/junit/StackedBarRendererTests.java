package org.jfree.chart.renderer.category.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link StackedBarRenderer} class.
 */
public class StackedBarRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StackedBarRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StackedBarRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    StackedBarRenderer r1=new StackedBarRenderer();
    StackedBarRenderer r2=new StackedBarRenderer();
    assertTrue(r1.equals(r2));
    assertTrue(r2.equals(r1));
    r1.setRenderAsPercentages(true);
    assertFalse(r1.equals(r2));
    r2.setRenderAsPercentages(true);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashCode(){
    StackedBarRenderer r1=new StackedBarRenderer();
    StackedBarRenderer r2=new StackedBarRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StackedBarRenderer r1=new StackedBarRenderer();
    StackedBarRenderer r2=null;
    try {
      r2=(StackedBarRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      System.err.println("Failed to clone.");
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    StackedBarRenderer r1=new StackedBarRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StackedBarRenderer r1=new StackedBarRenderer();
    StackedBarRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(StackedBarRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(r1,r2);
  }
  /** 
 * Some checks for the findRangeBounds() method.
 */
  public void testFindRangeBounds(){
    StackedBarRenderer r=new StackedBarRenderer();
    assertNull(r.findRangeBounds(null));
    DefaultCategoryDataset dataset=new DefaultCategoryDataset();
    assertNull(r.findRangeBounds(dataset));
    dataset.addValue(1.0,"R1","C1");
    assertEquals(new Range(0.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(-2.0,"R1","C2");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(null,"R1","C3");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(2.0,"R2","C1");
    assertEquals(new Range(-2.0,3.0),r.findRangeBounds(dataset));
    dataset.addValue(null,"R2","C2");
    assertEquals(new Range(-2.0,3.0),r.findRangeBounds(dataset));
  }
}
