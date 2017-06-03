package org.jfree.data.time;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.jfree.chart.event.DatasetChangeInfo;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.data.DomainInfo;
import org.jfree.data.DomainOrder;
import org.jfree.data.Range;
import org.jfree.data.event.DatasetChangeEvent;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.SelectableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYDatasetSelectionState;
import org.jfree.data.xy.XYDomainInfo;
import org.jfree.data.xy.XYRangeInfo;
/** 
 * A collection of time series objects.  This class implements the                                                                                                                                                                   {@link XYDataset} interface, as well as the extended{@link IntervalXYDataset} interface.  This makes it a convenient dataset foruse with the  {@link org.jfree.chart.plot.XYPlot} class.
 */
public class TimeSeriesCollection extends AbstractIntervalXYDataset implements XYDataset, IntervalXYDataset, DomainInfo, XYDomainInfo, XYRangeInfo, XYDatasetSelectionState, SelectableXYDataset, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=834149929022371137L;
  /** 
 * Storage for the time series. 
 */
  private List data;
  /** 
 * A working calendar (to recycle) 
 */
  private Calendar workingCalendar;
  /** 
 * The point within each time period that is used for the X value when this collection is used as an                                                                                                                                                                    {@link org.jfree.data.xy.XYDataset}.  This can be the start, middle or end of the time period.
 */
  private TimePeriodAnchor xPosition;
  /** 
 * Constructs an empty dataset, tied to the default timezone.
 */
  public TimeSeriesCollection(){
    this(null,TimeZone.getDefault());
  }
  /** 
 * Constructs an empty dataset, tied to a specific timezone.
 * @param zone  the timezone (<code>null</code> permitted, will use<code>TimeZone.getDefault()</code> in that case).
 */
  public TimeSeriesCollection(  TimeZone zone){
    this(null,zone);
  }
  /** 
 * Constructs a dataset containing a single series (more can be added), tied to the default timezone.
 * @param series the series (<code>null</code> permitted).
 */
  public TimeSeriesCollection(  TimeSeries series){
    this(series,TimeZone.getDefault());
  }
  /** 
 * Constructs a dataset containing a single series (more can be added), tied to a specific timezone.
 * @param series  a series to add to the collection (<code>null</code>permitted).
 * @param zone  the timezone (<code>null</code> permitted, will use<code>TimeZone.getDefault()</code> in that case).
 */
  public TimeSeriesCollection(  TimeSeries series,  TimeZone zone){
    super();
    if (zone == null) {
      zone=TimeZone.getDefault();
    }
    this.workingCalendar=Calendar.getInstance(zone);
    this.data=new ArrayList();
    if (series != null) {
      this.data.add(series);
      series.addChangeListener(this);
    }
    this.xPosition=TimePeriodAnchor.START;
    setSelectionState(this);
  }
  /** 
 * Returns the order of the domain values in this dataset.
 * @return {@link DomainOrder#ASCENDING}
 */
  public DomainOrder getDomainOrder(){
    return DomainOrder.ASCENDING;
  }
  /** 
 * Returns the position within each time period that is used for the X value when the collection is used as an                                                                                                                                                                   {@link org.jfree.data.xy.XYDataset}.
 * @return The anchor position (never <code>null</code>).
 */
  public TimePeriodAnchor getXPosition(){
    return this.xPosition;
  }
  /** 
 * Sets the position within each time period that is used for the X values when the collection is used as an                                                                                                                                                                    {@link XYDataset}, then sends a                                                                                                                                                                   {@link DatasetChangeEvent} is sent to all registered listeners.
 * @param anchor  the anchor position (<code>null</code> not permitted).
 */
  public void setXPosition(  TimePeriodAnchor anchor){
    if (anchor == null) {
      throw new IllegalArgumentException("Null 'anchor' argument.");
    }
    this.xPosition=anchor;
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns a list of all the series in the collection.
 * @return The list (which is unmodifiable).
 */
  public List getSeries(){
    return Collections.unmodifiableList(this.data);
  }
  /** 
 * Returns the number of series in the collection.
 * @return The series count.
 */
  public int getSeriesCount(){
    return this.data.size();
  }
  /** 
 * Returns the index of the specified series, or -1 if that series is not present in the dataset.
 * @param series  the series (<code>null</code> not permitted).
 * @return The series index.
 * @since 1.0.6
 */
  public int indexOf(  TimeSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    return this.data.indexOf(series);
  }
  /** 
 * Returns a series.
 * @param series  the index of the series (zero-based).
 * @return The series.
 */
  public TimeSeries getSeries(  int series){
    if ((series < 0) || (series >= getSeriesCount())) {
      throw new IllegalArgumentException("The 'series' argument is out of bounds (" + series + ").");
    }
    return (TimeSeries)this.data.get(series);
  }
  /** 
 * Returns the series with the specified key, or <code>null</code> if there is no such series.
 * @param key  the series key (<code>null</code> permitted).
 * @return The series with the given key.
 */
  public TimeSeries getSeries(  Comparable key){
    TimeSeries result=null;
    Iterator iterator=this.data.iterator();
    while (iterator.hasNext()) {
      TimeSeries series=(TimeSeries)iterator.next();
      Comparable k=series.getKey();
      if (k != null && k.equals(key)) {
        result=series;
      }
    }
    return result;
  }
  /** 
 * Returns the key for a series.
 * @param series  the index of the series (zero-based).
 * @return The key for a series.
 */
  public Comparable getSeriesKey(  int series){
    return getSeries(series).getKey();
  }
  /** 
 * Adds a series to the collection and sends a                                                                                                                                                                    {@link DatasetChangeEvent} toall registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 */
  public void addSeries(  TimeSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    this.data.add(series);
    series.addChangeListener(this);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes the specified series from the collection and sends a                                                                                                                                                                   {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series (<code>null</code> not permitted).
 */
  public void removeSeries(  TimeSeries series){
    if (series == null) {
      throw new IllegalArgumentException("Null 'series' argument.");
    }
    this.data.remove(series);
    series.removeChangeListener(this);
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Removes a series from the collection.
 * @param index  the series index (zero-based).
 */
  public void removeSeries(  int index){
    TimeSeries series=getSeries(index);
    if (series != null) {
      removeSeries(series);
    }
  }
  /** 
 * Removes all the series from the collection and sends a                                                                                                                                                                   {@link DatasetChangeEvent} to all registered listeners.
 */
  public void removeAllSeries(){
    for (int i=0; i < this.data.size(); i++) {
      TimeSeries series=(TimeSeries)this.data.get(i);
      series.removeChangeListener(this);
    }
    this.data.clear();
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns the number of items in the specified series.  This method is provided for convenience.
 * @param series  the series index (zero-based).
 * @return The item count.
 */
  public int getItemCount(  int series){
    return getSeries(series).getItemCount();
  }
  /** 
 * Returns the x-value (as a double primitive) for an item within a series.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The x-value.
 */
  public double getXValue(  int series,  int item){
    TimeSeries s=(TimeSeries)this.data.get(series);
    RegularTimePeriod period=s.getTimePeriod(item);
    return getX(period);
  }
  /** 
 * Returns the x-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public Number getX(  int series,  int item){
    TimeSeries ts=(TimeSeries)this.data.get(series);
    RegularTimePeriod period=ts.getTimePeriod(item);
    return new Long(getX(period));
  }
  /** 
 * Returns the x-value for a time period.
 * @param period  the time period (<code>null</code> not permitted).
 * @return The x-value.
 */
  protected synchronized long getX(  RegularTimePeriod period){
    long result=0L;
    if (this.xPosition == TimePeriodAnchor.START) {
      result=period.getFirstMillisecond(this.workingCalendar);
    }
 else {
      if (this.xPosition == TimePeriodAnchor.MIDDLE) {
        result=period.getMiddleMillisecond(this.workingCalendar);
      }
 else {
        if (this.xPosition == TimePeriodAnchor.END) {
          result=period.getLastMillisecond(this.workingCalendar);
        }
      }
    }
    return result;
  }
  /** 
 * Returns the starting X value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value.
 */
  public synchronized Number getStartX(  int series,  int item){
    TimeSeries ts=(TimeSeries)this.data.get(series);
    return new Long(ts.getTimePeriod(item).getFirstMillisecond(this.workingCalendar));
  }
  /** 
 * Returns the ending X value for the specified series and item.
 * @param series The series (zero-based index).
 * @param item  The item (zero-based index).
 * @return The value.
 */
  public synchronized Number getEndX(  int series,  int item){
    TimeSeries ts=(TimeSeries)this.data.get(series);
    return new Long(ts.getTimePeriod(item).getLastMillisecond(this.workingCalendar));
  }
  /** 
 * Returns the y-value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value (possibly <code>null</code>).
 */
  public Number getY(  int series,  int item){
    TimeSeries ts=(TimeSeries)this.data.get(series);
    return ts.getValue(item);
  }
  /** 
 * Returns the starting Y value for the specified series and item.
 * @param series  the series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value (possibly <code>null</code>).
 */
  public Number getStartY(  int series,  int item){
    return getY(series,item);
  }
  /** 
 * Returns the ending Y value for the specified series and item.
 * @param series  te series (zero-based index).
 * @param item  the item (zero-based index).
 * @return The value (possibly <code>null</code>).
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
    TimeSeries s=getSeries(series);
    TimeSeriesDataItem i=s.getRawDataItem(item);
    return i.isSelected();
  }
  /** 
 * Sets the selection state for the specified data item and sends a                                                                                                                                                                    {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series index.
 * @param item  the item index.
 * @param selected  the selection state.
 * @since 1.2.0
 */
  public void setSelected(  int series,  int item,  boolean selected){
    setSelected(series,item,selected,true);
  }
  /** 
 * Sets the selection state for the specified data item and, if requested, sends a                                                                                                                                                                    {@link DatasetChangeEvent} to all registered listeners.
 * @param series  the series index.
 * @param item  the item index.
 * @param selected  the selection state.
 * @param notify  notify listeners?
 * @since 1.2.0
 */
  public void setSelected(  int series,  int item,  boolean selected,  boolean notify){
    TimeSeries s=getSeries(series);
    TimeSeriesDataItem i=s.getRawDataItem(item);
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
 * Sends an event to all registered listeners to indicate that the selection has changed.
 * @since 1.2.0
 */
  public void fireSelectionEvent(){
    fireDatasetChanged(new DatasetChangeInfo());
  }
  /** 
 * Returns the indices of the two data items surrounding a particular millisecond value.
 * @param series  the series index.
 * @param milliseconds  the time.
 * @return An array containing the (two) indices of the items surroundingthe time.
 */
  public int[] getSurroundingItems(  int series,  long milliseconds){
    int[] result=new int[]{-1,-1};
    TimeSeries timeSeries=getSeries(series);
    for (int i=0; i < timeSeries.getItemCount(); i++) {
      Number x=getX(series,i);
      long m=x.longValue();
      if (m <= milliseconds) {
        result[0]=i;
      }
      if (m >= milliseconds) {
        result[1]=i;
        break;
      }
    }
    return result;
  }
  /** 
 * Returns the minimum x-value in the dataset.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The minimum value.
 */
  public double getDomainLowerBound(  boolean includeInterval){
    double result=Double.NaN;
    Range r=getDomainBounds(includeInterval);
    if (r != null) {
      result=r.getLowerBound();
    }
    return result;
  }
  /** 
 * Returns the maximum x-value in the dataset.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The maximum value.
 */
  public double getDomainUpperBound(  boolean includeInterval){
    double result=Double.NaN;
    Range r=getDomainBounds(includeInterval);
    if (r != null) {
      result=r.getUpperBound();
    }
    return result;
  }
  /** 
 * Returns the range of the values in this dataset's domain.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The range.
 */
  public Range getDomainBounds(  boolean includeInterval){
    Range result=null;
    Iterator iterator=this.data.iterator();
    while (iterator.hasNext()) {
      TimeSeries series=(TimeSeries)iterator.next();
      int count=series.getItemCount();
      if (count > 0) {
        RegularTimePeriod start=series.getTimePeriod(0);
        RegularTimePeriod end=series.getTimePeriod(count - 1);
        Range temp;
        if (!includeInterval) {
          temp=new Range(getX(start),getX(end));
        }
 else {
          temp=new Range(start.getFirstMillisecond(this.workingCalendar),end.getLastMillisecond(this.workingCalendar));
        }
        result=Range.combine(result,temp);
      }
    }
    return result;
  }
  /** 
 * Returns the bounds of the domain values for the specified series.
 * @param visibleSeriesKeys  a list of keys for the visible series.
 * @param includeInterval  include the x-interval?
 * @return A range.
 * @since 1.0.13
 */
  public Range getDomainBounds(  List visibleSeriesKeys,  boolean includeInterval){
    Range result=null;
    Iterator iterator=visibleSeriesKeys.iterator();
    while (iterator.hasNext()) {
      Comparable seriesKey=(Comparable)iterator.next();
      TimeSeries series=getSeries(seriesKey);
      int count=series.getItemCount();
      if (count > 0) {
        RegularTimePeriod start=series.getTimePeriod(0);
        RegularTimePeriod end=series.getTimePeriod(count - 1);
        Range temp;
        if (!includeInterval) {
          temp=new Range(getX(start),getX(end));
        }
 else {
          temp=new Range(start.getFirstMillisecond(this.workingCalendar),end.getLastMillisecond(this.workingCalendar));
        }
        result=Range.combine(result,temp);
      }
    }
    return result;
  }
  /** 
 * Returns the bounds for the y-values in the dataset.
 * @param visibleSeriesKeys  the visible series keys.
 * @param xRange  the x-range (<code>null</code> not permitted).
 * @param includeInterval  ignored.
 * @return The bounds.
 * @since 1.0.14
 */
  public Range getRangeBounds(  List visibleSeriesKeys,  Range xRange,  boolean includeInterval){
    Range result=null;
    Iterator iterator=visibleSeriesKeys.iterator();
    while (iterator.hasNext()) {
      Comparable seriesKey=(Comparable)iterator.next();
      TimeSeries series=getSeries(seriesKey);
      Range r=null;
      r=new Range(series.getMinY(),series.getMaxY());
      result=Range.combine(result,r);
    }
    return result;
  }
  /** 
 * Tests this time series collection for equality with another object.
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof TimeSeriesCollection)) {
      return false;
    }
    TimeSeriesCollection that=(TimeSeriesCollection)obj;
    if (this.xPosition != that.xPosition) {
      return false;
    }
    if (!ObjectUtilities.equal(this.data,that.data)) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code value for the object.
 * @return The hashcode
 */
  public int hashCode(){
    int result;
    result=this.data.hashCode();
    result=29 * result + (this.workingCalendar != null ? this.workingCalendar.hashCode() : 0);
    result=29 * result + (this.xPosition != null ? this.xPosition.hashCode() : 0);
    return result;
  }
  /** 
 * Returns a clone of this time series collection.
 * @return A clone.
 * @throws java.lang.CloneNotSupportedException
 */
  public Object clone() throws CloneNotSupportedException {
    TimeSeriesCollection clone=(TimeSeriesCollection)super.clone();
    clone.data=(List)ObjectUtilities.deepClone(this.data);
    clone.workingCalendar=(Calendar)this.workingCalendar.clone();
    return clone;
  }
}
