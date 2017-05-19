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
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link GroupedStackedBarRenderer} class.
 */
public class GroupedStackedBarRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(GroupedStackedBarRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public GroupedStackedBarRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    GroupedStackedBarRenderer r1=new GroupedStackedBarRenderer();
    GroupedStackedBarRenderer r2=new GroupedStackedBarRenderer();
    assertTrue(r1.equals(r2));
    assertTrue(r2.equals(r1));
    KeyToGroupMap m1=new KeyToGroupMap("G1");
    m1.mapKeyToGroup("S1","G2");
    r1.setSeriesToGroupMap(m1);
    assertFalse(r1.equals(r2));
    KeyToGroupMap m2=new KeyToGroupMap("G1");
    m2.mapKeyToGroup("S1","G2");
    r2.setSeriesToGroupMap(m2);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    GroupedStackedBarRenderer r1=new GroupedStackedBarRenderer();
    GroupedStackedBarRenderer r2=null;
    try {
      r2=(GroupedStackedBarRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    GroupedStackedBarRenderer r1=new GroupedStackedBarRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    GroupedStackedBarRenderer r1=new GroupedStackedBarRenderer();
    GroupedStackedBarRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(GroupedStackedBarRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (particularly by code in the renderer).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      DefaultCategoryDataset dataset=new DefaultCategoryDataset();
      dataset.addValue(1.0,"S1","C1");
      dataset.addValue(2.0,"S1","C2");
      dataset.addValue(3.0,"S2","C1");
      dataset.addValue(4.0,"S2","C2");
      GroupedStackedBarRenderer renderer=new GroupedStackedBarRenderer();
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),renderer);
      JFreeChart chart=new JFreeChart(plot);
      chart.createBufferedImage(300,200,null);
      success=true;
    }
 catch (    NullPointerException e) {
      e.printStackTrace();
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Some checks for the findRangeBounds() method.
 */
  public void testFindRangeBounds(){
    GroupedStackedBarRenderer r=new GroupedStackedBarRenderer();
    assertNull(r.findRangeBounds(null));
    DefaultCategoryDataset dataset=new DefaultCategoryDataset();
    assertNull(r.findRangeBounds(dataset));
    dataset.addValue(1.0,"R1","C1");
    assertEquals(new Range(0.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(-2.0,"R1","C2");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(null,"R1","C3");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
    KeyToGroupMap m=new KeyToGroupMap("G1");
    m.mapKeyToGroup("R1","G1");
    m.mapKeyToGroup("R2","G1");
    m.mapKeyToGroup("R3","G2");
    r.setSeriesToGroupMap(m);
    dataset.addValue(0.5,"R3","C1");
    assertEquals(new Range(-2.0,1.0),r.findRangeBounds(dataset));
    dataset.addValue(5.0,"R3","C2");
    assertEquals(new Range(-2.0,5.0),r.findRangeBounds(dataset));
  }
}
