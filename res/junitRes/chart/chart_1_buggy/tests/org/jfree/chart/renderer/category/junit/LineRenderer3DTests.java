package org.jfree.chart.renderer.category.junit;
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
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link LineRenderer3D} class.
 */
public class LineRenderer3DTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(LineRenderer3DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public LineRenderer3DTests(  String name){
    super(name);
  }
  /** 
 * Test that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    LineRenderer3D r1=new LineRenderer3D();
    LineRenderer3D r2=new LineRenderer3D();
    assertEquals(r1,r2);
    r1.setXOffset(99.9);
    assertFalse(r1.equals(r2));
    r2.setXOffset(99.9);
    assertTrue(r1.equals(r2));
    r1.setYOffset(111.1);
    assertFalse(r1.equals(r2));
    r2.setYOffset(111.1);
    assertTrue(r1.equals(r2));
    r1.setWallPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertFalse(r1.equals(r2));
    r2.setWallPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    LineRenderer3D r1=new LineRenderer3D();
    LineRenderer3D r2=new LineRenderer3D();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    LineRenderer3D r1=new LineRenderer3D();
    LineRenderer3D r2=null;
    try {
      r2=(LineRenderer3D)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
    assertTrue(checkIndependence(r1,r2));
  }
  /** 
 * Check that this class implements PublicCloneable.
 */
  public void testPublicCloneable(){
    LineRenderer3D r1=new LineRenderer3D();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Checks that the two renderers are equal but independent of one another.
 * @param r1  renderer 1.
 * @param r2  renderer 2.
 * @return A boolean.
 */
  private boolean checkIndependence(  LineRenderer3D r1,  LineRenderer3D r2){
    boolean b0=r1.equals(r2);
    r1.setBaseLinesVisible(!r1.getBaseLinesVisible());
    if (r1.equals(r2)) {
      return false;
    }
    r2.setBaseLinesVisible(r1.getBaseLinesVisible());
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesLinesVisible(1,true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setSeriesLinesVisible(1,true);
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setBaseShapesVisible(!r1.getBaseShapesVisible());
    if (r1.equals(r2)) {
      return false;
    }
    r2.setBaseShapesVisible(r1.getBaseShapesVisible());
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesShapesVisible(1,true);
    if (r1.equals(r2)) {
      return false;
    }
    r2.setSeriesShapesVisible(1,true);
    if (!r1.equals(r2)) {
      return false;
    }
    r1.setSeriesShapesFilled(0,false);
    r2.setSeriesShapesFilled(0,true);
    boolean b7=!r1.equals(r2);
    r2.setSeriesShapesFilled(0,false);
    boolean b8=(r1.equals(r2));
    r1.setBaseShapesFilled(false);
    r2.setBaseShapesFilled(true);
    boolean b9=!r1.equals(r2);
    r2.setBaseShapesFilled(false);
    boolean b10=(r1.equals(r2));
    return b0 && b7 && b8&& b9&& b10;
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    LineRenderer3D r1=new LineRenderer3D();
    LineRenderer3D r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(LineRenderer3D)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}
