package org.jfree.chart.renderer.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
/** 
 * Tests for the             {@link DefaultPolarItemRenderer} class.
 */
public class DefaultPolarItemRendererTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(DefaultPolarItemRendererTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public DefaultPolarItemRendererTests(  String name){
    super(name);
  }
  /** 
 * Check that the equals() method distinguishes all fields.
 */
  public void testEquals(){
    DefaultPolarItemRenderer r1=new DefaultPolarItemRenderer();
    DefaultPolarItemRenderer r2=new DefaultPolarItemRenderer();
    assertEquals(r1,r2);
    r1.setSeriesFilled(1,true);
    assertFalse(r1.equals(r2));
    r2.setSeriesFilled(1,true);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    DefaultPolarItemRenderer r1=new DefaultPolarItemRenderer();
    DefaultPolarItemRenderer r2=new DefaultPolarItemRenderer();
    assertTrue(r1.equals(r2));
    int h1=r1.hashCode();
    int h2=r2.hashCode();
    assertEquals(h1,h2);
  }
  /** 
 * Confirm that cloning works.
 */
  public void testCloning(){
    DefaultPolarItemRenderer r1=new DefaultPolarItemRenderer();
    DefaultPolarItemRenderer r2=null;
    try {
      r2=(DefaultPolarItemRenderer)r1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(r1 != r2);
    assertTrue(r1.getClass() == r2.getClass());
    assertTrue(r1.equals(r2));
    r1.setSeriesFilled(1,true);
    assertFalse(r1.equals(r2));
    r2.setSeriesFilled(1,true);
    assertTrue(r1.equals(r2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    DefaultPolarItemRenderer r1=new DefaultPolarItemRenderer();
    DefaultPolarItemRenderer r2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(r1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      r2=(DefaultPolarItemRenderer)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(r1,r2);
  }
}
