package org.jfree.data.statistics.junit;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
/** 
 * Tests for the             {@link BoxAndWhiskerCalculator} class.
 */
public class BoxAndWhiskerCalculatorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(BoxAndWhiskerCalculatorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public BoxAndWhiskerCalculatorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the calculateBoxAndWhiskerStatistics() method.
 */
  public void testCalculateBoxAndWhiskerStatistics(){
    boolean pass=false;
    try {
      BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    List values=new ArrayList();
    values.add(new Double(1.1));
    BoxAndWhiskerItem item=BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values);
    assertEquals(1.1,item.getMean().doubleValue(),EPSILON);
    assertEquals(1.1,item.getMedian().doubleValue(),EPSILON);
    assertEquals(1.1,item.getQ1().doubleValue(),EPSILON);
    assertEquals(1.1,item.getQ3().doubleValue(),EPSILON);
  }
  private static final double EPSILON=0.000000001;
  /** 
 * Tests the Q1 calculation.
 */
  public void testCalculateQ1(){
    boolean pass=false;
    try {
      BoxAndWhiskerCalculator.calculateQ1(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    List values=new ArrayList();
    double q1=BoxAndWhiskerCalculator.calculateQ1(values);
    assertTrue(Double.isNaN(q1));
    values.add(new Double(1.0));
    q1=BoxAndWhiskerCalculator.calculateQ1(values);
    assertEquals(q1,1.0,EPSILON);
    values.add(new Double(2.0));
    q1=BoxAndWhiskerCalculator.calculateQ1(values);
    assertEquals(q1,1.0,EPSILON);
    values.add(new Double(3.0));
    q1=BoxAndWhiskerCalculator.calculateQ1(values);
    assertEquals(q1,1.5,EPSILON);
    values.add(new Double(4.0));
    q1=BoxAndWhiskerCalculator.calculateQ1(values);
    assertEquals(q1,1.5,EPSILON);
  }
  /** 
 * Tests the Q3 calculation.
 */
  public void testCalculateQ3(){
    boolean pass=false;
    try {
      BoxAndWhiskerCalculator.calculateQ3(null);
    }
 catch (    IllegalArgumentException e) {
      pass=true;
    }
    assertTrue(pass);
    List values=new ArrayList();
    double q3=BoxAndWhiskerCalculator.calculateQ3(values);
    assertTrue(Double.isNaN(q3));
    values.add(new Double(1.0));
    q3=BoxAndWhiskerCalculator.calculateQ3(values);
    assertEquals(q3,1.0,EPSILON);
    values.add(new Double(2.0));
    q3=BoxAndWhiskerCalculator.calculateQ3(values);
    assertEquals(q3,2.0,EPSILON);
    values.add(new Double(3.0));
    q3=BoxAndWhiskerCalculator.calculateQ3(values);
    assertEquals(q3,2.5,EPSILON);
    values.add(new Double(4.0));
    q3=BoxAndWhiskerCalculator.calculateQ3(values);
    assertEquals(q3,3.5,EPSILON);
  }
  /** 
 * The test case included in bug report 1593149.
 */
  public void test1593149(){
    ArrayList theList=new ArrayList(5);
    theList.add(0,new Double(1.0));
    theList.add(1,new Double(2.0));
    theList.add(2,new Double(Double.NaN));
    theList.add(3,new Double(3.0));
    theList.add(4,new Double(4.0));
    BoxAndWhiskerItem theItem=BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(theList);
    assertEquals(1.0,theItem.getMinRegularValue().doubleValue(),EPSILON);
    assertEquals(4.0,theItem.getMaxRegularValue().doubleValue(),EPSILON);
  }
}
