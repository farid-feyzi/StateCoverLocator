package org.jfree.chart.renderer.category;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
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
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;
/** 
 * A renderer that handles the drawing a bar plot where each bar has a mean value and a standard deviation line.  The example shown here is generated by the <code>StatisticalBarChartDemo1.java</code> program included in the JFreeChart Demo Collection: <br><br> <img src="../../../../../images/StatisticalBarRendererSample.png" alt="StatisticalBarRendererSample.png" />
 */
public class StatisticalBarRenderer extends BarRenderer implements CategoryItemRenderer, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4986038395414039117L;
  /** 
 * The paint used to show the error indicator. 
 */
  private transient Paint errorIndicatorPaint;
  /** 
 * The stroke used to draw the error indicators.
 * @since 1.0.8
 */
  private transient Stroke errorIndicatorStroke;
  /** 
 * Default constructor.
 */
  public StatisticalBarRenderer(){
    super();
    this.errorIndicatorPaint=Color.gray;
    this.errorIndicatorStroke=new BasicStroke(0.5f);
  }
  /** 
 * Returns the paint used for the error indicators.
 * @return The paint used for the error indicators (possibly<code>null</code>).
 * @see #setErrorIndicatorPaint(Paint)
 */
  public Paint getErrorIndicatorPaint(){
    return this.errorIndicatorPaint;
  }
  /** 
 * Sets the paint used for the error indicators (if <code>null</code>, the item outline paint is used instead) and sends a                              {@link RendererChangeEvent} to all registered listeners.
 * @param paint  the paint (<code>null</code> permitted).
 * @see #getErrorIndicatorPaint()
 */
  public void setErrorIndicatorPaint(  Paint paint){
    this.errorIndicatorPaint=paint;
    fireChangeEvent();
  }
  /** 
 * Returns the stroke used to draw the error indicators.  If this is <code>null</code>, the renderer will use the item outline stroke).
 * @return The stroke (possibly <code>null</code>).
 * @see #setErrorIndicatorStroke(Stroke)
 * @since 1.0.8
 */
  public Stroke getErrorIndicatorStroke(){
    return this.errorIndicatorStroke;
  }
  /** 
 * Sets the stroke used to draw the error indicators, and sends a                              {@link RendererChangeEvent} to all registered listeners.  If you setthis to <code>null</code>, the renderer will use the item outline stroke.
 * @param stroke  the stroke (<code>null</code> permitted).
 * @see #getErrorIndicatorStroke()
 * @since 1.0.8
 */
  public void setErrorIndicatorStroke(  Stroke stroke){
    this.errorIndicatorStroke=stroke;
    fireChangeEvent();
  }
  /** 
 * Returns the range of values the renderer requires to display all the items from the specified dataset. This takes into account the range between the min/max values, possibly ignoring invisible series.
 * @param dataset  the dataset (<code>null</code> permitted).
 * @return The range (or <code>null</code> if the dataset is<code>null</code> or empty).
 */
  public Range findRangeBounds(  CategoryDataset dataset){
    return findRangeBounds(dataset,true);
  }
  /** 
 * Draws the bar with its standard deviation line range for a single (series, category) data item.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the dataset.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 * @param pass  the pass index.
 */
  public void drawItem(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  CategoryDataset dataset,  int row,  int column,  boolean selected,  int pass){
    int visibleRow=state.getVisibleSeriesIndex(row);
    if (visibleRow < 0) {
      return;
    }
    if (!(dataset instanceof StatisticalCategoryDataset)) {
      throw new IllegalArgumentException("Requires StatisticalCategoryDataset.");
    }
    StatisticalCategoryDataset statDataset=(StatisticalCategoryDataset)dataset;
    PlotOrientation orientation=plot.getOrientation();
    if (orientation == PlotOrientation.HORIZONTAL) {
      drawHorizontalItem(g2,state,dataArea,plot,domainAxis,rangeAxis,statDataset,visibleRow,row,column,selected);
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        drawVerticalItem(g2,state,dataArea,plot,domainAxis,rangeAxis,statDataset,visibleRow,row,column,selected);
      }
    }
  }
  /** 
 * Draws an item for a plot with a horizontal orientation.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the data.
 * @param visibleRow  the visible row index.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 * @since 1.2.0
 */
  protected void drawHorizontalItem(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  StatisticalCategoryDataset dataset,  int visibleRow,  int row,  int column,  boolean selected){
    RectangleEdge xAxisLocation=plot.getDomainAxisEdge();
    double rectY=domainAxis.getCategoryStart(column,getColumnCount(),dataArea,xAxisLocation);
    int seriesCount=state.getVisibleSeriesCount() >= 0 ? state.getVisibleSeriesCount() : getRowCount();
    int categoryCount=getColumnCount();
    if (seriesCount > 1) {
      double seriesGap=dataArea.getHeight() * getItemMargin() / (categoryCount * (seriesCount - 1));
      rectY=rectY + visibleRow * (state.getBarWidth() + seriesGap);
    }
 else {
      rectY=rectY + visibleRow * state.getBarWidth();
    }
    Number meanValue=dataset.getMeanValue(row,column);
    if (meanValue == null) {
      return;
    }
    double value=meanValue.doubleValue();
    double base=0.0;
    double lclip=rangeAxis.getLowerBound();
    double uclip=rangeAxis.getUpperBound();
    if (uclip <= 0.0) {
      if (value >= uclip) {
        return;
      }
      base=uclip;
      if (value <= lclip) {
        value=lclip;
      }
    }
 else {
      if (lclip <= 0.0) {
        if (value >= uclip) {
          value=uclip;
        }
 else {
          if (value <= lclip) {
            value=lclip;
          }
        }
      }
 else {
        if (value <= lclip) {
          return;
        }
        base=rangeAxis.getLowerBound();
        if (value >= uclip) {
          value=uclip;
        }
      }
    }
    RectangleEdge yAxisLocation=plot.getRangeAxisEdge();
    double transY1=rangeAxis.valueToJava2D(base,dataArea,yAxisLocation);
    double transY2=rangeAxis.valueToJava2D(value,dataArea,yAxisLocation);
    double rectX=Math.min(transY2,transY1);
    double rectHeight=state.getBarWidth();
    double rectWidth=Math.abs(transY2 - transY1);
    Rectangle2D bar=new Rectangle2D.Double(rectX,rectY,rectWidth,rectHeight);
    Paint itemPaint=getItemPaint(row,column,selected);
    GradientPaintTransformer t=getGradientPaintTransformer();
    if (t != null && itemPaint instanceof GradientPaint) {
      itemPaint=t.transform((GradientPaint)itemPaint,bar);
    }
    g2.setPaint(itemPaint);
    g2.fill(bar);
    if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
      Stroke stroke=getItemOutlineStroke(row,column,selected);
      Paint paint=getItemOutlinePaint(row,column,selected);
      if (stroke != null && paint != null) {
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(bar);
      }
    }
    Number n=dataset.getStdDevValue(row,column);
    if (n != null) {
      double valueDelta=n.doubleValue();
      double highVal=rangeAxis.valueToJava2D(meanValue.doubleValue() + valueDelta,dataArea,yAxisLocation);
      double lowVal=rangeAxis.valueToJava2D(meanValue.doubleValue() - valueDelta,dataArea,yAxisLocation);
      if (this.errorIndicatorPaint != null) {
        g2.setPaint(this.errorIndicatorPaint);
      }
 else {
        g2.setPaint(getItemOutlinePaint(row,column,selected));
      }
      if (this.errorIndicatorStroke != null) {
        g2.setStroke(this.errorIndicatorStroke);
      }
 else {
        g2.setStroke(getItemOutlineStroke(row,column,selected));
      }
      Line2D line=null;
      line=new Line2D.Double(lowVal,rectY + rectHeight / 2.0d,highVal,rectY + rectHeight / 2.0d);
      g2.draw(line);
      line=new Line2D.Double(highVal,rectY + rectHeight * 0.25,highVal,rectY + rectHeight * 0.75);
      g2.draw(line);
      line=new Line2D.Double(lowVal,rectY + rectHeight * 0.25,lowVal,rectY + rectHeight * 0.75);
      g2.draw(line);
    }
    CategoryItemLabelGenerator generator=getItemLabelGenerator(row,column,selected);
    if (generator != null && isItemLabelVisible(row,column,selected)) {
      drawItemLabelForBar(g2,plot,dataset,row,column,selected,generator,bar,(value < 0.0));
    }
    EntityCollection entities=state.getEntityCollection();
    if (entities != null) {
      addEntity(entities,bar,dataset,row,column,selected);
    }
  }
  /** 
 * Draws an item for a plot with a vertical orientation.
 * @param g2  the graphics device.
 * @param state  the renderer state.
 * @param dataArea  the data area.
 * @param plot  the plot.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param dataset  the data.
 * @param visibleRow  the visible row index.
 * @param row  the row index (zero-based).
 * @param column  the column index (zero-based).
 * @param selected  is the item selected?
 * @since 1.2.0
 */
  protected void drawVerticalItem(  Graphics2D g2,  CategoryItemRendererState state,  Rectangle2D dataArea,  CategoryPlot plot,  CategoryAxis domainAxis,  ValueAxis rangeAxis,  StatisticalCategoryDataset dataset,  int visibleRow,  int row,  int column,  boolean selected){
    RectangleEdge xAxisLocation=plot.getDomainAxisEdge();
    double rectX=domainAxis.getCategoryStart(column,getColumnCount(),dataArea,xAxisLocation);
    int seriesCount=state.getVisibleSeriesCount() >= 0 ? state.getVisibleSeriesCount() : getRowCount();
    int categoryCount=getColumnCount();
    if (seriesCount > 1) {
      double seriesGap=dataArea.getWidth() * getItemMargin() / (categoryCount * (seriesCount - 1));
      rectX=rectX + visibleRow * (state.getBarWidth() + seriesGap);
    }
 else {
      rectX=rectX + visibleRow * state.getBarWidth();
    }
    Number meanValue=dataset.getMeanValue(row,column);
    if (meanValue == null) {
      return;
    }
    double value=meanValue.doubleValue();
    double base=0.0;
    double lclip=rangeAxis.getLowerBound();
    double uclip=rangeAxis.getUpperBound();
    if (uclip <= 0.0) {
      if (value >= uclip) {
        return;
      }
      base=uclip;
      if (value <= lclip) {
        value=lclip;
      }
    }
 else {
      if (lclip <= 0.0) {
        if (value >= uclip) {
          value=uclip;
        }
 else {
          if (value <= lclip) {
            value=lclip;
          }
        }
      }
 else {
        if (value <= lclip) {
          return;
        }
        base=rangeAxis.getLowerBound();
        if (value >= uclip) {
          value=uclip;
        }
      }
    }
    RectangleEdge yAxisLocation=plot.getRangeAxisEdge();
    double transY1=rangeAxis.valueToJava2D(base,dataArea,yAxisLocation);
    double transY2=rangeAxis.valueToJava2D(value,dataArea,yAxisLocation);
    double rectY=Math.min(transY2,transY1);
    double rectWidth=state.getBarWidth();
    double rectHeight=Math.abs(transY2 - transY1);
    Rectangle2D bar=new Rectangle2D.Double(rectX,rectY,rectWidth,rectHeight);
    Paint itemPaint=getItemPaint(row,column,selected);
    GradientPaintTransformer t=getGradientPaintTransformer();
    if (t != null && itemPaint instanceof GradientPaint) {
      itemPaint=t.transform((GradientPaint)itemPaint,bar);
    }
    g2.setPaint(itemPaint);
    g2.fill(bar);
    if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
      Stroke stroke=getItemOutlineStroke(row,column,selected);
      Paint paint=getItemOutlinePaint(row,column,selected);
      if (stroke != null && paint != null) {
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(bar);
      }
    }
    Number n=dataset.getStdDevValue(row,column);
    if (n != null) {
      double valueDelta=n.doubleValue();
      double highVal=rangeAxis.valueToJava2D(meanValue.doubleValue() + valueDelta,dataArea,yAxisLocation);
      double lowVal=rangeAxis.valueToJava2D(meanValue.doubleValue() - valueDelta,dataArea,yAxisLocation);
      if (this.errorIndicatorPaint != null) {
        g2.setPaint(this.errorIndicatorPaint);
      }
 else {
        g2.setPaint(getItemOutlinePaint(row,column,selected));
      }
      if (this.errorIndicatorStroke != null) {
        g2.setStroke(this.errorIndicatorStroke);
      }
 else {
        g2.setStroke(getItemOutlineStroke(row,column,selected));
      }
      Line2D line=null;
      line=new Line2D.Double(rectX + rectWidth / 2.0d,lowVal,rectX + rectWidth / 2.0d,highVal);
      g2.draw(line);
      line=new Line2D.Double(rectX + rectWidth / 2.0d - 5.0d,highVal,rectX + rectWidth / 2.0d + 5.0d,highVal);
      g2.draw(line);
      line=new Line2D.Double(rectX + rectWidth / 2.0d - 5.0d,lowVal,rectX + rectWidth / 2.0d + 5.0d,lowVal);
      g2.draw(line);
    }
    CategoryItemLabelGenerator generator=getItemLabelGenerator(row,column,selected);
    if (generator != null && isItemLabelVisible(row,column,selected)) {
      drawItemLabelForBar(g2,plot,dataset,row,column,selected,generator,bar,(value < 0.0));
    }
    EntityCollection entities=state.getEntityCollection();
    if (entities != null) {
      addEntity(entities,bar,dataset,row,column,selected);
    }
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
    if (!(obj instanceof StatisticalBarRenderer)) {
      return false;
    }
    StatisticalBarRenderer that=(StatisticalBarRenderer)obj;
    if (!PaintUtilities.equal(this.errorIndicatorPaint,that.errorIndicatorPaint)) {
      return false;
    }
    if (!ObjectUtilities.equal(this.errorIndicatorStroke,that.errorIndicatorStroke)) {
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
    SerialUtilities.writePaint(this.errorIndicatorPaint,stream);
    SerialUtilities.writeStroke(this.errorIndicatorStroke,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.errorIndicatorPaint=SerialUtilities.readPaint(stream);
    this.errorIndicatorStroke=SerialUtilities.readStroke(stream);
  }
}
