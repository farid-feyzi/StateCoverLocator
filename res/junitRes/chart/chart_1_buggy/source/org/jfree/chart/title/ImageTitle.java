package org.jfree.chart.title;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.chart.util.HorizontalAlignment;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.chart.util.Size2D;
import org.jfree.chart.util.VerticalAlignment;
/** 
 * A chart title that displays an image.  This is useful, for example, if you have an image of your corporate logo and want to use as a footnote or part of a title in a chart you create. <P> ImageTitle needs an image passed to it in the constructor.  For ImageTitle to work, you must have already loaded this image from its source (disk or URL).  It is recommended you use something like Toolkit.getDefaultToolkit().getImage() to get the image.  Then, use MediaTracker or some other message to make sure the image is fully loaded from disk. <P> SPECIAL NOTE:  this class fails to serialize, so if you are relying on your charts to be serializable, please avoid using this class.
 */
public class ImageTitle extends Title {
  /** 
 * The title image. 
 */
  private Image image;
  /** 
 * Creates a new image title.
 * @param image  the image (<code>null</code> not permitted).
 */
  public ImageTitle(  Image image){
    this(image,image.getHeight(null),image.getWidth(null),Title.DEFAULT_POSITION,Title.DEFAULT_HORIZONTAL_ALIGNMENT,Title.DEFAULT_VERTICAL_ALIGNMENT,Title.DEFAULT_PADDING);
  }
  /** 
 * Creates a new image title.
 * @param image  the image (<code>null</code> not permitted).
 * @param position  the title position.
 * @param horizontalAlignment  the horizontal alignment.
 * @param verticalAlignment  the vertical alignment.
 */
  public ImageTitle(  Image image,  RectangleEdge position,  HorizontalAlignment horizontalAlignment,  VerticalAlignment verticalAlignment){
    this(image,image.getHeight(null),image.getWidth(null),position,horizontalAlignment,verticalAlignment,Title.DEFAULT_PADDING);
  }
  /** 
 * Creates a new image title with the given image scaled to the given width and height in the given location.
 * @param image  the image (<code>null</code> not permitted).
 * @param height  the height used to draw the image.
 * @param width  the width used to draw the image.
 * @param position  the title position.
 * @param horizontalAlignment  the horizontal alignment.
 * @param verticalAlignment  the vertical alignment.
 * @param padding  the amount of space to leave around the outside of thetitle.
 */
  public ImageTitle(  Image image,  int height,  int width,  RectangleEdge position,  HorizontalAlignment horizontalAlignment,  VerticalAlignment verticalAlignment,  RectangleInsets padding){
    super(position,horizontalAlignment,verticalAlignment,padding);
    if (image == null) {
      throw new NullPointerException("Null 'image' argument.");
    }
    this.image=image;
    setHeight(height);
    setWidth(width);
  }
  /** 
 * Returns the image for the title.
 * @return The image for the title (never <code>null</code>).
 */
  public Image getImage(){
    return this.image;
  }
  /** 
 * Sets the image for the title and notifies registered listeners that the title has been modified.
 * @param image  the new image (<code>null</code> not permitted).
 */
  public void setImage(  Image image){
    if (image == null) {
      throw new NullPointerException("Null 'image' argument.");
    }
    this.image=image;
    notifyListeners(new TitleChangeEvent(this));
  }
  /** 
 * Arranges the contents of the block, within the given constraints, and returns the block size.
 * @param g2  the graphics device.
 * @param constraint  the constraint (<code>null</code> not permitted).
 * @return The block size (in Java2D units, never <code>null</code>).
 */
  public Size2D arrange(  Graphics2D g2,  RectangleConstraint constraint){
    Size2D s=new Size2D(this.image.getWidth(null),this.image.getHeight(null));
    return new Size2D(calculateTotalWidth(s.getWidth()),calculateTotalHeight(s.getHeight()));
  }
  /** 
 * Draws the title on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device.
 * @param area  the area allocated for the title.
 */
  public void draw(  Graphics2D g2,  Rectangle2D area){
    RectangleEdge position=getPosition();
    if (position == RectangleEdge.TOP || position == RectangleEdge.BOTTOM) {
      drawHorizontal(g2,area);
    }
 else {
      if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
        drawVertical(g2,area);
      }
 else {
        throw new RuntimeException("Invalid title position.");
      }
    }
  }
  /** 
 * Draws the title on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device.
 * @param chartArea  the area within which the title (and plot) should bedrawn.
 * @return The size of the area used by the title.
 */
  protected Size2D drawHorizontal(  Graphics2D g2,  Rectangle2D chartArea){
    double startY=0.0;
    double topSpace=0.0;
    double bottomSpace=0.0;
    double leftSpace=0.0;
    double rightSpace=0.0;
    double w=getWidth();
    double h=getHeight();
    RectangleInsets padding=getPadding();
    topSpace=padding.calculateTopOutset(h);
    bottomSpace=padding.calculateBottomOutset(h);
    leftSpace=padding.calculateLeftOutset(w);
    rightSpace=padding.calculateRightOutset(w);
    if (getPosition() == RectangleEdge.TOP) {
      startY=chartArea.getY() + topSpace;
    }
 else {
      startY=chartArea.getY() + chartArea.getHeight() - bottomSpace - h;
    }
    HorizontalAlignment horizontalAlignment=getHorizontalAlignment();
    double startX=0.0;
    if (horizontalAlignment == HorizontalAlignment.CENTER) {
      startX=chartArea.getX() + leftSpace + chartArea.getWidth() / 2.0 - w / 2.0;
    }
 else {
      if (horizontalAlignment == HorizontalAlignment.LEFT) {
        startX=chartArea.getX() + leftSpace;
      }
 else {
        if (horizontalAlignment == HorizontalAlignment.RIGHT) {
          startX=chartArea.getX() + chartArea.getWidth() - rightSpace - w;
        }
      }
    }
    g2.drawImage(this.image,(int)startX,(int)startY,(int)w,(int)h,null);
    return new Size2D(chartArea.getWidth() + leftSpace + rightSpace,h + topSpace + bottomSpace);
  }
  /** 
 * Draws the title on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device.
 * @param chartArea  the area within which the title (and plot) should bedrawn.
 * @return The size of the area used by the title.
 */
  protected Size2D drawVertical(  Graphics2D g2,  Rectangle2D chartArea){
    double startX=0.0;
    double topSpace=0.0;
    double bottomSpace=0.0;
    double leftSpace=0.0;
    double rightSpace=0.0;
    double w=getWidth();
    double h=getHeight();
    RectangleInsets padding=getPadding();
    if (padding != null) {
      topSpace=padding.calculateTopOutset(h);
      bottomSpace=padding.calculateBottomOutset(h);
      leftSpace=padding.calculateLeftOutset(w);
      rightSpace=padding.calculateRightOutset(w);
    }
    if (getPosition() == RectangleEdge.LEFT) {
      startX=chartArea.getX() + leftSpace;
    }
 else {
      startX=chartArea.getMaxX() - rightSpace - w;
    }
    VerticalAlignment alignment=getVerticalAlignment();
    double startY=0.0;
    if (alignment == VerticalAlignment.CENTER) {
      startY=chartArea.getMinY() + topSpace + chartArea.getHeight() / 2.0 - h / 2.0;
    }
 else {
      if (alignment == VerticalAlignment.TOP) {
        startY=chartArea.getMinY() + topSpace;
      }
 else {
        if (alignment == VerticalAlignment.BOTTOM) {
          startY=chartArea.getMaxY() - bottomSpace - h;
        }
      }
    }
    g2.drawImage(this.image,(int)startX,(int)startY,(int)w,(int)h,null);
    return new Size2D(chartArea.getWidth() + leftSpace + rightSpace,h + topSpace + bottomSpace);
  }
  /** 
 * Draws the block within the specified area.
 * @param g2  the graphics device.
 * @param area  the area.
 * @param params  ignored (<code>null</code> permitted).
 * @return Always <code>null</code>.
 */
  public Object draw(  Graphics2D g2,  Rectangle2D area,  Object params){
    draw(g2,area);
    return null;
  }
  /** 
 * Tests this <code>ImageTitle</code> for equality with an arbitrary object.  Returns <code>true</code> if: <ul> <li><code>obj</code> is an instance of <code>ImageTitle</code>; <li><code>obj</code> references the same image as this <code>ImageTitle</code>; <li><code>super.equals(obj)<code> returns <code>true</code>; </ul>
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ImageTitle)) {
      return false;
    }
    ImageTitle that=(ImageTitle)obj;
    if (!ObjectUtilities.equal(this.image,that.image)) {
      return false;
    }
    return super.equals(obj);
  }
}
