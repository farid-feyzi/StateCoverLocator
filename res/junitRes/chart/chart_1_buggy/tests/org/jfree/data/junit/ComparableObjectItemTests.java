package org.jfree.data.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.ComparableObjectItem;
/** 
 * Tests for the             {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ComparableObjectItemTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ComparableObjectItemTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the constructor.
 */
  public void testConstructor(){
    boolean pass=false;
    try {
      new ComparableObjectItem(null,"XYZ");
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Confirm that the equals method can distinguish all the required fields.
 */
  public void testEquals(){
    ComparableObjectItem item1=new ComparableObjectItem(new Integer(1),"XYZ");
    ComparableObjectItem item2=new ComparableObjectItem(new Integer(1),"XYZ");
    assertTrue(item1.equals(item2));
    assertTrue(item2.equals(item1));
    item1=new ComparableObjectItem(new Integer(2),"XYZ");
    assertFalse(item1.equals(item2));
    item2=new ComparableObjectItem(new Integer(2),"XYZ");
    assertTrue(item1.equals(item2));
    item1=new ComparableObjectItem(new Integer(2),null);
    assertFalse(item1.equals(item2));
    item2=new ComparableObjectItem(new Integer(2),null);
    assertTrue(item1.equals(item2));
  }
  /** 
 * Some checks for the clone() method.
 */
  public void testCloning(){
    ComparableObjectItem item1=new ComparableObjectItem(new Integer(1),"XYZ");
    ComparableObjectItem item2=null;
    try {
      item2=(ComparableObjectItem)item1.clone();
    }
 catch (    CloneNotSupportedException e) {
      e.printStackTrace();
    }
    assertTrue(item1 != item2);
    assertTrue(item1.getClass() == item2.getClass());
    assertTrue(item1.equals(item2));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    ComparableObjectItem item1=new ComparableObjectItem(new Integer(1),"XYZ");
    ComparableObjectItem item2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(item1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      item2=(ComparableObjectItem)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    assertEquals(item1,item2);
  }
  /** 
 * Some checks for the compareTo() method.
 */
  public void testCompareTo(){
    ComparableObjectItem item1=new ComparableObjectItem(new Integer(1),"XYZ");
    ComparableObjectItem item2=new ComparableObjectItem(new Integer(2),"XYZ");
    ComparableObjectItem item3=new ComparableObjectItem(new Integer(3),"XYZ");
    ComparableObjectItem item4=new ComparableObjectItem(new Integer(1),"XYZ");
    assertTrue(item2.compareTo(item1) > 0);
    assertTrue(item3.compareTo(item1) > 0);
    assertTrue(item4.compareTo(item1) == 0);
    assertTrue(item1.compareTo(item2) < 0);
  }
}
