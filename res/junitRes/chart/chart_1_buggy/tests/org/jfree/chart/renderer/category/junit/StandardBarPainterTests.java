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
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.util.PublicCloneable;
/** 
 * Tests for the             {@link StandardBarPainter} class.
 */
public class StandardBarPainterTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardBarPainterTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardBarPainterTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    StandardBarPainter p1=new StandardBarPainter();
    StandardBarPainter p2=new StandardBarPainter();
    assertEquals(p1,p2);
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    StandardBarPainter p1=new StandardBarPainter();
    StandardBarPainter p2=new StandardBarPainter();
    assertTrue(p1.equals(p2));
    int h1=p1.hashCode();
    int h2=p2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning isn't implemented (it isn't required, because instances of the class are immutable).
 */
  public void testCloning(){
    StandardBarPainter p1=new StandardBarPainter();
    assertFalse(p1 instanceof Cloneable);
    assertFalse(p1 instanceof PublicCloneable);
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    StandardBarPainter p1=new StandardBarPainter();
    StandardBarPainter p2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(p1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      p2=(StandardBarPainter)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(p1,p2);
  }
}
