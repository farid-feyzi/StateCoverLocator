package org.jfree.chart.renderer.xy.junit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;
/** 
 * Tests for the             {@link StackedXYAreaRenderer} class.
 */
public class StackedXYAreaRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StackedXYAreaRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StackedXYAreaRendererTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    StackedXYAreaRenderer r1=new StackedXYAreaRenderer();
    StackedXYAreaRenderer r2=new StackedXYAreaRenderer();
    assertEquals(r1,r2);
    assertEquals(r2,r1);
    r1.setShapePaint(new GradientPaint(1.0f,2.0f,Color.yellow,3.0f,4.0f,Color.green));
    assertFalse(r1.equals(r2));
    r2.setShapePaint(new GradientPaint(1.0f,2.0f,Color.yellow,3.0f,4.0f,Color.green));
    assertTrue(r1.equals(r2));
    Stroke s=new BasicStroke(1.23f);
    r1.setShapeStroke(s);
    assertFalse(r1.equals(r2));
    r2.setShapeStroke(s);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    StackedXYAreaRenderer r1=new StackedXYAreaRenderer();
    StackedXYAreaRenderer r2=new StackedXYAreaRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    StackedXYAreaRenderer r1=new StackedXYAreaRenderer();
    StackedXYAreaRenderer r2=null;
    try {
      r2=(StackedXYAreaRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
  }
  /** 
 * Verify that this class implements             {@link PublicCloneable}.
 */
  public void testPublicCloneable(){
    StackedXYAreaRenderer r1=new StackedXYAreaRenderer();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StackedXYAreaRenderer r1=new StackedXYAreaRenderer();
    r1.setShapePaint(Color.red);
    r1.setShapeStroke(new BasicStroke(1.23f));
    StackedXYAreaRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(StackedXYAreaRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
  /** 
 * Check that the renderer is calculating the range bounds correctly.
 */
  public void testFindRangeBounds(){
    TableXYDataset dataset=RendererXYPackageTests.createTestTableXYDataset();
    JFreeChart chart=ChartFactory.createStackedXYAreaChart("Test Chart","X","Y",dataset,false);
    XYPlot plot=(XYPlot)chart.getPlot();
    NumberAxis rangeAxis=(NumberAxis)plot.getRangeAxis();
    Range bounds=rangeAxis.getRange();
    assertTrue(bounds.contains(6.0));
    assertTrue(bounds.contains(8.0));
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (particularly by code in the renderer).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      DefaultTableXYDataset dataset=new DefaultTableXYDataset();
      XYSeries s1=new XYSeries("Series 1",true,false);
      s1.add(5.0,5.0);
      s1.add(10.0,15.5);
      s1.add(15.0,9.5);
      s1.add(20.0,7.5);
      dataset.addSeries(s1);
      XYSeries s2=new XYSeries("Series 2",true,false);
      s2.add(5.0,5.0);
      s2.add(10.0,15.5);
      s2.add(15.0,9.5);
      s2.add(20.0,3.5);
      dataset.addSeries(s2);
      XYPlot plot=new XYPlot(dataset,new NumberAxis("X"),new NumberAxis("Y"),new StackedXYAreaRenderer());
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
 * A test for bug 1593156.
 */
  public void testBug1593156(){
    boolean success=false;
    try {
      DefaultTableXYDataset dataset=new DefaultTableXYDataset();
      XYSeries s1=new XYSeries("Series 1",true,false);
      s1.add(5.0,5.0);
      s1.add(10.0,15.5);
      s1.add(15.0,9.5);
      s1.add(20.0,7.5);
      dataset.addSeries(s1);
      XYSeries s2=new XYSeries("Series 2",true,false);
      s2.add(5.0,5.0);
      s2.add(10.0,15.5);
      s2.add(15.0,9.5);
      s2.add(20.0,3.5);
      dataset.addSeries(s2);
      StackedXYAreaRenderer renderer=new StackedXYAreaRenderer(XYAreaRenderer.LINES);
      XYPlot plot=new XYPlot(dataset,new NumberAxis("X"),new NumberAxis("Y"),renderer);
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
