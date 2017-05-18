package org.jfree.chart;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
/** 
 * An interface for an object that can draw itself within an area on a Graphics2D.
 */
public interface Drawable {
  /** 
 * Draws the object.
 * @param g2  the graphics device.
 * @param area  the area inside which the object should be drawn.
 */
  public void draw(  Graphics2D g2,  Rectangle2D area);
}
