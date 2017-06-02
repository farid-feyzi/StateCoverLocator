package org.jfree.data.time;
import java.util.Calendar;
import java.util.TimeZone;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.data.RangeInfo;
import org.jfree.data.event.SeriesChangeEvent;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
/** 
 * A dynamic dataset. <p> Like FastTimeSeriesCollection, this class is a functional replacement for JFreeChart's TimeSeriesCollection _and_ TimeSeries classes. FastTimeSeriesCollection is appropriate for a fixed time range; for real-time applications this subclass adds the ability to append new data and discard the oldest. In this class, the arrays used in FastTimeSeriesCollection become FIFO's. NOTE:As presented here, all data is assumed >= 0, an assumption which is embodied only in methods associated with interface RangeInfo.
 */
public class DynamicTimeSeriesCollection extends AbstractIntervalXYDataset implements IntervalXYDataset, DomainInfo, RangeInfo {
  /** 
 * Useful constant for controlling the x-value returned for a time period.
 */
  public static final int START=0;
  /** 
 * Useful constant for controlling the x-value returned for a time period.
 */
  public static final int MIDDLE=1;
  /** 
 * Useful constant for controlling the x-value returned for a time period.
 */
  public static final int END=2;
  /** 
 * The maximum number of items for each series (can be overridden). 
 */
  private int maximumItemCount=2000;
  /** 
 * The history count. 
 */
  protected int historyCount;
  /** 
 * Storage for the series keys. 
 */
  private Comparable[] seriesKeys;
  /** 
 * The time period class - barely used, and could be removed (DG). 
 */
  private Class timePeriodClass=Minute.class;
  /** 
 * Storage for the x-values. 
 */
  protected RegularTimePeriod[] pointsInTime;
  /** 
 * The number of series. 
 */
  private int seriesCount;
  /** 
 * A wrapper for a fixed array of float values.
 */
protected class ValueSequence {
    /** 
 * Storage for the float values. 
 */
    float[] dataPoints;
    /** 
 * Default constructor:
 */
    public ValueSequence(){
      this(DynamicTimeSeriesCollection.this.maximumItemCount);
    }
    /** 
 * Creates a sequence with the specified length.
 * @param length  the length.
 */
    public ValueSequence(    int length){
      this.dataPoints=new float[length];
      for (int i=0; i < length; i++) {
        this.dataPoints[i]=0.0f;
      }
    }
    /** 
 * Enters data into the storage array.
 * @param index  the index.
 * @param value  the value.
 */
    public void enterData(    int index,    float value){
      this.dataPoints[index]=value;
    }
    /** 
 * Returns a value from the storage array.
 * @param index  the index.
 * @return The value.
 */
    public float getData(    int index){
      return this.dataPoints[index];
    }
  }
  /** 
 * An array for storing the objects that represent each series. 
 */
  protected ValueSequence[] valueHistory;
  /** 
 * A working calendar (to recycle) 
 */
  protected Calendar workingCalendar;
  /** 
 * The position within a time period to return as the x-value (START, MIDDLE or END).
 */
  private int position;
  /** 
 * A flag that indicates that the domain is 'points in time'.  If this flag is true, only the x-value is used to determine the range of values in the domain, the start and end x-values are ignored.
 */
  private boolean domainIsPointsInTime;
  /** 
 * index for mapping: points to the oldest valid time & data. 
 */
  private int oldestAt;
  /** 
 * Index of the newest data item. 
 */
  private int newestAt;
  /** 
 * the # of msec by which time advances. 
 */
  private long deltaTime;
  /** 
 * Cached domain start (for use by DomainInfo). 
 */
  private Long domainStart;
  /** 
 * Cached domain end (for use by DomainInfo). 
 */
  private Long domainEnd;
  /** 
 * Cached domain range (for use by DomainInfo). 
 */
  private Range domainRange;
  /** 
 * The minimum value. 
 */
  private Float minValue=new Float(0.0f);
  /** 
 * The maximum value. 
 */
  private Float maxValue=null;
  /** 
 * The value range. 
 */
  private Range valueRange;
  /** 
 * Constructs a dataset with capacity for N series, tied to default timezone.
 * @param nSeries the number of series to be accommodated.
 * @param nMoments the number of TimePeriods to be spanned.
 */
  public DynamicTimeSeriesCollection(  int nSeries,  int nMoments){
    this(nSeries,nMoments,new Millisecond(),TimeZone.getDefault());
    this.newestAt=nMoments - 1;
  }
  /** 
 * Constructs an empty dataset, tied to a specific timezone.
 * @param nSeries the number of series to be accommodated
 * @param nMoments the number of TimePeriods to be spanned
 * @param zone the timezone.
 */
  public DynamicTimeSeriesCollection(  int nSeries,  int nMoments,  TimeZone zone){
    this(nSeries,nMoments,new Millisecond(),zone);
    this.newestAt=nMoments - 1;
  }
  /** 
 * Creates a new dataset.
 * @param nSeries  the number of series.
 * @param nMoments  the number of items per series.
 * @param timeSample  a time period sample.
 */
  public DynamicTimeSeriesCollection(  int nSeries,  int nMoments,  RegularTimePeriod timeSample){
    this(nSeries,nMoments,timeSample,TimeZone.getDefault());
  }
  /** 
 * Creates a new dataset.
 * @param nSeries  the number of series.
 * @param nMoments  the number of items per series.
 * @param timeSample  a time period sample.
 * @param zone  the time zone.
 */
  public DynamicTimeSeriesCollection(  int nSeries,  int nMoments,  RegularTimePeriod timeSample,  TimeZone zone){
    this.maximumItemCount=nMoments;
    this.historyCount=nMoments;
    this.seriesKeys=new Comparable[nSeries];
    for (int i=0; i < nSeries; i++) {
      this.seriesKeys[i]="";
    }
    this.newestAt=nMoments - 1;
    this.valueHistory=new ValueSequence[nSeries];
    this.timePeriodClass=timeSample.getClass();
    if (this.timePeriodClass == Second.class) {
      this.pointsInTime=new Second[nMoments];
    }
 else {
      if (this.timePeriodClass == Minute.class) {
        this.pointsInTime=new Minute[nMoments];
      }
 else {
        if (this.timePeriodClass == Hour.class) {
          this.pointsInTime=new Hour[nMoments];
        }
      }
    }
    this.workingCalendar=Calendar.getInstance(zone);
    this.position=START;
    this.domainIsPointsInTime=true;
  }
  /** 
 * Fill the pointsInTime with times using TimePeriod.next(): Will silently return if the time array was already populated. Also computes the data cached for later use by methods implementing the DomainInfo interface:
 * @param start  the start.
 * @return ??.
 */
  public synchronized long setTimeBase(  RegularTimePeriod start){
    if (this.pointsInTime[0] == null) {
      this.pointsInTime[0]=start;
      for (int i=1; i < this.historyCount; i++) {
        this.pointsInTime[i]=this.pointsInTime[i - 1].next();
      }
    }
    long oldestL=this.pointsInTime[0].getFirstMillisecond(this.workingCalendar);
    long nextL=this.pointsInTime[1].getFirstMillisecond(this.workingCalendar);
    this.deltaTime=nextL - oldestL;
    this.oldestAt=0;
    this.newestAt=this.historyCount - 1;
    findDomainLimits();
    return this.deltaTime;
  }
  /** 
 * Finds the domain limits.  Note: this doesn't need to be synchronized because it's called from within another method that already is.
 */
  protected void findDomainLimits(){
    long startL=getOldestTime().getFirstMillisecond(this.workingCalendar);
    long endL;
    if (this.domainIsPointsInTime) {
      endL=getNewestTime().getFirstMillisecond(this.workingCalendar);
    }
 else {
      endL=getNewestTime().getLastMillisecond(this.workingCalendar);
    }
    this.domainStart=new Long(startL);
    this.domainEnd=new Long(endL);
    this.domainRange=new Range(startL,endL);
  }
  /** 
 * Returns the x position type (START, MIDDLE or END).
 * @return The x position type.
 */
  public int getPosition(){
    return this.position;
  }
  /** 
 * Sets the x position type (START, MIDDLE or END).
 * @param position The x position type.
 */
  public void setPosition(  int position){
    this.position=position;
  }
  /** 
 * Adds a series to the dataset.  Only the y-values are supplied, the x-values are specified elsewhere.
 * @param values  the y-values.
 * @param seriesNumber  the series index (zero-based).
 * @param seriesKey  the series key.Use this as-is during setup only, or add the synchronized keyword around the copy loop.
 */
  public void addSeries(  float[] values,  int seriesNumber,  Comparable seriesKey){
    invalidateRangeInfo();
    int i;
    if (values == null) {
      throw new IllegalArgumentException("TimeSeriesDataset.addSeries(): " + "cannot add null array of values.");
    }
    if (seriesNumber >= this.valueHistory.length) {
      throw new IllegalArgumentException("TimeSeriesDataset.addSeries(): " + "cannot add more series than specified in c'tor");
    }
    if (this.valueHistory[seriesNumber] == null) {
      this.valueHistory[seriesNumber]=new ValueSequence(this.historyCount);
      this.seriesCount++;
    }
    int srcLength=values.length;
    int copyLength=this.historyCount;
    boolean fillNeeded=false;
    if (srcLength < this.historyCount) {
      fillNeeded=true;
      copyLength=srcLength;
    }
    for (i=0; i < copyLength; i++) {
      this.valueHistory[seriesNumber].enterData(i,values[i]);
    }
    if (fillNeeded) {
      for (i=copyLength; i < this.historyCount; i++) {
        this.valueHistory[seriesNumber].enterData(i,0.0f);
      }
    }
    if (seriesKey != null) {
      this.seriesKeys[seriesNumber]=seriesKey;
    }
    fireSeriesChanged();
  }
  /** 
 * Sets the name of a series.  If planning to add values individually.
 * @param seriesNumber  the series.
 * @param key  the new key.
 */
  public void setSeriesKey(  int seriesNumber,  Comparable key){
    this.seriesKeys[seriesNumber]=key;
  }
  /** 
 * Adds a value to a series.
 * @param seriesNumber  the series index.
 * @param index  ??.
 * @param value  the value.
 */
  public void addValue(  int seriesNumber,  int index,  float value){
    invalidateRangeInfo();
    if (seriesNumber >= this.valueHistory.length) {
      throw new IllegalArgumentException("TimeSeriesDataset.addValue(): series #" + seriesNumber + "unspecified in c'tor");
    }
    if (this.valueHistory[seriesNumber] == null) {
      this.valueHistory[seriesNumber]=new ValueSequence(this.historyCount);
      this.seriesCount++;
    }
    this.valueHistory[seriesNumber].enterData(index,value);
    fireSeriesChanged();
  }
  /** 
 * Returns the number of series in the collection.
 * @return The series count.
 */
  public int getSeriesCount(){
    return this.seriesCount;
  }
  /** 
 * Returns the number of items in a series. <p> For this implementation, all series have the same number of items.
 * @param series  the series index (zero-based).
 * @return The item count.
 */
  public int getItemCount(  int series){
    return this.historyCount;
  }
  /** 
 * Re-map an index, for use in retrieving data.
 * @param toFetch  the index.
 * @return The translated index.
 */
  protected int translateGet(  int toFetch){
    if (this.oldestAt == 0) {
      return toFetch;
    }
    int newIndex=toFetch + this.oldestAt;
    if (newIndex >= this.historyCount) {
      newIndex-=this.historyCount;
    }
    return newIndex;
  }
  /** 
 * Returns the actual index to a time offset by "delta" from newestAt.
 * @param delta  the delta.
 * @return The offset.
 */
  public int offsetFromNewest(  int delta){
    return wrapOffset(this.newestAt + delta);
  }
  /** 
 * ??
 * @param delta ??
 * @return The offset.
 */
  public int offsetFromOldest(  int delta){
    return wrapOffset(this.oldestAt + delta);
  }
  /** 
 * ??
 * @param protoIndex  the index.
 * @return The offset.
 */
  protected int wrapOffset(  int protoIndex){
    int tmp=protoIndex;
    if (tmp >= this.historyCount) {
      tmp-=this.historyCount;
    }
 else {
      if (tmp < 0) {
        tmp+=this.historyCount;
      }
    }
    return tmp;
  }
  /** 
 * Adjust the array offset as needed when a new time-period is added: Increments the indices "oldestAt" and "newestAt", mod(array length), zeroes the series values at newestAt, returns the new TimePeriod.
 * @return The new time period.
 */
  public synchronized RegularTimePeriod advanceTime(){
    RegularTimePeriod nextInstant=this.pointsInTime[this.newestAt].next();
    this.newestAt=this.oldestAt;
    boolean extremaChanged=false;
    float oldMax=0.0f;
    if (this.maxValue != null) {
      oldMax=this.maxValue.floatValue();
    }
    for (int s=0; s < getSeriesCount(); s++) {
      if (this.valueHistory[s].getData(this.oldestAt) == oldMax) {
        extremaChanged=true;
      }
      if (extremaChanged) {
        break;
      }
    }
    if (extremaChanged) {
      invalidateRangeInfo();
    }
    float wiper=(float)0.0;
    for (int s=0; s < getSeriesCount(); s++) {
      this.valueHistory[s].enterData(this.newestAt,wiper);
    }
    this.pointsInTime[this.newestAt]=nextInstant;
    this.oldestAt++;
    if (this.oldestAt >= this.historyCount) {
      this.oldestAt=0;
    }
    long startL=this.domainStart.longValue();
    this.domainStart=new Long(startL + this.deltaTime);
    long endL=this.domainEnd.longValue();
    this.domainEnd=new Long(endL + this.deltaTime);
    this.domainRange=new Range(startL,endL);
    fireSeriesChanged();
    return nextInstant;
  }
  /** 
 * Invalidates the range info.
 */
  public void invalidateRangeInfo(){
    this.maxValue=null;
    this.valueRange=null;
  }
  /** 
 * Returns the maximum value.
 * @return The maximum value.
 */
  protected double findMaxValue(){
    double max=0.0f;
    for (int s=0; s < getSeriesCount(); s++) {
      for (int i=0; i < this.historyCount; i++) {
        double tmp=getYValue(s,i);
        if (tmp > max) {
          max=tmp;
        }
      }
    }
    return max;
  }
  /** 
 * Returns the index of the oldest data item.
 * @return The index.
 */
  public int getOldestIndex(){
    return this.oldestAt;
  }
  /** 
 * Returns the index of the newest data item.
 * @return The index.
 */
  public int getNewestIndex(){
    return this.newestAt;
  }
  /** 
 * Appends new data.
 * @param newData  the data.
 */
  public void appendData(  float[] newData){
    int nDataPoints=newData.length;
    if (nDataPoints > this.valueHistory.length) {
      throw new IllegalArgumentException("More data than series to put them in");
    }
    int s;
    for (s=0; s < nDataPoints; s++) {
      if (this.valueHistory[s] == null) {
        this.valueHistory[s]=new ValueSequence(this.historyCount);
      }
      this.valueHistory[s].enterData(this.newestAt,newData[s]);
    }
    fireSeriesChanged();
  }
  /** 
 * Appends data at specified index, for loading up with data from file(s).
 * @param newData  the data
 * @param insertionIndex  the index value at which to put it
 * @param refresh  value of n in "refresh the display on every nth call"(ignored if <= 0 )
 */
  public void appendData(  float[] newData,  int insertionIndex,  int refresh){
    int nDataPoints=newData.length;
    if (nDataPoints > this.valueHistory.length) {
      throw new IllegalArgumentException("More data than series to put them " + "in");
    }
    for (int s=0; s < nDataPoints; s++) {
      if (this.valueHistory[s] == null) {
        this.valueHistory[s]=new ValueSequence(this.historyCount);
      }
      this.valueHistory[s].enterData(insertionIndex,newData[s]);
    }
    if (refresh > 0) {
      insertionIndex++;
      if (insertionIndex % refresh == 0) {
        fireSeriesChanged();
      }
    }
  }
  /** 
 * Returns the newest time.
 * @return The newest time.
 */
  public RegularTimePeriod getNewestTime(){
    return this.pointsInTime[this.newestAt];
  }
  /** 
 * Returns the oldest time.
 * @return The oldest time.
 */
  public RegularTimePeriod getOldestTime(){
    return this.pointsInTime[this.oldestAt];
  }
  /** 
 * Returns the x-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public Number getX(  int series,  int item){
    RegularTimePeriod tp=this.pointsInTime[translateGet(item)];
    return new Long(getX(tp));
  }
  /** 
 * Returns the y-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public double getYValue(  int series,  int item){
    ValueSequence values=this.valueHistory[series];
    return values.getData(translateGet(item));
  }
  /** 
 * Returns the y-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public Number getY(  int series,  int item){
    return new Float(getYValue(series,item));
  }
  /** 
 * Returns the start x-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public Number getStartX(  int series,  int item){
    RegularTimePeriod tp=this.pointsInTime[translateGet(item)];
    return new Long(tp.getFirstMillisecond(this.workingCalendar));
  }
  /** 
 * Returns the end x-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public Number getEndX(  int series,  int item){
    RegularTimePeriod tp=this.pointsInTime[translateGet(item)];
    return new Long(tp.getLastMillisecond(this.workingCalendar));
  }
  /** 
 * Returns the start y-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public Number getStartY(  int series,  int item){
    return getY(series,item);
  }
  /** 
 * Returns the end y-value.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The value.
 */
  public Number getEndY(  int series,  int item){
    return getY(series,item);
  }
  /** 
 * Returns the key for a series.
 * @param series  the series index (zero-based).
 * @return The key.
 */
  public Comparable getSeriesKey(  int series){
    return this.seriesKeys[series];
  }
  /** 
 * Sends a                                                                                                                                                               {@link SeriesChangeEvent} to all registered listeners.
 */
  protected void fireSeriesChanged(){
    seriesChanged(new SeriesChangeEvent(this));
  }
  /** 
 * Returns the minimum x-value in the dataset.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The minimum value.
 */
  public double getDomainLowerBound(  boolean includeInterval){
    return this.domainStart.doubleValue();
  }
  /** 
 * Returns the maximum x-value in the dataset.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The maximum value.
 */
  public double getDomainUpperBound(  boolean includeInterval){
    return this.domainEnd.doubleValue();
  }
  /** 
 * Returns the range of the values in this dataset's domain.
 * @param includeInterval  a flag that determines whether or not thex-interval is taken into account.
 * @return The range.
 */
  public Range getDomainBounds(  boolean includeInterval){
    if (this.domainRange == null) {
      findDomainLimits();
    }
    return this.domainRange;
  }
  /** 
 * Returns the x-value for a time period.
 * @param period  the period.
 * @return The x-value.
 */
  private long getX(  RegularTimePeriod period){
switch (this.position) {
case (START):
      return period.getFirstMillisecond(this.workingCalendar);
case (MIDDLE):
    return period.getMiddleMillisecond(this.workingCalendar);
case (END):
  return period.getLastMillisecond(this.workingCalendar);
default :
return period.getMiddleMillisecond(this.workingCalendar);
}
}
/** 
 * Returns the minimum range value.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The minimum range value.
 */
public double getRangeLowerBound(boolean includeInterval){
double result=Double.NaN;
if (this.minValue != null) {
result=this.minValue.doubleValue();
}
return result;
}
/** 
 * Returns the maximum range value.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The maximum range value.
 */
public double getRangeUpperBound(boolean includeInterval){
double result=Double.NaN;
if (this.maxValue != null) {
result=this.maxValue.doubleValue();
}
return result;
}
/** 
 * Returns the value range.
 * @param includeInterval  a flag that determines whether or not they-interval is taken into account.
 * @return The range.
 */
public Range getRangeBounds(boolean includeInterval){
if (this.valueRange == null) {
double max=getRangeUpperBound(includeInterval);
this.valueRange=new Range(0.0,max);
}
return this.valueRange;
}
}
