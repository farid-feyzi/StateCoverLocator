package org.jfree.chart.annotations;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockParams;
import org.jfree.chart.block.EntityBlockResult;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleAnchor;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.jfree.chart.util.XYCoordinateType;
/** 
 * An annotation that allows any      {@link Title} to be placed at a location onan  {@link XYPlot}.
 * @since 1.0.11
 */
public class XYTitleAnnotation extends AbstractXYAnnotation implements Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-4364694501921559958L;
  /** 
 * The coordinate type. 
 */
  private XYCoordinateType coordinateType;
  /** 
 * The x-coordinate (in data space). 
 */
  private double x;
  /** 
 * The y-coordinate (in data space). 
 */
  private double y;
  /** 
 * The maximum width. 
 */
  private double maxWidth;
  /** 
 * The maximum height. 
 */
  private double maxHeight;
  /** 
 * The title. 
 */
  private Title title;
  /** 
 * The title anchor point.
 */
  private RectangleAnchor anchor;
  /** 
 * Creates a new annotation to be displayed at the specified (x, y) location.
 * @param x  the x-coordinate (in data space).
 * @param y  the y-coordinate (in data space).
 * @param title  the title (<code>null</code> not permitted).
 */
  public XYTitleAnnotation(  double x,  double y,  Title title){
    this(x,y,title,RectangleAnchor.CENTER);
  }
  /** 
 * Creates a new annotation to be displayed at the specified (x, y) location.
 * @param x  the x-coordinate (in data space).
 * @param y  the y-coordinate (in data space).
 * @param title  the title (<code>null</code> not permitted).
 * @param anchor  the title anchor (<code>null</code> not permitted).
 */
  public XYTitleAnnotation(  double x,  double y,  Title title,  RectangleAnchor anchor){
    super();
    if (title == null) {
      throw new IllegalArgumentException("Null 'title' argument.");
    }
    if (anchor == null) {
      throw new IllegalArgumentException("Null 'anchor' argument.");
    }
    this.coordinateType=XYCoordinateType.RELATIVE;
    this.x=x;
    this.y=y;
    this.maxWidth=0.0;
    this.maxHeight=0.0;
    this.title=title;
    this.anchor=anchor;
  }
  /** 
 * Returns the coordinate type (set in the constructor).
 * @return The coordinate type (never <code>null</code>).
 */
  public XYCoordinateType getCoordinateType(){
    return this.coordinateType;
  }
  /** 
 * Returns the x-coordinate for the annotation.
 * @return The x-coordinate.
 */
  public double getX(){
    return this.x;
  }
  /** 
 * Returns the y-coordinate for the annotation.
 * @return The y-coordinate.
 */
  public double getY(){
    return this.y;
  }
  /** 
 * Returns the title for the annotation.
 * @return The title.
 */
  public Title getTitle(){
    return this.title;
  }
  /** 
 * Returns the title anchor for the annotation.
 * @return The title anchor.
 */
  public RectangleAnchor getTitleAnchor(){
    return this.anchor;
  }
  /** 
 * Returns the maximum width.
 * @return The maximum width.
 */
  public double getMaxWidth(){
    return this.maxWidth;
  }
  /** 
 * Sets the maximum width and sends an     {@link AnnotationChangeEvent} to all registered listeners.
 * @param max  the maximum width (0.0 or less means no maximum).
 */
  public void setMaxWidth(  double max){
    this.maxWidth=max;
    fireAnnotationChanged();
  }
  /** 
 * Returns the maximum height.
 * @return The maximum height.
 */
  public double getMaxHeight(){
    return this.maxHeight;
  }
  /** 
 * Sets the maximum height and sends an     {@link AnnotationChangeEvent} to all registered listeners.
 * @param max  the maximum height.
 */
  public void setMaxHeight(  double max){
    this.maxHeight=max;
    fireAnnotationChanged();
  }
  /** 
 * Draws the annotation.  This method is called by the drawing code in the     {@link XYPlot} class, you don't normally need to call this methoddirectly.
 * @param g2  the graphics device.
 * @param plot  the plot.
 * @param dataArea  the data area.
 * @param domainAxis  the domain axis.
 * @param rangeAxis  the range axis.
 * @param rendererIndex  the renderer index.
 * @param info  if supplied, this info object will be populated withentity information.
 */
  public void draw(  Graphics2D g2,  XYPlot plot,  Rectangle2D dataArea,  ValueAxis domainAxis,  ValueAxis rangeAxis,  int rendererIndex,  PlotRenderingInfo info){
    PlotOrientation orientation=plot.getOrientation();
    AxisLocation domainAxisLocation=plot.getDomainAxisLocation();
    AxisLocation rangeAxisLocation=plot.getRangeAxisLocation();
    RectangleEdge domainEdge=Plot.resolveDomainAxisLocation(domainAxisLocation,orientation);
    RectangleEdge rangeEdge=Plot.resolveRangeAxisLocation(rangeAxisLocation,orientation);
    Range xRange=domainAxis.getRange();
    Range yRange=rangeAxis.getRange();
    double anchorX=0.0;
    double anchorY=0.0;
    if (this.coordinateType == XYCoordinateType.RELATIVE) {
      anchorX=xRange.getLowerBound() + (this.x * xRange.getLength());
      anchorY=yRange.getLowerBound() + (this.y * yRange.getLength());
    }
 else {
      anchorX=domainAxis.valueToJava2D(this.x,dataArea,domainEdge);
      anchorY=rangeAxis.valueToJava2D(this.y,dataArea,rangeEdge);
    }
    float j2DX=(float)domainAxis.valueToJava2D(anchorX,dataArea,domainEdge);
    float j2DY=(float)rangeAxis.valueToJava2D(anchorY,dataArea,rangeEdge);
    float xx=0.0f;
    float yy=0.0f;
    if (orientation == PlotOrientation.HORIZONTAL) {
      xx=j2DY;
      yy=j2DX;
    }
 else {
      if (orientation == PlotOrientation.VERTICAL) {
        xx=j2DX;
        yy=j2DY;
      }
    }
    double maxW=dataArea.getWidth();
    double maxH=dataArea.getHeight();
    if (this.coordinateType == XYCoordinateType.RELATIVE) {
      if (this.maxWidth > 0.0) {
        maxW=maxW * this.maxWidth;
      }
      if (this.maxHeight > 0.0) {
        maxH=maxH * this.maxHeight;
      }
    }
    if (this.coordinateType == XYCoordinateType.DATA) {
      maxW=this.maxWidth;
      maxH=this.maxHeight;
    }
    RectangleConstraint rc=new RectangleConstraint(new Range(0,maxW),new Range(0,maxH));
    Size2D size=this.title.arrange(g2,rc);
    Rectangle2D titleRect=new Rectangle2D.Double(0,0,size.width,size.height);
    Point2D anchorPoint=RectangleAnchor.coordinates(titleRect,this.anchor);
    xx=xx - (float)anchorPoint.getX();
    yy=yy - (float)anchorPoint.getY();
    titleRect.setRect(xx,yy,titleRect.getWidth(),titleRect.getHeight());
    BlockParams p=new BlockParams();
    if (info != null) {
      if (info.getOwner().getEntityCollection() != null) {
        p.setGenerateEntities(true);
      }
    }
    Object result=this.title.draw(g2,titleRect,p);
    if (info != null) {
      if (result instanceof EntityBlockResult) {
        EntityBlockResult ebr=(EntityBlockResult)result;
        info.getOwner().getEntityCollection().addAll(ebr.getEntityCollection());
      }
      String toolTip=getToolTipText();
      String url=getURL();
      if (toolTip != null || url != null) {
        addEntity(info,new Rectangle2D.Float(xx,yy,(float)size.width,(float)size.height),rendererIndex,toolTip,url);
      }
    }
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof XYTitleAnnotation)) {
      return false;
    }
    XYTitleAnnotation that=(XYTitleAnnotation)obj;
    if (this.coordinateType != that.coordinateType) {
      return false;
    }
    if (this.x != that.x) {
      return false;
    }
    if (this.y != that.y) {
      return false;
    }
    if (this.maxWidth != that.maxWidth) {
      return false;
    }
    if (this.maxHeight != that.maxHeight) {
      return false;
    }
    if (!ObjectUtilities.equal(this.title,that.title)) {
      return false;
    }
    if (!this.anchor.equals(that.anchor)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a hash code for this object.
 * @return A hash code.
 */
  public int hashCode(){
    int result=193;
    result=HashUtilities.hashCode(result,this.anchor);
    result=HashUtilities.hashCode(result,this.coordinateType);
    result=HashUtilities.hashCode(result,this.x);
    result=HashUtilities.hashCode(result,this.y);
    result=HashUtilities.hashCode(result,this.maxWidth);
    result=HashUtilities.hashCode(result,this.maxHeight);
    result=HashUtilities.hashCode(result,this.title);
    return result;
  }
  /** 
 * Returns a clone of the annotation.
 * @return A clone.
 * @throws CloneNotSupportedException  if the annotation can't be cloned.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}