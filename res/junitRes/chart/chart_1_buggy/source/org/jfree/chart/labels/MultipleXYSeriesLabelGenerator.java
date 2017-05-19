package org.jfree.chart.labels;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.XYDataset;
/** 
 * A series label generator for plots that use data from an                               {@link org.jfree.data.xy.XYDataset}.
 */
public class MultipleXYSeriesLabelGenerator implements XYSeriesLabelGenerator, Cloneable, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=138976236941898560L;
  /** 
 * The default item label format. 
 */
  public static final String DEFAULT_LABEL_FORMAT="{0}";
  /** 
 * The format pattern for the initial part of the label. 
 */
  private String formatPattern;
  /** 
 * The format pattern for additional labels. 
 */
  private String additionalFormatPattern;
  /** 
 * Storage for the additional series labels. 
 */
  private Map seriesLabelLists;
  /** 
 * Creates an item label generator using default number formatters.
 */
  public MultipleXYSeriesLabelGenerator(){
    this(DEFAULT_LABEL_FORMAT);
  }
  /** 
 * Creates a new series label generator.
 * @param format  the format pattern (<code>null</code> not permitted).
 */
  public MultipleXYSeriesLabelGenerator(  String format){
    if (format == null) {
      throw new IllegalArgumentException("Null 'format' argument.");
    }
    this.formatPattern=format;
    this.additionalFormatPattern="\n{0}";
    this.seriesLabelLists=new HashMap();
  }
  /** 
 * Adds an extra label for the specified series.
 * @param series  the series index.
 * @param label  the label.
 */
  public void addSeriesLabel(  int series,  String label){
    Integer key=new Integer(series);
    List labelList=(List)this.seriesLabelLists.get(key);
    if (labelList == null) {
      labelList=new java.util.ArrayList();
      this.seriesLabelLists.put(key,labelList);
    }
    labelList.add(label);
  }
  /** 
 * Clears the extra labels for the specified series.
 * @param series  the series index.
 */
  public void clearSeriesLabels(  int series){
    Integer key=new Integer(series);
    this.seriesLabelLists.put(key,null);
  }
  /** 
 * Generates a label for the specified series.  This label will be used for the chart legend.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series.
 * @return A series label.
 */
  public String generateLabel(  XYDataset dataset,  int series){
    if (dataset == null) {
      throw new IllegalArgumentException("Null 'dataset' argument.");
    }
    StringBuffer label=new StringBuffer();
    label.append(MessageFormat.format(this.formatPattern,createItemArray(dataset,series)));
    Integer key=new Integer(series);
    List extraLabels=(List)this.seriesLabelLists.get(key);
    if (extraLabels != null) {
      Object[] temp=new Object[1];
      for (int i=0; i < extraLabels.size(); i++) {
        temp[0]=extraLabels.get(i);
        String labelAddition=MessageFormat.format(this.additionalFormatPattern,temp);
        label.append(labelAddition);
      }
    }
    return label.toString();
  }
  /** 
 * Creates the array of items that can be passed to the                              {@link MessageFormat} class for creating labels.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param series  the series (zero-based index).
 * @return The items (never <code>null</code>).
 */
  protected Object[] createItemArray(  XYDataset dataset,  int series){
    Object[] result=new Object[1];
    result[0]=dataset.getSeriesKey(series).toString();
    return result;
  }
  /** 
 * Returns an independent copy of the generator.
 * @return A clone.
 * @throws CloneNotSupportedException if cloning is not supported.
 */
  public Object clone() throws CloneNotSupportedException {
    MultipleXYSeriesLabelGenerator clone=(MultipleXYSeriesLabelGenerator)super.clone();
    clone.seriesLabelLists=new HashMap();
    Set keys=this.seriesLabelLists.keySet();
    Iterator iterator=keys.iterator();
    while (iterator.hasNext()) {
      Object key=iterator.next();
      Object entry=this.seriesLabelLists.get(key);
      Object toAdd=entry;
      if (entry instanceof PublicCloneable) {
        PublicCloneable pc=(PublicCloneable)entry;
        toAdd=pc.clone();
      }
      clone.seriesLabelLists.put(key,toAdd);
    }
    return clone;
  }
  /** 
 * Tests this object for equality with an arbitrary object.
 * @param obj  the other object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MultipleXYSeriesLabelGenerator)) {
      return false;
    }
    MultipleXYSeriesLabelGenerator that=(MultipleXYSeriesLabelGenerator)obj;
    if (!this.formatPattern.equals(that.formatPattern)) {
      return false;
    }
    if (!this.additionalFormatPattern.equals(that.additionalFormatPattern)) {
      return false;
    }
    if (!this.seriesLabelLists.equals(that.seriesLabelLists)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    int result=127;
    result=HashUtilities.hashCode(result,this.formatPattern);
    result=HashUtilities.hashCode(result,this.additionalFormatPattern);
    result=HashUtilities.hashCode(result,this.seriesLabelLists);
    return result;
  }
}
