package org.jfree.chart.annotations;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PaintUtilities;
import org.jfree.chart.util.SerialUtilities;
/** 
 * A base class for text annotations.  This class records the content but not the location of the annotation.
 */
public class TextAnnotation extends AbstractAnnotation implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7008912287533127432L;
  /** 
 * The default font. 
 */
  public static final Font DEFAULT_FONT=new Font("Tahoma",Font.PLAIN,10);
  /** 
 * The default paint. 
 */
  public static final Paint DEFAULT_PAINT=Color.black;
  /** 
 * The default text anchor. 
 */
  public static final TextAnchor DEFAULT_TEXT_ANCHOR=TextAnchor.CENTER;
  /** 
 * The default rotation anchor. 
 */
  public static final TextAnchor DEFAULT_ROTATION_ANCHOR=TextAnchor.CENTER;
  /** 
 * The default rotation angle. 
 */
  public static final double DEFAULT_ROTATION_ANGLE=0.0;
  /** 
 * The text. 
 */
  private String text;
  /** 
 * The font. 
 */
  private Font font;
  /** 
 * The paint. 
 */
  private transient Paint paint;
  /** 
 * The text anchor. 
 */
  private TextAnchor textAnchor;
  /** 
 * The rotation anchor. 
 */
  private TextAnchor rotationAnchor;
  /** 
 * The rotation angle. 
 */
  private double rotationAngle;
  /** 
 * Creates a text annotation with default settings.
 * @param text  the text (<code>null</code> not permitted).
 */
  protected TextAnnotation(  String text){
    super();
    if (text == null) {
      throw new IllegalArgumentException("Null 'text' argument.");
    }
    this.text=text;
    this.font=DEFAULT_FONT;
    this.paint=DEFAULT_PAINT;
    this.textAnchor=DEFAULT_TEXT_ANCHOR;
    this.rotationAnchor=DEFAULT_ROTATION_ANCHOR;
    this.rotationAngle=DEFAULT_ROTATION_ANGLE;
  }
  /** 
 * Returns the text for the annotation.
 * @return The text (never <code>null</code>).
 * @see #setText(String)
 */
  public String getText(){
    return this.text;
  }
  /** 
 * Sets the text for the annotation.
 * @param text  the text (<code>null</code> not permitted).
 * @see #getText()
 */
  public void setText(  String text){
    if (text == null) {
      throw new IllegalArgumentException("Null 'text' argument.");
    }
    this.text=text;
  }
  /** 
 * Returns the font for the annotation.
 * @return The font (never <code>null</code>).
 * @see #setFont(Font)
 */
  public Font getFont(){
    return this.font;
  }
  /** 
 * Sets the font for the annotation and sends an                                                                                                                                                               {@link AnnotationChangeEvent} to all registered listeners.
 * @param font  the font (<code>null</code> not permitted).
 * @see #getFont()
 */
  public void setFont(  Font font){
    if (font == null) {
      throw new IllegalArgumentException("Null 'font' argument.");
    }
    this.font=font;
    fireAnnotationChanged();
  }
  /** 
 * Returns the paint for the annotation.
 * @return The paint (never <code>null</code>).
 * @see #setPaint(Paint)
 */
  public Paint getPaint(){
    return this.paint;
  }
  /** 
 * Sets the paint for the annotation and sends an                                                                                                                                                               {@link AnnotationChangeEvent} to all registered listeners.
 * @param paint  the paint (<code>null</code> not permitted).
 * @see #getPaint()
 */
  public void setPaint(  Paint paint){
    if (paint == null) {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.paint=paint;
    fireAnnotationChanged();
  }
  /** 
 * Returns the text anchor.
 * @return The text anchor.
 * @see #setTextAnchor(TextAnchor)
 */
  public TextAnchor getTextAnchor(){
    return this.textAnchor;
  }
  /** 
 * Sets the text anchor (the point on the text bounding rectangle that is aligned to the (x, y) coordinate of the annotation) and sends an                                                                                                                                                               {@link AnnotationChangeEvent} to all registered listeners.
 * @param anchor  the anchor point (<code>null</code> not permitted).
 * @see #getTextAnchor()
 */
  public void setTextAnchor(  TextAnchor anchor){
    if (anchor == null) {
      throw new IllegalArgumentException("Null 'anchor' argument.");
    }
    this.textAnchor=anchor;
    fireAnnotationChanged();
  }
  /** 
 * Returns the rotation anchor.
 * @return The rotation anchor point (never <code>null</code>).
 * @see #setRotationAnchor(TextAnchor)
 */
  public TextAnchor getRotationAnchor(){
    return this.rotationAnchor;
  }
  /** 
 * Sets the rotation anchor point and sends an                                                                                                                                                               {@link AnnotationChangeEvent} to all registered listeners.
 * @param anchor  the anchor (<code>null</code> not permitted).
 * @see #getRotationAnchor()
 */
  public void setRotationAnchor(  TextAnchor anchor){
    this.rotationAnchor=anchor;
    fireAnnotationChanged();
  }
  /** 
 * Returns the rotation angle in radians.
 * @return The rotation angle.
 * @see #setRotationAngle(double)
 */
  public double getRotationAngle(){
    return this.rotationAngle;
  }
  /** 
 * Sets the rotation angle and sends an                                                                                                                                                                {@link AnnotationChangeEvent} toall registered listeners.  The angle is measured clockwise in radians.
 * @param angle  the angle (in radians).
 * @see #getRotationAngle()
 */
  public void setRotationAngle(  double angle){
    this.rotationAngle=angle;
    fireAnnotationChanged();
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return <code>true</code> or <code>false</code>.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof TextAnnotation)) {
      return false;
    }
    TextAnnotation that=(TextAnnotation)obj;
    if (!ObjectUtilities.equal(this.text,that.getText())) {
      return false;
    }
    if (!ObjectUtilities.equal(this.font,that.getFont())) {
      return false;
    }
    if (!PaintUtilities.equal(this.paint,that.getPaint())) {
      return false;
    }
    if (!ObjectUtilities.equal(this.textAnchor,that.getTextAnchor())) {
      return false;
    }
    if (!ObjectUtilities.equal(this.rotationAnchor,that.getRotationAnchor())) {
      return false;
    }
    if (this.rotationAngle != that.getRotationAngle()) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=193;
    result=37 * result + this.font.hashCode();
    result=37 * result + HashUtilities.hashCodeForPaint(this.paint);
    result=37 * result + this.rotationAnchor.hashCode();
    long temp=Double.doubleToLongBits(this.rotationAngle);
    result=37 * result + (int)(temp ^ (temp >>> 32));
    result=37 * result + this.text.hashCode();
    result=37 * result + this.textAnchor.hashCode();
    return result;
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    SerialUtilities.writePaint(this.paint,stream);
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.paint=SerialUtilities.readPaint(stream);
  }
}
