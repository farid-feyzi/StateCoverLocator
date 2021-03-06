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
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link BarRenderer3D} class.
 */
public class BarRenderer3DTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(BarRenderer3DTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public BarRenderer3DTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    BarRenderer3D r1=new BarRenderer3D(1.0,2.0);
    BarRenderer3D r2=new BarRenderer3D(1.0,2.0);
    assertEquals(r1,r2);
    r1=new BarRenderer3D(1.1,2.0);
    assertFalse(r1.equals(r2));
    r2=new BarRenderer3D(1.1,2.0);
    assertTrue(r1.equals(r2));
    r1=new BarRenderer3D(1.1,2.2);
    assertFalse(r1.equals(r2));
    r2=new BarRenderer3D(1.1,2.2);
    assertTrue(r1.equals(r2));
    r1.setWallPaint(new GradientPaint(1.0f,2.0f,Color.red,4.0f,3.0f,Color.blue));
    assertFalse(r1.equals(r2));
    r2.setWallPaint(new GradientPaint(1.0f,2.0f,Color.red,4.0f,3.0f,Color.blue));
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    BarRenderer3D r1=new BarRenderer3D();
    BarRenderer3D r2=new BarRenderer3D();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    BarRenderer3D r1=new BarRenderer3D();
    BarRenderer3D r2=null;
    try {
      r2=(BarRenderer3D)r1.clone();
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
    BarRenderer3D r1=new BarRenderer3D();
    assertTrue(r1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    BarRenderer3D r1=new BarRenderer3D();
    r1.setWallPaint(new GradientPaint(1.0f,2.0f,Color.red,4.0f,3.0f,Color.blue));
    BarRenderer3D r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(BarRenderer3D)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}
