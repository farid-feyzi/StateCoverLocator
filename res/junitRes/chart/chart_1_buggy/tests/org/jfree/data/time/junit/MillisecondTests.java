package org.jfree.data.time.junit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.MonthConstants;
import org.jfree.data.time.Second;
/** 
 * Tests for the             {@link Millisecond} class.
 */
public class MillisecondTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(MillisecondTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public MillisecondTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
  }
  /** 
 * Check that a             {@link Millisecond} instance is equal to itself.SourceForge Bug ID: 558850.
 */
  public void testEqualsSelf(){
    Millisecond millisecond=new Millisecond();
    assertTrue(millisecond.equals(millisecond));
  }
  /** 
 * Tests the equals method.
 */
  public void testEquals(){
    Day day1=new Day(29,MonthConstants.MARCH,2002);
    Hour hour1=new Hour(15,day1);
    Minute minute1=new Minute(15,hour1);
    Second second1=new Second(34,minute1);
    Millisecond milli1=new Millisecond(999,second1);
    Day day2=new Day(29,MonthConstants.MARCH,2002);
    Hour hour2=new Hour(15,day2);
    Minute minute2=new Minute(15,hour2);
    Second second2=new Second(34,minute2);
    Millisecond milli2=new Millisecond(999,second2);
    assertTrue(milli1.equals(milli2));
  }
  /** 
 * In GMT, the 4.55:59.123pm on 21 Mar 2002 is java.util.Date(1016729759123L).  Use this to check the Millisecond constructor.
 */
  public void testDateConstructor1(){
    TimeZone zone=TimeZone.getTimeZone("GMT");
    Calendar c=new GregorianCalendar(zone);
    Millisecond m1=new Millisecond(new Date(1016729759122L),zone);
    Millisecond m2=new Millisecond(new Date(1016729759123L),zone);
    assertEquals(122,m1.getMillisecond());
    assertEquals(1016729759122L,m1.getLastMillisecond(c));
    assertEquals(123,m2.getMillisecond());
    assertEquals(1016729759123L,m2.getFirstMillisecond(c));
  }
  /** 
 * In Tallinn, the 4.55:59.123pm on 21 Mar 2002 is java.util.Date(1016722559123L).  Use this to check the Millisecond constructor.
 */
  public void testDateConstructor2(){
    TimeZone zone=TimeZone.getTimeZone("Europe/Tallinn");
    Calendar c=new GregorianCalendar(zone);
    Millisecond m1=new Millisecond(new Date(1016722559122L),zone);
    Millisecond m2=new Millisecond(new Date(1016722559123L),zone);
    assertEquals(122,m1.getMillisecond());
    assertEquals(1016722559122L,m1.getLastMillisecond(c));
    assertEquals(123,m2.getMillisecond());
    assertEquals(1016722559123L,m2.getFirstMillisecond(c));
  }
  /** 
 * Serialize an instance, restore it, and check for equality.
 */
  public void testSerialization(){
    Millisecond m1=new Millisecond();
    Millisecond m2=null;
    try {
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      ObjectOutput out=new ObjectOutputStream(buffer);
      out.writeObject(m1);
      out.close();
      ObjectInput in=new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
      m2=(Millisecond)in.readObject();
      in.close();
    }
 catch (    Exception e) {
      System.out.println(e.toString());
    }
    assertEquals(m1,m2);
  }
  /** 
 * Two objects that are equal are required to return the same hashCode.
 */
  public void testHashcode(){
    Millisecond m1=new Millisecond(599,23,45,7,9,10,2007);
    Millisecond m2=new Millisecond(599,23,45,7,9,10,2007);
    assertTrue(m1.equals(m2));
    int hash1=m1.hashCode();
    int hash2=m2.hashCode();
    assertEquals(hash1,hash2);
  }
  /** 
 * A test for bug report 943985 - the calculation for the middle millisecond is incorrect for odd milliseconds.
 */
  public void test943985(){
    Millisecond ms=new Millisecond(new java.util.Date(4));
    assertEquals(ms.getFirstMillisecond(),ms.getMiddleMillisecond());
    assertEquals(ms.getMiddleMillisecond(),ms.getLastMillisecond());
    ms=new Millisecond(new java.util.Date(5));
    assertEquals(ms.getFirstMillisecond(),ms.getMiddleMillisecond());
    assertEquals(ms.getMiddleMillisecond(),ms.getLastMillisecond());
  }
  /** 
 * The             {@link Millisecond} class is immutable, so should not be{@link Cloneable}.
 */
  public void testNotCloneable(){
    Millisecond m=new Millisecond(599,23,45,7,9,10,2007);
    assertFalse(m instanceof Cloneable);
  }
  /** 
 * Some checks for the getFirstMillisecond() method.
 */
  public void testGetFirstMillisecond(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.UK);
    TimeZone savedZone=TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Millisecond m=new Millisecond(500,15,43,15,1,4,2006);
    assertEquals(1143902595500L,m.getFirstMillisecond());
    Locale.setDefault(saved);
    TimeZone.setDefault(savedZone);
  }
  /** 
 * Some checks for the getFirstMillisecond(TimeZone) method.
 */
  public void testGetFirstMillisecondWithTimeZone(){
    Millisecond m=new Millisecond(500,50,59,15,1,4,1950);
    TimeZone zone=TimeZone.getTimeZone("America/Los_Angeles");
    Calendar c=new GregorianCalendar(zone);
    assertEquals(-623289609500L,m.getFirstMillisecond(c));
    boolean pass=false;
    try {
      m.getFirstMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getFirstMillisecond(TimeZone) method.
 */
  public void testGetFirstMillisecondWithCalendar(){
    Millisecond m=new Millisecond(500,55,40,2,15,4,2000);
    GregorianCalendar calendar=new GregorianCalendar(Locale.GERMANY);
    calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
    assertEquals(955766455500L,m.getFirstMillisecond(calendar));
    boolean pass=false;
    try {
      m.getFirstMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getLastMillisecond() method.
 */
  public void testGetLastMillisecond(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.UK);
    TimeZone savedZone=TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Millisecond m=new Millisecond(750,1,1,1,1,1,1970);
    assertEquals(61750L,m.getLastMillisecond());
    Locale.setDefault(saved);
    TimeZone.setDefault(savedZone);
  }
  /** 
 * Some checks for the getLastMillisecond(TimeZone) method.
 */
  public void testGetLastMillisecondWithTimeZone(){
    Millisecond m=new Millisecond(750,55,1,2,7,7,1950);
    TimeZone zone=TimeZone.getTimeZone("America/Los_Angeles");
    Calendar c=new GregorianCalendar(zone);
    assertEquals(-614962684250L,m.getLastMillisecond(c));
    boolean pass=false;
    try {
      m.getLastMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getLastMillisecond(TimeZone) method.
 */
  public void testGetLastMillisecondWithCalendar(){
    Millisecond m=new Millisecond(250,50,45,21,21,4,2001);
    GregorianCalendar calendar=new GregorianCalendar(Locale.GERMANY);
    calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
    assertEquals(987889550250L,m.getLastMillisecond(calendar));
    boolean pass=false;
    try {
      m.getLastMillisecond((Calendar)null);
    }
 catch (    NullPointerException e) {
      pass=true;
    }
    assertTrue(pass);
  }
  /** 
 * Some checks for the getSerialIndex() method.
 */
  public void testGetSerialIndex(){
    Millisecond m=new Millisecond(500,1,1,1,1,1,2000);
    assertEquals(3155850061500L,m.getSerialIndex());
    m=new Millisecond(500,1,1,1,1,1,1900);
    assertEquals(176461500L,m.getSerialIndex());
  }
  /** 
 * Some checks for the testNext() method.
 */
  public void testNext(){
    Millisecond m=new Millisecond(555,55,30,1,12,12,2000);
    m=(Millisecond)m.next();
    assertEquals(2000,m.getSecond().getMinute().getHour().getYear());
    assertEquals(12,m.getSecond().getMinute().getHour().getMonth());
    assertEquals(12,m.getSecond().getMinute().getHour().getDayOfMonth());
    assertEquals(1,m.getSecond().getMinute().getHour().getHour());
    assertEquals(30,m.getSecond().getMinute().getMinute());
    assertEquals(55,m.getSecond().getSecond());
    assertEquals(556,m.getMillisecond());
    m=new Millisecond(999,59,59,23,31,12,9999);
    assertNull(m.next());
  }
  /** 
 * Some checks for the getStart() method.
 */
  public void testGetStart(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.ITALY);
    Calendar cal=Calendar.getInstance(Locale.ITALY);
    cal.set(2006,Calendar.JANUARY,16,3,47,55);
    cal.set(Calendar.MILLISECOND,555);
    Millisecond m=new Millisecond(555,55,47,3,16,1,2006);
    assertEquals(cal.getTime(),m.getStart());
    Locale.setDefault(saved);
  }
  /** 
 * Some checks for the getEnd() method.
 */
  public void testGetEnd(){
    Locale saved=Locale.getDefault();
    Locale.setDefault(Locale.ITALY);
    Calendar cal=Calendar.getInstance(Locale.ITALY);
    cal.set(2006,Calendar.JANUARY,16,3,47,55);
    cal.set(Calendar.MILLISECOND,555);
    Millisecond m=new Millisecond(555,55,47,3,16,1,2006);
    assertEquals(cal.getTime(),m.getEnd());
    Locale.setDefault(saved);
  }
}
