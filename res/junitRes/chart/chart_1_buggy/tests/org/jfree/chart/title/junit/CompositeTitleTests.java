package org.jfree.chart.title.junit;
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
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.RectangleInsets;
/** 
 * Tests for the             {@link CompositeTitle} class.
 */
public class CompositeTitleTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(CompositeTitleTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public CompositeTitleTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the constructor.
 */
  public void testConstructor(){
    CompositeTitle t=new CompositeTitle();
    assertNull(t.getBackgroundPaint());
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    CompositeTitle t1=new CompositeTitle(new BlockContainer());
    CompositeTitle t2=new CompositeTitle(new BlockContainer());
    assertEquals(t1,t2);
    assertEquals(t2,t1);
    t1.setMargin(new RectangleInsets(1.0,2.0,3.0,4.0));
    assertFalse(t1.equals(t2));
    t2.setMargin(new RectangleInsets(1.0,2.0,3.0,4.0));
    assertTrue(t1.equals(t2));
    t1.setFrame(new BlockBorder(Color.red));
    assertFalse(t1.equals(t2));
    t2.setFrame(new BlockBorder(Color.red));
    assertTrue(t1.equals(t2));
    t1.setPadding(new RectangleInsets(1.0,2.0,3.0,4.0));
    assertFalse(t1.equals(t2));
    t2.setPadding(new RectangleInsets(1.0,2.0,3.0,4.0));
    assertTrue(t1.equals(t2));
    t1.getContainer().add(new TextTitle("T1"));
    assertFalse(t1.equals(t2));
    t2.getContainer().add(new TextTitle("T1"));
    assertTrue(t1.equals(t2));
    t1.setBackgroundPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertFalse(t1.equals(t2));
    t2.setBackgroundPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    assertTrue(t1.equals(t2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    CompositeTitle t1=new CompositeTitle(new BlockContainer());
    t1.getContainer().add(new TextTitle("T1"));
    CompositeTitle t2=new CompositeTitle(new BlockContainer());
    t2.getContainer().add(new TextTitle("T1"));
    assertTrue(t1.equals(t2));
    int h1=t1.hashCode();
    int h2=t2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    CompositeTitle t1=new CompositeTitle(new BlockContainer());
    t1.getContainer().add(new TextTitle("T1"));
    t1.setBackgroundPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.yellow));
    CompositeTitle t2=null;
    try {
      t2=(CompositeTitle)t1.clone();
    }
 catch (    CloneNotSupportedException e) {
      fail(e.toString());
    }
    assertTrue(t1 != t2);
    assertTrue(t1.getClass() == t2.getClass());
    assertTrue(t1.equals(t2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    CompositeTitle t1=new CompositeTitle(new BlockContainer());
    t1.getContainer().add(new TextTitle("T1"));
    t1.setBackgroundPaint(new GradientPaint(1.0f,2.0f,Color.red,3.0f,4.0f,Color.blue));
    CompositeTitle t2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(t1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      t2=(CompositeTitle)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(t1,t2);
  }
}
