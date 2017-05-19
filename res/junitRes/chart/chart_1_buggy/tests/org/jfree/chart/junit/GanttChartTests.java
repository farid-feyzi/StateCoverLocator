package org.jfree.chart.junit;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
/** 
 * Some tests for a Gantt chart.
 */
public class GanttChartTests extends TestCase {
  /** 
 * A chart. 
 */
  private JFreeChart chart;
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(GanttChartTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public GanttChartTests(  String name){
    super(name);
  }
  /** 
 * Common test setup.
 */
  protected void setUp(){
    this.chart=createGanttChart();
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (a problem that was occurring at one point).
 */
  public void testDrawWithNullInfo(){
    boolean success=false;
    try {
      BufferedImage image=new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
      Graphics2D g2=image.createGraphics();
      this.chart.draw(g2,new Rectangle2D.Double(0,0,200,100),null,null);
      g2.dispose();
      success=true;
    }
 catch (    Exception e) {
      e.printStackTrace();
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Draws the chart with a <code>null</code> info object to make sure that no exceptions are thrown (a problem that was occurring at one point).
 */
  public void testDrawWithNullInfo2(){
    boolean success=false;
    try {
      JFreeChart chart=createGanttChart();
      CategoryPlot plot=(CategoryPlot)chart.getPlot();
      plot.setDataset(createDataset());
      chart.createBufferedImage(300,200,null);
      success=true;
    }
 catch (    NullPointerException e) {
      success=false;
    }
    assertTrue(success);
  }
  /** 
 * Replaces the chart's dataset and then checks that the new dataset is OK.
 */
  public void testReplaceDataset(){
    LocalListener l=new LocalListener();
    this.chart.addChangeListener(l);
    CategoryPlot plot=(CategoryPlot)this.chart.getPlot();
    plot.setDataset(null);
    assertEquals(true,l.flag);
  }
  /** 
 * Check that setting a tool tip generator for a series does override the default generator.
 */
  public void testSetSeriesToolTipGenerator(){
    CategoryPlot plot=(CategoryPlot)this.chart.getPlot();
    CategoryItemRenderer renderer=plot.getRenderer();
    StandardCategoryToolTipGenerator tt=new StandardCategoryToolTipGenerator();
    renderer.setSeriesToolTipGenerator(0,tt);
    CategoryToolTipGenerator tt2=renderer.getToolTipGenerator(0,0,false);
    assertTrue(tt2 == tt);
  }
  /** 
 * Check that setting a URL generator for a series does override the default generator.
 */
  public void testSetSeriesURLGenerator(){
    CategoryPlot plot=(CategoryPlot)this.chart.getPlot();
    CategoryItemRenderer renderer=plot.getRenderer();
    StandardCategoryURLGenerator url1=new StandardCategoryURLGenerator();
    renderer.setSeriesURLGenerator(0,url1);
    CategoryURLGenerator url2=renderer.getURLGenerator(0,0,false);
    assertTrue(url2 == url1);
  }
  /** 
 * Create a Gantt chart.
 * @return The chart.
 */
  private static JFreeChart createGanttChart(){
    return ChartFactory.createGanttChart("Gantt Chart","Domain","Range",null,true);
  }
  /** 
 * Creates a sample dataset for a Gantt chart.
 * @return The dataset.
 */
  public static IntervalCategoryDataset createDataset(){
    TaskSeries s1=new TaskSeries("Scheduled");
    s1.add(new Task("Write Proposal",new SimpleTimePeriod(date(1,Calendar.APRIL,2001),date(5,Calendar.APRIL,2001))));
    s1.add(new Task("Obtain Approval",new SimpleTimePeriod(date(9,Calendar.APRIL,2001),date(9,Calendar.APRIL,2001))));
    s1.add(new Task("Requirements Analysis",new SimpleTimePeriod(date(10,Calendar.APRIL,2001),date(5,Calendar.MAY,2001))));
    s1.add(new Task("Design Phase",new SimpleTimePeriod(date(6,Calendar.MAY,2001),date(30,Calendar.MAY,2001))));
    s1.add(new Task("Design Signoff",new SimpleTimePeriod(date(2,Calendar.JUNE,2001),date(2,Calendar.JUNE,2001))));
    s1.add(new Task("Alpha Implementation",new SimpleTimePeriod(date(3,Calendar.JUNE,2001),date(31,Calendar.JULY,2001))));
    s1.add(new Task("Design Review",new SimpleTimePeriod(date(1,Calendar.AUGUST,2001),date(8,Calendar.AUGUST,2001))));
    s1.add(new Task("Revised Design Signoff",new SimpleTimePeriod(date(10,Calendar.AUGUST,2001),date(10,Calendar.AUGUST,2001))));
    s1.add(new Task("Beta Implementation",new SimpleTimePeriod(date(12,Calendar.AUGUST,2001),date(12,Calendar.SEPTEMBER,2001))));
    s1.add(new Task("Testing",new SimpleTimePeriod(date(13,Calendar.SEPTEMBER,2001),date(31,Calendar.OCTOBER,2001))));
    s1.add(new Task("Final Implementation",new SimpleTimePeriod(date(1,Calendar.NOVEMBER,2001),date(15,Calendar.NOVEMBER,2001))));
    s1.add(new Task("Signoff",new SimpleTimePeriod(date(28,Calendar.NOVEMBER,2001),date(30,Calendar.NOVEMBER,2001))));
    TaskSeries s2=new TaskSeries("Actual");
    s2.add(new Task("Write Proposal",new SimpleTimePeriod(date(1,Calendar.APRIL,2001),date(5,Calendar.APRIL,2001))));
    s2.add(new Task("Obtain Approval",new SimpleTimePeriod(date(9,Calendar.APRIL,2001),date(9,Calendar.APRIL,2001))));
    s2.add(new Task("Requirements Analysis",new SimpleTimePeriod(date(10,Calendar.APRIL,2001),date(15,Calendar.MAY,2001))));
    s2.add(new Task("Design Phase",new SimpleTimePeriod(date(15,Calendar.MAY,2001),date(17,Calendar.JUNE,2001))));
    s2.add(new Task("Design Signoff",new SimpleTimePeriod(date(30,Calendar.JUNE,2001),date(30,Calendar.JUNE,2001))));
    s2.add(new Task("Alpha Implementation",new SimpleTimePeriod(date(1,Calendar.JULY,2001),date(12,Calendar.SEPTEMBER,2001))));
    s2.add(new Task("Design Review",new SimpleTimePeriod(date(12,Calendar.SEPTEMBER,2001),date(22,Calendar.SEPTEMBER,2001))));
    s2.add(new Task("Revised Design Signoff",new SimpleTimePeriod(date(25,Calendar.SEPTEMBER,2001),date(27,Calendar.SEPTEMBER,2001))));
    s2.add(new Task("Beta Implementation",new SimpleTimePeriod(date(27,Calendar.SEPTEMBER,2001),date(30,Calendar.OCTOBER,2001))));
    s2.add(new Task("Testing",new SimpleTimePeriod(date(31,Calendar.OCTOBER,2001),date(17,Calendar.NOVEMBER,2001))));
    s2.add(new Task("Final Implementation",new SimpleTimePeriod(date(18,Calendar.NOVEMBER,2001),date(5,Calendar.DECEMBER,2001))));
    s2.add(new Task("Signoff",new SimpleTimePeriod(date(10,Calendar.DECEMBER,2001),date(11,Calendar.DECEMBER,2001))));
    TaskSeriesCollection collection=new TaskSeriesCollection();
    collection.add(s1);
    collection.add(s2);
    return collection;
  }
  /** 
 * Utility method for creating <code>Date</code> objects.
 * @param day  the date.
 * @param month  the month.
 * @param year  the year.
 * @return a date.
 */
  private static Date date(  int day,  int month,  int year){
    Calendar calendar=Calendar.getInstance();
    calendar.set(year,month,day);
    Date result=calendar.getTime();
    return result;
  }
  /** 
 * A chart change listener.
 */
static class LocalListener implements ChartChangeListener {
    /** 
 * A flag. 
 */
    private boolean flag=false;
    /** 
 * Event handler.
 * @param event  the event.
 */
    public void chartChanged(    ChartChangeEvent event){
      this.flag=true;
    }
  }
}
