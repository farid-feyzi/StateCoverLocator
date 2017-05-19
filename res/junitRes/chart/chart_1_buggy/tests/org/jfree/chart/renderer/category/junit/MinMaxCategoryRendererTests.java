package org.jfree.chart.renderer.category.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
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
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
/** 
 * Tests for the             {@link MinMaxCategoryRenderer} class.
 */
public class MinMaxCategoryRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(MinMaxCategoryRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public MinMaxCategoryRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    MinMaxCategoryRenderer r1=new MinMaxCategoryRenderer();
    MinMaxCategoryRenderer r2=new MinMaxCategoryRenderer();
    assertEquals(r1,r2);
    r1.setDrawLines(true);
    assertFalse(r1.equals(r2));
    r2.setDrawLines(true);
    assertTrue(r1.equals(r2));
    r1.setGroupPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertFalse(r1.equals(r2));
    r2.setGroupPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertTrue(r1.equals(r2));
    r1.setGroupStroke(new BasicStroke(1.2f));
    assertFalse(r1.equals(r2));
    r2.setGroupStroke(new BasicStroke(1.2f));
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    MinMaxCategoryRenderer r1=new MinMaxCategoryRenderer();
    MinMaxCategoryRenderer r2=new MinMaxCategoryRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    MinMaxCategoryRenderer r1=new MinMaxCategoryRenderer();
    MinMaxCategoryRenderer r2=null;
    try {
      r2=(MinMaxCategoryRenderer)r1.clone();
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
    MinMaxCategoryRenderer r1=new MinMaxCategoryRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    MinMaxCategoryRenderer r1=new MinMaxCategoryRenderer();
    MinMaxCategoryRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(MinMaxCategoryRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
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
      CategoryPlot plot=new CategoryPlot(dataset,new CategoryAxis("Category"),new NumberAxis("Value"),new MinMaxCategoryRenderer());
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
}
