package org.jfree.chart.renderer.xy;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.Effect3D;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.SerialUtilities;
/** 
 * A XYLineAndShapeRenderer that adds a shadow line to the graph to emulate a 3D-effect.
 */
public class XYLine3DRenderer extends XYLineAndShapeRenderer implements Effect3D, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=588933208243446087L;
  /** 
 * The default x-offset for the 3D effect. 
 */
  public static final double DEFAULT_X_OFFSET=12.0;
  /** 
 * The default y-offset for the 3D effect. 
 */
  public static final double DEFAULT_Y_OFFSET=8.0;
  /** 
 * The default wall paint. 
 */
  public static final Paint DEFAULT_WALL_PAINT=new Color(0xDD,0xDD,0xDD);
  /** 
 * The size of x-offset for the 3D effect. 
 */
  private double xOffset;
  /** 
 * The size of y-offset for the 3D effect. 
 */
  private double yOffset;
  /** 
 * The paint used to shade the left and lower 3D wall. 
 */
  private transient Paint wallPaint;
  /** 
 * Creates a new renderer.
 */
  public XYLine3DRenderer(){
    this.wallPaint=DEFAULT_WALL_PAINT;
    this.xOffset=DEFAULT_X_OFFSET;
    this.yOffset=DEFAULT_Y_OFFSET;
  }
  /** 
 * Returns the x-offset for the 3D effect.
 * @return The 3D effect.
 */
  public double getXOffset(){
    return this.xOffset;
  }
  /** 
 * Returns the y-offset for the 3D effect.
 * @return The 3D effect.
 */
  public double getYOffset(){
    return this.yOffset;
  }
  /** 
 * Sets the x-offset and sends a                                                                                               {@link RendererChangeEvent} to allregistered listeners.
 * @param xOffset  the x-offset.
 */
  public void setXOffset(  double xOffset){
    this.xOffset=xOffset;
    fireChangeEvent();
  }
  /** 
 * Sets the y-offset and sends a                                                                                               {@link RendererChangeEvent} to allregistered listeners.
 * @param yOffset  the y-offset.
 */
  public void setYOffset(  double yOffset){
    this.yOffset=yOffset;
    fireChangeEvent();
  }
  /** 
 * Returns the paint used to highlight the left and bottom wall in the plot background.
 * @return The paint.
 */
  public Paint getWallPaint(){
    return this.wallPaint;
  }
  /** 
 * Sets the paint used to hightlight the left and bottom walls in the plot background and sends a                                                                                               {@link RendererChangeEvent} to all registeredlisteners.
 * @param paint  the paint.
 */
  public void setWallPaint(  Paint paint){
    this.wallPaint=paint;
    fireChangeEvent();
  }
  /** 
 * Returns the number of passes through the data that the renderer requires in order to draw the chart.  Most charts will require a single pass, but some require two passes.
 * @return The pass count.
 */
  public int getPassCount(){
    return 3;
  }
  /** 
 * Returns <code>true</code> if the specified pass involves drawing lines.
 * @param pass  the pass.
 * @return A boolean.
 */
  protected boolean isLinePass(  int pass){
    return pass == 0 || pass == 1;
  }
  /** 
 * Returns <code>true</code> if the specified pass involves drawing items.
 * @param pass  the pass.
 * @return A boolean.
 */
  protected boolean isItemPass(  int pass){
    return pass == 2;
  }
  /** 
 * Returns <code>true</code> if the specified pass involves drawing shadows.
 * @param pass  the pass.
 * @return A boolean.
 */
  protected boolean isShadowPass(  int pass){
    return pass == 0;
  }
  /** 
 * Overrides the method in the subclass to draw a shadow in the first pass.
 * @param g2  the graphics device.
 * @param pass  the pass.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @param selected  is the data item selected?
 * @param shape  the shape.
 */
  protected void drawShape1(  Graphics2D g2,  int pass,  int series,  int item,  boolean selected,  Shape shape){
    if (isShadowPass(pass)) {
      if (getWallPaint() != null) {
        g2.setStroke(getItemStroke(series,item,selected));
        g2.setPaint(getWallPaint());
        g2.translate(getXOffset(),getYOffset());
        g2.draw(shape);
        g2.translate(-getXOffset(),-getYOffset());
      }
    }
 else {
      super.drawShape1(g2,pass,series,item,selected,shape);
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
    if (!(obj instanceof XYLine3DRenderer)) {
      return false;
    }
    XYLine3DRenderer that=(XYLine3DRenderer)obj;
    if (this.xOffset != that.xOffset) {
      return false;
    }
    if (this.yOffset != that.yOffset) {
      return false;
    }
    if (!PaintUtilities.equal(this.wallPaint,that.wallPaint)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.wallPaint=SerialUtilities.readPaint(stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.wallPaint,stream);
  }
}
