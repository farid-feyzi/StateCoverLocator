package org.jfree.chart.imagemap;
import java.io.IOException;
import java.io.PrintWriter;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.util.StringUtilities;
/** 
 * Collection of utility methods related to producing image maps. Functionality was originally in                                                                                                                                                                {@link org.jfree.chart.ChartUtilities}.
 */
public class ImageMapUtilities {
  /** 
 * Writes an image map to an output stream.
 * @param writer  the writer (<code>null</code> not permitted).
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @throws java.io.IOException if there are any I/O errors.
 */
  public static void writeImageMap(  PrintWriter writer,  String name,  ChartRenderingInfo info) throws IOException {
    ImageMapUtilities.writeImageMap(writer,name,info,new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
  }
  /** 
 * Writes an image map to an output stream.
 * @param writer  the writer (<code>null</code> not permitted).
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param useOverLibForToolTips  whether to use OverLIB for tooltips(http://www.bosrup.com/web/overlib/).
 * @throws java.io.IOException if there are any I/O errors.
 */
  public static void writeImageMap(  PrintWriter writer,  String name,  ChartRenderingInfo info,  boolean useOverLibForToolTips) throws IOException {
    ToolTipTagFragmentGenerator toolTipTagFragmentGenerator=null;
    if (useOverLibForToolTips) {
      toolTipTagFragmentGenerator=new OverLIBToolTipTagFragmentGenerator();
    }
 else {
      toolTipTagFragmentGenerator=new StandardToolTipTagFragmentGenerator();
    }
    ImageMapUtilities.writeImageMap(writer,name,info,toolTipTagFragmentGenerator,new StandardURLTagFragmentGenerator());
  }
  /** 
 * Writes an image map to an output stream.
 * @param writer  the writer (<code>null</code> not permitted).
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param toolTipTagFragmentGenerator  a generator for the HTML fragmentthat will contain the tooltip text (<code>null</code> not permitted if <code>info</code> contains tooltip information).
 * @param urlTagFragmentGenerator  a generator for the HTML fragment thatwill contain the URL reference (<code>null</code> not permitted if <code>info</code> contains URLs).
 * @throws java.io.IOException if there are any I/O errors.
 */
  public static void writeImageMap(  PrintWriter writer,  String name,  ChartRenderingInfo info,  ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,  URLTagFragmentGenerator urlTagFragmentGenerator) throws IOException {
    writer.println(ImageMapUtilities.getImageMap(name,info,toolTipTagFragmentGenerator,urlTagFragmentGenerator));
  }
  /** 
 * Creates an image map element that complies with the XHTML 1.0 specification.
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @return The map element.
 */
  public static String getImageMap(  String name,  ChartRenderingInfo info){
    return ImageMapUtilities.getImageMap(name,info,new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
  }
  /** 
 * Creates an image map element that complies with the XHTML 1.0 specification.
 * @param name  the map name (<code>null</code> not permitted).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param toolTipTagFragmentGenerator  a generator for the HTML fragmentthat will contain the tooltip text (<code>null</code> not permitted if <code>info</code> contains tooltip information).
 * @param urlTagFragmentGenerator  a generator for the HTML fragment thatwill contain the URL reference (<code>null</code> not permitted if <code>info</code> contains URLs).
 * @return The map tag.
 */
  public static String getImageMap(  String name,  ChartRenderingInfo info,  ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,  URLTagFragmentGenerator urlTagFragmentGenerator){
    StringBuffer sb=new StringBuffer();
    sb.append("<map id=\"" + htmlEscape(name) + "\" name=\""+ htmlEscape(name)+ "\">");
    sb.append(StringUtilities.getLineSeparator());
    EntityCollection entities=info.getEntityCollection();
    if (entities != null) {
      int count=entities.getEntityCount();
      for (int i=count - 1; i >= 0; i--) {
        ChartEntity entity=entities.getEntity(i);
        if (entity.getToolTipText() != null || entity.getURLText() != null) {
          String area=entity.getImageMapAreaTag(toolTipTagFragmentGenerator,urlTagFragmentGenerator);
          if (area.length() > 0) {
            sb.append(area);
            sb.append(StringUtilities.getLineSeparator());
          }
        }
      }
    }
    sb.append("</map>");
    return sb.toString();
  }
  /** 
 * Returns a string that is equivalent to the input string, but with special characters converted to HTML escape sequences.
 * @param input  the string to escape (<code>null</code> not permitted).
 * @return A string with characters escaped.
 * @since 1.0.9
 */
  public static String htmlEscape(  String input){
    if (input == null) {
      throw new IllegalArgumentException("Null 'input' argument.");
    }
    StringBuffer result=new StringBuffer();
    int length=input.length();
    for (int i=0; i < length; i++) {
      char c=input.charAt(i);
      if (c == '&') {
        result.append("&amp;");
      }
 else {
        if (c == '\"') {
          result.append("&quot;");
        }
 else {
          if (c == '<') {
            result.append("&lt;");
          }
 else {
            if (c == '>') {
              result.append("&gt;");
            }
 else {
              if (c == '\'') {
                result.append("&#39;");
              }
 else {
                if (c == '\\') {
                  result.append("&#092;");
                }
 else {
                  result.append(c);
                }
              }
            }
          }
        }
      }
    }
    return result.toString();
  }
  /** 
 * Returns a string that is equivalent to the input string, but with special characters converted to JavaScript escape sequences.
 * @param input  the string to escape (<code>null</code> not permitted).
 * @return A string with characters escaped.
 * @since 1.0.13
 */
  public static String javascriptEscape(  String input){
    if (input == null) {
      throw new IllegalArgumentException("Null 'input' argument.");
    }
    StringBuffer result=new StringBuffer();
    int length=input.length();
    for (int i=0; i < length; i++) {
      char c=input.charAt(i);
      if (c == '\"') {
        result.append("\\\"");
      }
 else {
        if (c == '\'') {
          result.append("\\'");
        }
 else {
          if (c == '\\') {
            result.append("\\\\");
          }
 else {
            result.append(c);
          }
        }
      }
    }
    return result.toString();
  }
}
