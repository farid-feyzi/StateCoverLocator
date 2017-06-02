package org.jfree.data.xy;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.HashUtilities;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.DomainInfo;
import org.jfree.data.DomainOrder;
import org.jfree.data.Range;
import org.jfree.data.RangeInfo;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.event.DatasetChangeEvent;
/** 
 * Represents a collection of                                                                                                                                                              {@link XYSeries} objects that can be used as adataset.
 */
public class XYSeriesCollection extends AbstractIntervalXYDataset implements IntervalXYDataset, DomainInfo, RangeInfo, XYDatasetSelectionState, SelectableXYDataset, PublicCloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-7590013825931496766L;
  /** 
 * The series that are included in the collection. 
 */
  private List data;
  /** 
 * The interval delegate (used to calculate the start and end x-values). 
 */
  private IntervalXYDelegate intervalDelegate;
  /** 
 * Constructs an empty dataset.
 */
  public XYSeriesCollection(){
    this(null);
  }
  /** 
 * Constructs a dataset and populates it with a single series.
 * @param series  the series (<code>null</code> ignored).
 */
  public XYSeriesCollection(  XYSeries series){
    this.data=new java.util.ArrayList();
    this.intervalDelegate=new IntervalXYDelegate(this,false);
    addChangeListener(this.intervalDelegate);
    if (series != null) {
      this.data.add(series);
      series.addChangeListener(this);
    }
    setSelectionState(this);
  }
  /** 
 * Returns the order of the domain (X) values, if this is known.
 * @return The domain order.
 */
  public DomainOrder getDomainOrder(){
    int seriesCount=getSeriesCount();
    for (int i=0; i < seriesCount; i++) {
      XYSeries s=getSeries(i);
      if (!s.getAutoSort()) {
        return DomainOrder.NONE;
      }
    }
    return DomainOrder.ASCENDING;
  }
  /** 
 * Adds a series to the collection and sends a                                                                                                                                                              {@link DatasetChangeEvent}to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 */
  public void addSeries(  XYSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    this.data.add(series);
    series.addChangeListener(this);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a series from the collection and sends a                                                                                                                                                             {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series index (zero-based).
 */
  public void removeSeries(  int series){
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds.");
    }
    XYSeries ts=(XYSeries)this.data.get(series);
    ts.removeChangeListener(this);
    this.data.remove(series);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a series from the collection and sends a                                                                                                                                                             {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 */
  public void removeSeries(  XYSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    if (this.data.contains(series)) {
      series.removeChangeListener(this);
      this.data.remove(series);
      fireDatasetChanged(new DatasetChangeInfo());
    }
  }
  /** 
 * Removes all the series from the collection and sends a                                                                                                                                                             {@link DatasetChangeEvent} to all registered listeners.
 */
  public void removeAllSeries(){
    for (int i=0; i < this.data.size(); i++) {
      XYSeries series=(XYSeries)this.data.get(i);
      series.removeChangeListener(this);
    }
    this.data.clear();
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns the number of series in the collection.
 * @return The series count.
 */
  public int getSeriesCount(){
    return this.data.size();
  }
  /** 
 * Returns a list of all the series in the collection.
 * @return The list (which is unmodifiable).
 */
  public List getSeries(){
    return Collections.unmodifiableList(this.data);
  }
  /** 
 * Returns the index of the specified series, or -1 if that series is not present in the dataset.
 * @param series  the series (<code>null</code> not permitted).
 * @return The series index.
 * @since 1.0.6
 */
  public int indexOf(  XYSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    return this.data.indexOf(series);
  }
  /** 
 * Returns a series from the collection.
 * @param series  the series index (zero-based).
 * @return The series.
 * @throws IllegalArgumentException if <code>series</code> is not in therange <code>0</code> to <code>getSeriesCount() - 1</code>.
 */
  public XYSeries getSeries(  int series){
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("Series index out of bounds");
    }
    return (XYSeries)this.data.get(series);
  }
  /** 
 * Returns a series from the collection.
 * @param key  the key (<code>null</code> not permitted).
 * @return The series with the specified key.
 * @throws UnknownKeyException if <code>key</code> is not found in thecollection.
 * @since 1.0.9
 */
  public XYSeries getSeries(  Comparable key){
    if (key == null) {
      throw new IllegalArgumentException("Null 'key' argument.");
    }
    Iterator iterator=this.data.iterator();
    while (iterator.hasNext()) {
      XYSeries series=(XYSeries)iterator.next();
      if (key.equals(series.getKey())) {
        return series;
      }
    }
    throw new UnknownKeyException("Key not found: " + key);
  }
  /** 
 * Returns the key for a series.
 * @param series  the series index (in the range <code>0</code> to<code>getSeriesCount() - 1</code>).
 * @return The key for a series.
 * @throws IllegalArgumentException if <code>series</code> is not in thespecified range.
 */
  public Comparable getSeriesKey(  int series){
    return getSeries(series).getKey();
  }
  /** 
 * Returns the number of items in the specified series.
 * @param series  the series (zero-based index).
 * @return The item count.
 * @throws IllegalArgumentException if <code>series</code> is not in therange <code>0</code> to <code>getSeriesCount() - 1</code>.
 */
  public int getItemCount(  int series){
    return getSeries(series).getItemCount();
  }
  /** 
 * Returns the x-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getX(  int series,  int item){
    XYSeries s=(XYSeries)this.data.get(series);
    return s.getX(item);
  }
  /** 
 * Returns the starting X value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The starting X value.
 */
  public Number getStartX(  int series,  int item){
    return this.intervalDelegate.getStartX(series,item);
  }
  /** 
 * Returns the ending X value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The ending X value.
 */
  public Number getEndX(  int series,  int item){
    return this.intervalDelegate.getEndX(series,item);
  }
  /** 
 * Returns the y-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param index  the index of the item of interest (zero-based).
 * @return The value (possibly <code>null</code>).
 */
  public Number getY(  int series,  int index){
    XYSeries s=(XYSeries)this.data.get(series);
    return s.getY(index);
  }
  /** 
 * Returns the starting Y value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The starting Y value.
 */
  public Number getStartY(  int series,  int item){
    return getY(series,item);
  }
  /** 
 * Returns the ending Y value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The ending Y value.
 */
  public Number getEndY(  int series,  int item){
    return getY(series,item);
  }
  /** 
 * Returns the selection state for the specified data item.
 * @param series  the series index.
 * @param item  the item index.
 * @return <code>true</code> if the item is selected, and<code>false</code> otherwise.
 * @since 1.2.0
 */
  public boolean isSelected(  int series,  int item){
    XYSeries s=getSeries(series);
    XYDataItem i=s.getRawDataItem(item);
    return i.isSelected();
  }
  /** 
 * Sets the selection state for the specified data item and sends a                                                                                                                                                              {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series index.
 * @param item  the item index.
 * @param selected  the selection state.
 * @since 1.2.0
 */
  public void setSelected(  int series,  int item,  boolean selected){
    setSelected(series,item,selected,true);
  }
  /** 
 * Sets the selection state for the specified data item and, if requested, sends a                                                                                                                                                              {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series index.
 * @param item  the item index.
 * @param selected  the selection state.
 * @param notify  notify listeners?
 * @since 1.2.0
 */
  public void setSelected(  int series,  int item,  boolean selected,  boolean notify){
    XYSeries s=getSeries(series);
    XYDataItem i=s.getRawDataItem(item);
    i.setSelected(selected);
    if (notify) {
      fireSelectionEvent();
    }
  }
  /** 
 * Clears the selection state for all data items.
 * @since 1.2.0
 */
  public void clearSelection(){
    int seriesCount=getSeriesCount();
    for (int s=0; s < seriesCount; s++) {
      int itemCount=getItemCount(s);
      for (int i=0; i < itemCount; i++) {
        setSelected(s,i,false,false);
      }
    }
  }
  /** 
 * Fires an event to signal that the selection changed.
 * @since 1.2.0
 */
  public void fireSelectionEvent(){
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Tests this collection for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof XYSeriesCollection)) {
      return false;
    }
    XYSeriesCollection that=(XYSeriesCollection)obj;
    if (!this.intervalDelegate.equals(that.intervalDelegate)) {
      return false;
    }
    return ObjectUtilities.equal(this.data,that.data);
  }
  /** 
 * Returns a clone of this instance.
 * @return A clone.
 * @throws CloneNotSupportedException if there is a problem.
 */
  public Object clone() throws CloneNotSupportedException {
    XYSeriesCollection clone=(XYSeriesCollection)super.clone();
    clone.data=(List)ObjectUtilities.deepClone(this.data);
    clone.intervalDelegate=(IntervalXYDelegate)this.intervalDelegate.clone();
    return clone;
  }
  /** 
 * Returns a hash code.
 * @return A hash code.
 */
  public int hashCode(){
    int hash=5;
    hash=HashUtilities.hashCode(hash,this.intervalDelegate);
    hash=HashUtilities.hashCode(hash,this.data);
    return hash;
  }
  /** 
 * Returns the minimum x-value in the dataset.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The minimum value.
 */
  public double getDomainLowerBound(  boolean includeInterval){
    if (includeInterval) {
      return this.intervalDelegate.getDomainLowerBound(includeInterval);
    }
 else {
      double result=Double.NaN;
      int seriesCount=getSeriesCount();
      for (int s=0; s < seriesCount; s++) {
        XYSeries series=getSeries(s);
        double lowX=series.getMinX();
        if (Double.isNaN(result)) {
          result=lowX;
        }
 else {
          if (!Double.isNaN(lowX)) {
            result=Math.min(result,lowX);
          }
        }
      }
      return result;
    }
  }
  /** 
 * Returns the maximum x-value in the dataset.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The maximum value.
 */
  public double getDomainUpperBound(  boolean includeInterval){
    if (includeInterval) {
      return this.intervalDelegate.getDomainUpperBound(includeInterval);
    }
 else {
      double result=Double.NaN;
      int seriesCount=getSeriesCount();
      for (int s=0; s < seriesCount; s++) {
        XYSeries series=getSeries(s);
        double hiX=series.getMaxX();
        if (Double.isNaN(result)) {
          result=hiX;
        }
 else {
          if (!Double.isNaN(hiX)) {
            result=Math.max(result,hiX);
          }
        }
      }
      return result;
    }
  }
  /** 
 * Returns the range of the values in this dataset's domain.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The range (or <code>null</code> if the dataset contains novalues).
 */
  public Range getDomainBounds(  boolean includeInterval){
    if (includeInterval) {
      return this.intervalDelegate.getDomainBounds(includeInterval);
    }
 else {
      double lower=Double.POSITIVE_INFINITY;
      double upper=Double.NEGATIVE_INFINITY;
      int seriesCount=getSeriesCount();
      for (int s=0; s < seriesCount; s++) {
        XYSeries series=getSeries(s);
        double minX=series.getMinX();
        if (!Double.isNaN(minX)) {
          lower=Math.min(lower,minX);
        }
        double maxX=series.getMaxX();
        if (!Double.isNaN(maxX)) {
          upper=Math.max(upper,maxX);
        }
      }
      if (lower > upper) {
        return null;
      }
 else {
        return new Range(lower,upper);
      }
    }
  }
  /** 
 * Returns the interval width. This is used to calculate the start and end x-values, if/when the dataset is used as an                                                                                                                                                              {@link IntervalXYDataset}.
 * @return The interval width.
 */
  public double getIntervalWidth(){
    return this.intervalDelegate.getIntervalWidth();
  }
  /** 
 * Sets the interval width and sends a                                                                                                                                                              {@link DatasetChangeEvent} to allregistered listeners.
 * @param width  the width (negative values not permitted).
 */
  public void setIntervalWidth(  double width){
    if (width < 0.0) {
      throw new IllegalArgumentException("Negative 'width' argument.");
    }
    this.intervalDelegate.setFixedIntervalWidth(width);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns the interval position factor.
 * @return The interval position factor.
 */
  public double getIntervalPositionFactor(){
    return this.intervalDelegate.getIntervalPositionFactor();
  }
  /** 
 * Sets the interval position factor. This controls where the x-value is in relation to the interval surrounding the x-value (0.0 means the x-value will be positioned at the start, 0.5 in the middle, and 1.0 at the end).
 * @param factor  the factor.
 */
  public void setIntervalPositionFactor(  double factor){
    this.intervalDelegate.setIntervalPositionFactor(factor);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns whether the interval width is automatically calculated or not.
 * @return Whether the width is automatically calculated or not.
 */
  public boolean isAutoWidth(){
    return this.intervalDelegate.isAutoWidth();
  }
  /** 
 * Sets the flag that indicates wether the interval width is automatically calculated or not.
 * @param b  a boolean.
 */
  public void setAutoWidth(  boolean b){
    this.intervalDelegate.setAutoWidth(b);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns the range of the values in this dataset's range.
 * @param includeInterval  ignored.
 * @return The range (or <code>null</code> if the dataset contains novalues).
 */
  public Range getRangeBounds(  boolean includeInterval){
    double lower=Double.POSITIVE_INFINITY;
    double upper=Double.NEGATIVE_INFINITY;
    int seriesCount=getSeriesCount();
    for (int s=0; s < seriesCount; s++) {
      XYSeries series=getSeries(s);
      double minY=series.getMinY();
      if (!Double.isNaN(minY)) {
        lower=Math.min(lower,minY);
      }
      double maxY=series.getMaxY();
      if (!Double.isNaN(maxY)) {
        upper=Math.max(upper,maxY);
      }
    }
    if (lower > upper) {
      return null;
    }
 else {
      return new Range(lower,upper);
    }
  }
  /** 
 * Returns the minimum y-value in the dataset.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The minimum value.
 */
  public double getRangeLowerBound(  boolean includeInterval){
    double result=Double.NaN;
    int seriesCount=getSeriesCount();
    for (int s=0; s < seriesCount; s++) {
      XYSeries series=getSeries(s);
      double lowY=series.getMinY();
      if (Double.isNaN(result)) {
        result=lowY;
      }
 else {
        if (!Double.isNaN(lowY)) {
          result=Math.min(result,lowY);
        }
      }
    }
    return result;
  }
  /** 
 * Returns the maximum y-value in the dataset.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The maximum value.
 */
  public double getRangeUpperBound(  boolean includeInterval){
    double result=Double.NaN;
    int seriesCount=getSeriesCount();
    for (int s=0; s < seriesCount; s++) {
      XYSeries series=getSeries(s);
      double hiY=series.getMaxY();
      if (Double.isNaN(result)) {
        result=hiY;
      }
 else {
        if (!Double.isNaN(hiY)) {
          result=Math.max(result,hiY);
        }
      }
    }
    return result;
  }
}
