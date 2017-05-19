package org.jfree.chart.block.junit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.block.ColorBlock;
/** 
 * Tests for the             {@link ColorBlock} class.
 */
public class ColorBlockTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ColorBlockTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ColorBlockTests(  String name){
    super(name);
  }
  /** 
 * Confirm that the equals() method can distinguish all the required fields.
 */
  public void testEquals(){
    ColorBlock b1=new ColorBlock(Color.red,1.0,2.0);
    ColorBlock b2=new ColorBlock(Color.red,1.0,2.0);
    assertTrue(b1.equals(b2));
    assertTrue(b2.equals(b2));
    b1=new ColorBlock(Color.blue,1.0,2.0);
    assertFalse(b1.equals(b2));
    b2=new ColorBlock(Color.blue,1.0,2.0);
    assertTrue(b1.equals(b2));
    b1=new ColorBlock(Color.blue,1.1,2.0);
    assertFalse(b1.equals(b2));
    b2=new ColorBlock(Color.blue,1.1,2.0);
    assertTrue(b1.equals(b2));
    b1=new ColorBlock(Color.blue,1.1,2.2);
    assertFalse(b1.equals(b2));
    b2=new ColorBlock(Color.blue,1.1,2.2);
    assertTrue(b1.equals(b2));
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    GradientPaint gp=new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue);
    Rectangle2D bounds1=new Rectangle2D.Double(10.0,20.0,30.0,40.0);
    ColorBlock b1=new ColorBlock(gp,1.0,2.0);
    b1.setBounds(bounds1);
    ColorBlock b2=null;
    try {
      b2=(ColorBlock)b1.clone();
    }
 catch (    CloneNotSupportedException e) {
      fail(e.toString());
    }
    assertTrue(b1 != b2);
    assertTrue(b1.getClass() == b2.getClass());
    assertTrue(b1.equals(b2));
    bounds1.setRect(1.0,2.0,3.0,4.0);
    assertFalse(b1.equals(b2));
    b2.setBounds(new Rectangle2D.Double(1.0,2.0,3.0,4.0));
    assertTrue(b1.equals(b2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    GradientPaint gp=new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue);
    ColorBlock b1=new ColorBlock(gp,1.0,2.0);
    ColorBlock b2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(b1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      b2=(ColorBlock)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      fail(e.toString());
    }
    assertEquals(b1,b2);
  }
}
