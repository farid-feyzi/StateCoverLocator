package org.jfree.chart.renderer.category;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
/** 
 * A renderer for simple Gantt charts.  The example shown here is generated by the <code>GanttDemo1.java</code> program included in the JFreeChart Demo Collection: <br><br> <img src="../../../../../images/GanttRendererSample.png" alt="GanttRendererSample.png" />
 */
public class GanttRenderer extends IntervalBarRenderer implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4010349116350119512L;
  /** 
 * The paint for displaying the percentage complete. 
 */
  private transient Paint completePaint;
  /** 
 * The paint for displaying the incomplete part of a task. 
 */
  private transient Paint incompletePaint;
  /** 
 * Controls the starting edge of the progress indicator (expressed as a percentage of the overall bar width).
 */
  private double startPercent;
  /** 
 * Controls the ending edge of the progress indicator (expressed as a percentage of the overall bar width).
 */
  private double endPercent;
  /** 
 * Creates a new renderer.
 */
  public GanttRenderer(){
    super();
    setIncludeBaseInRange(false);
    this.completePaint=Color.green;
    this.incompletePaint=Color.red;
    this.startPercent=0.35;
    this.endPercent=0.65;
  }
  /** 
 * Returns the paint used to show the percentage complete.
 * @return The paint (never <code>null</code>.
 * @see #setCompletePaint(Paint)
 */
  public Paint getCompletePaint(){
    return this.completePaint;
  }
  /** 
 * Sets the paint used to show the percentage complete and sends a                                                                                              {@link RendererChangeEvent} to all registered listeners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getCompletePaint()
 */
  public void setCompletePaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.completePaint=paint;
    fireChangeEvent();
  }
  /** 
 * Returns the paint used to show the percentage incomplete.
 * @return The paint (never <code>null</code>).
 * @see #setCompletePaint(Paint)
 */
  public Paint getIncompletePaint(){
    return this.incompletePaint;
  }
  /** 
 * Sets the paint used to show the percentage incomplete and sends a                                                                                              {@link RendererChangeEvent} to all registered listeners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getIncompletePaint()
 */
  public void setIncompletePaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.incompletePaint=paint;
    fireChangeEvent();
  }
  /** 
 * Returns the position of the start of the progress indicator, as a percentage of the bar width.
 * @return The start percent.
 * @see #setStartPercent(double)
 */
  public double getStartPercent(){
    return this.startPercent;
  }
  /** 
 * Sets the position of the start of the progress indicator, as a percentage of the bar width, and sends a                                                                                               {@link RendererChangeEvent} toall registered listeners.
 * @param percent  the percent.
 * @see #getStartPercent()
 */
  public void setStartPercent(  double percent){
    this.startPercent=percent;
    fireChangeEvent();
  }
  /** 
 * Returns the position of the end of the progress indicator, as a percentage of the bar width.
 * @return The end percent.
 * @see #setEndPercent(double)
 */
  public double getEndPercent(){
    return this.endPercent;
  }
  /** 
 * Sets the position of the end of the progress indicator, as a percentage of the bar width, and sends a                                                                                               {@link RendererChangeEvent} to allregistered listeners.
 * @param percent  the percent.
 * @see #getEndPercent()
 */
  public void setEndPercent(  double percent){
    this.endPercent=percent;
    fireChangeEvent();
  }
  /** 
 * Draws the bar for a single (series, category) data item.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the dataset.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param pass  the pass index.
 */
  public void drawItem(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  CategoryDataset dataset,  int row,  int column,  boolean selected,  int pass){
    if (dataset instanceof GanttCategoryDataset) {
      GanttCategoryDataset gcd=(GanttCategoryDataset)dataset;
      drawTasks(g2,state,dataArea,plot,domainAxis,rangeAxis,gcd,row,column,selected);
    }
 else {
      super.drawItem(g2,state,dataArea,plot,domainAxis,rangeAxis,dataset,row,column,selected,pass);
    }
  }
  /** 
 * Draws the tasks/subtasks for one item.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data plot area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the data.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 */
  protected void drawTasks(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  GanttCategoryDataset dataset,  int row,  int column,  boolean selected){
    int count=dataset.getSubIntervalCount(row,column);
    if (count == 0) {
      drawTask(g2,state,dataArea,plot,domainAxis,rangeAxis,dataset,row,column,selected);
    }
    PlotOrientation orientation=plot.getOrientation();
    for (int subinterval=0; subinterval < count; subinterval++) {
      RectangleEdge rangeAxisLocation=plot.getRangeAxisEdge();
      Number value0=dataset.getStartValue(row,column,subinterval);
      if (value0 == null) {
        return;
      }
      double translatedValue0=rangeAxis.valueToJava2D(value0.doubleValue(),dataArea,rangeAxisLocation);
      Number value1=dataset.getEndValue(row,column,subinterval);
      if (value1 == null) {
        return;
      }
      double translatedValue1=rangeAxis.valueToJava2D(value1.doubleValue(),dataArea,rangeAxisLocation);
      if (translatedValue1 < translatedValue0) {
        double temp=translatedValue1;
        translatedValue1=translatedValue0;
        translatedValue0=temp;
      }
      double rectStart=calculateBarW0(plot,plot.getOrientation(),dataArea,domainAxis,state,row,column);
      double rectLength=Math.abs(translatedValue1 - translatedValue0);
      double rectBreadth=state.getBarWidth();
      Rectangle2D bar=null;
      RectangleEdge barBase=null;
      if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
        bar=new Rectangle2D.Double(translatedValue0,rectStart,rectLength,rectBreadth);
        barBase=RectangleEdge.LEFT;
      }
 else {
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
          bar=new Rectangle2D.Double(rectStart,translatedValue0,rectBreadth,rectLength);
          barBase=RectangleEdge.BOTTOM;
        }
      }
      Rectangle2D completeBar=null;
      Rectangle2D incompleteBar=null;
      Number percent=dataset.getPercentComplete(row,column,subinterval);
      double start=getStartPercent();
      double end=getEndPercent();
      if (percent != null) {
        double p=percent.doubleValue();
        if (orientation == PlotOrientation.HORIZONTAL) {
          completeBar=new Rectangle2D.Double(translatedValue0,rectStart + start * rectBreadth,rectLength * p,rectBreadth * (end - start));
          incompleteBar=new Rectangle2D.Double(translatedValue0 + rectLength * p,rectStart + start * rectBreadth,rectLength * (1 - p),rectBreadth * (end - start));
        }
 else {
          if (orientation == PlotOrientation.VERTICAL) {
            completeBar=new Rectangle2D.Double(rectStart + start * rectBreadth,translatedValue0 + rectLength * (1 - p),rectBreadth * (end - start),rectLength * p);
            incompleteBar=new Rectangle2D.Double(rectStart + start * rectBreadth,translatedValue0,rectBreadth * (end - start),rectLength * (1 - p));
          }
        }
      }
      if (getShadowsVisible()) {
        getBarPainter().paintBarShadow(g2,this,row,column,selected,bar,barBase,true);
      }
      getBarPainter().paintBar(g2,this,row,column,selected,bar,barBase);
      if (completeBar != null) {
        g2.setPaint(getCompletePaint());
        g2.fill(completeBar);
      }
      if (incompleteBar != null) {
        g2.setPaint(getIncompletePaint());
        g2.fill(incompleteBar);
      }
      if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
        g2.setStroke(getItemStroke(row,column,selected));
        g2.setPaint(getItemOutlinePaint(row,column,selected));
        g2.draw(bar);
      }
      if (subinterval == count - 1) {
        int datasetIndex=plot.indexOf(dataset);
        Comparable columnKey=dataset.getColumnKey(column);
        Comparable rowKey=dataset.getRowKey(row);
        double xx=domainAxis.getCategorySeriesMiddle(columnKey,rowKey,dataset,getItemMargin(),dataArea,plot.getDomainAxisEdge());
        updateCrosshairValues(state.getCrosshairState(),dataset.getRowKey(row),dataset.getColumnKey(column),value1.doubleValue(),datasetIndex,xx,translatedValue1,orientation);
      }
      if (state.getInfo() != null) {
        EntityCollection entities=state.getEntityCollection();
        if (entities != null) {
          addEntity(entities,bar,dataset,row,column,selected);
        }
      }
    }
  }
  /** 
 * Draws a single task.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data plot area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the data.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 * @since 1.2.0
 */
  protected void drawTask(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  GanttCategoryDataset dataset,  int row,  int column,  boolean selected){
    PlotOrientation orientation=plot.getOrientation();
    RectangleEdge rangeAxisLocation=plot.getRangeAxisEdge();
    Number value0=dataset.getEndValue(row,column);
    if (value0 == null) {
      return;
    }
    double java2dValue0=rangeAxis.valueToJava2D(value0.doubleValue(),dataArea,rangeAxisLocation);
    Number value1=dataset.getStartValue(row,column);
    if (value1 == null) {
      return;
    }
    double java2dValue1=rangeAxis.valueToJava2D(value1.doubleValue(),dataArea,rangeAxisLocation);
    if (java2dValue1 < java2dValue0) {
      double temp=java2dValue1;
      java2dValue1=java2dValue0;
      java2dValue0=temp;
      Number tempNum=value1;
      value1=value0;
      value0=tempNum;
    }
    double rectStart=calculateBarW0(plot,orientation,dataArea,domainAxis,state,row,column);
    double rectBreadth=state.getBarWidth();
    double rectLength=Math.abs(java2dValue1 - java2dValue0);
    Rectangle2D bar=null;
    RectangleEdge barBase=null;
    if (orientation == PlotOrientation.HORIZONTAL) {
      bar=new Rectangle2D.Double(java2dValue0,rectStart,rectLength,rectBreadth);
      barBase=RectangleEdge.LEFT;
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        bar=new Rectangle2D.Double(rectStart,java2dValue1,rectBreadth,rectLength);
        barBase=RectangleEdge.BOTTOM;
      }
    }
    Rectangle2D completeBar=null;
    Rectangle2D incompleteBar=null;
    Number percent=dataset.getPercentComplete(row,column);
    double start=getStartPercent();
    double end=getEndPercent();
    if (percent != null) {
      double p=percent.doubleValue();
      if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
        completeBar=new Rectangle2D.Double(java2dValue0,rectStart + start * rectBreadth,rectLength * p,rectBreadth * (end - start));
        incompleteBar=new Rectangle2D.Double(java2dValue0 + rectLength * p,rectStart + start * rectBreadth,rectLength * (1 - p),rectBreadth * (end - start));
      }
 else {
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
          completeBar=new Rectangle2D.Double(rectStart + start * rectBreadth,java2dValue1 + rectLength * (1 - p),rectBreadth * (end - start),rectLength * p);
          incompleteBar=new Rectangle2D.Double(rectStart + start * rectBreadth,java2dValue1,rectBreadth * (end - start),rectLength * (1 - p));
        }
      }
    }
    if (getShadowsVisible()) {
      getBarPainter().paintBarShadow(g2,this,row,column,selected,bar,barBase,true);
    }
    getBarPainter().paintBar(g2,this,row,column,selected,bar,barBase);
    if (completeBar != null) {
      g2.setPaint(getCompletePaint());
      g2.fill(completeBar);
    }
    if (incompleteBar != null) {
      g2.setPaint(getIncompletePaint());
      g2.fill(incompleteBar);
    }
    if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
      Stroke stroke=getItemOutlineStroke(row,column,selected);
      Paint paint=getItemOutlinePaint(row,column,selected);
      if (stroke != null && paint != null) {
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(bar);
      }
    }
    CategoryItemLabelGenerator generator=getItemLabelGenerator(row,column,selected);
    if (generator != null && isItemLabelVisible(row,column,selected)) {
      drawItemLabelForBar(g2,plot,dataset,row,column,selected,generator,bar,false);
    }
    int datasetIndex=plot.indexOf(dataset);
    Comparable columnKey=dataset.getColumnKey(column);
    Comparable rowKey=dataset.getRowKey(row);
    double xx=domainAxis.getCategorySeriesMiddle(columnKey,rowKey,dataset,getItemMargin(),dataArea,plot.getDomainAxisEdge());
    updateCrosshairValues(state.getCrosshairState(),dataset.getRowKey(row),dataset.getColumnKey(column),value1.doubleValue(),datasetIndex,xx,java2dValue1,orientation);
    EntityCollection entities=state.getEntityCollection();
    if (entities != null) {
      addEntity(entities,bar,dataset,row,column,selected);
    }
  }
  /** 
 * Returns the Java2D coordinate for the middle of the specified data item.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @param dataset  the dataset.
 * @param axis  the axis.
 * @param area  the drawing area.
 * @param edge  the edge along which the axis lies.
 * @return The Java2D coordinate.
 * @since 1.0.11
 */
  public double getItemMiddle(  Comparable rowKey,  Comparable columnKey,  CategoryDataset dataset,  CategoryAxis axis,  Rectangle2D area,  RectangleEdge edge){
    return axis.getCategorySeriesMiddle(columnKey,rowKey,dataset,getItemMargin(),area,edge);
  }
  /** 
 * Tests this renderer for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof GanttRenderer)) {
      return false;
    }
    GanttRenderer that=(GanttRenderer)obj;
    if (!PaintUtilities.equal(this.completePaint,that.completePaint)) {
      return false;
    }
    if (!PaintUtilities.equal(this.incompletePaint,that.incompletePaint)) {
      return false;
    }
    if (this.startPercent != that.startPercent) {
      return false;
    }
    if (this.endPercent != that.endPercent) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.completePaint,stream);
    SerialUtilities.writePaint(this.incompletePaint,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.completePaint=SerialUtilities.readPaint(stream);
    this.incompletePaint=SerialUtilities.readPaint(stream);
  }
}
