package org.jfree.chart.editor;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.ui.LCBLayout;
import org.jfree.chart.ui.PaintSample;
import org.jfree.chart.ui.StrokeChooserPanel;
import org.jfree.chart.ui.StrokeSample;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.chart.util.ResourceBundleWrapper;
/** 
 * A panel for editing the properties of a      {@link Plot}.
 */
class DefaultPlotEditor extends JPanel implements ActionListener {
  /** 
 * Orientation constants. 
 */
  private final static String[] orientationNames={"Vertical","Horizontal"};
  private final static int ORIENTATION_VERTICAL=0;
  private final static int ORIENTATION_HORIZONTAL=1;
  /** 
 * The paint (color) used to fill the background of the plot. 
 */
  private PaintSample backgroundPaintSample;
  /** 
 * The stroke used to draw the outline of the plot. 
 */
  private StrokeSample outlineStrokeSample;
  /** 
 * The paint (color) used to draw the outline of the plot. 
 */
  private PaintSample outlinePaintSample;
  /** 
 * A panel used to display/edit the properties of the domain axis (if any).
 */
  private DefaultAxisEditor domainAxisPropertyPanel;
  /** 
 * A panel used to display/edit the properties of the range axis (if any).
 */
  private DefaultAxisEditor rangeAxisPropertyPanel;
  /** 
 * An array of stroke samples to choose from. 
 */
  private StrokeSample[] availableStrokeSamples;
  /** 
 * The insets for the plot. 
 */
  private RectangleInsets plotInsets;
  /** 
 * The orientation for the plot (for <tt>CategoryPlot</tt>s and <tt>XYPlot</tt>s).
 */
  private PlotOrientation plotOrientation;
  /** 
 * The orientation combo box (for <tt>CategoryPlot</tt>s and <tt>XYPlot</tt>s).
 */
  private JComboBox orientationCombo;
  /** 
 * Whether or not to draw lines between each data point (for <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s).
 */
  private Boolean drawLines;
  /** 
 * The checkbox for whether or not to draw lines between each data point.
 */
  private JCheckBox drawLinesCheckBox;
  /** 
 * Whether or not to draw shapes at each data point (for <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s).
 */
  private Boolean drawShapes;
  /** 
 * The checkbox for whether or not to draw shapes at each data point.
 */
  private JCheckBox drawShapesCheckBox;
  /** 
 * The resourceBundle for the localization. 
 */
  protected static ResourceBundle localizationResources=ResourceBundleWrapper.getBundle("org.jfree.chart.editor.LocalizationBundle");
  /** 
 * Standard constructor - constructs a panel for editing the properties of the specified plot. <P> In designing the panel, we need to be aware that subclasses of Plot will need to implement subclasses of PlotPropertyEditPanel - so we need to leave one or two 'slots' where the subclasses can extend the user interface.
 * @param plot  the plot, which should be changed.
 */
  public DefaultPlotEditor(  Plot plot){
    this.plotInsets=plot.getInsets();
    this.backgroundPaintSample=new PaintSample(plot.getBackgroundPaint());
    this.outlineStrokeSample=new StrokeSample(plot.getOutlineStroke());
    this.outlinePaintSample=new PaintSample(plot.getOutlinePaint());
    if (plot instanceof CategoryPlot) {
      this.plotOrientation=((CategoryPlot)plot).getOrientation();
    }
 else {
      if (plot instanceof XYPlot) {
        this.plotOrientation=((XYPlot)plot).getOrientation();
      }
    }
    if (plot instanceof CategoryPlot) {
      CategoryItemRenderer renderer=((CategoryPlot)plot).getRenderer();
      if (renderer instanceof LineAndShapeRenderer) {
        LineAndShapeRenderer r=(LineAndShapeRenderer)renderer;
        this.drawLines=Boolean.valueOf(r.getBaseLinesVisible());
        this.drawShapes=Boolean.valueOf(r.getBaseShapesVisible());
      }
    }
 else {
      if (plot instanceof XYPlot) {
        XYItemRenderer renderer=((XYPlot)plot).getRenderer();
        if (renderer instanceof StandardXYItemRenderer) {
          StandardXYItemRenderer r=(StandardXYItemRenderer)renderer;
          this.drawLines=Boolean.valueOf(r.getPlotLines());
          this.drawShapes=Boolean.valueOf(r.getBaseShapesVisible());
        }
      }
    }
    setLayout(new BorderLayout());
    this.availableStrokeSamples=new StrokeSample[4];
    this.availableStrokeSamples[0]=new StrokeSample(null);
    this.availableStrokeSamples[1]=new StrokeSample(new BasicStroke(1.0f));
    this.availableStrokeSamples[2]=new StrokeSample(new BasicStroke(2.0f));
    this.availableStrokeSamples[3]=new StrokeSample(new BasicStroke(3.0f));
    JPanel panel=new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),plot.getPlotType() + localizationResources.getString(":")));
    JPanel general=new JPanel(new BorderLayout());
    general.setBorder(BorderFactory.createTitledBorder(localizationResources.getString("General")));
    JPanel interior=new JPanel(new LCBLayout(7));
    interior.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
    interior.add(new JLabel(localizationResources.getString("Outline_stroke")));
    JButton button=new JButton(localizationResources.getString("Select..."));
    button.setActionCommand("OutlineStroke");
    button.addActionListener(this);
    interior.add(this.outlineStrokeSample);
    interior.add(button);
    interior.add(new JLabel(localizationResources.getString("Outline_Paint")));
    button=new JButton(localizationResources.getString("Select..."));
    button.setActionCommand("OutlinePaint");
    button.addActionListener(this);
    interior.add(this.outlinePaintSample);
    interior.add(button);
    interior.add(new JLabel(localizationResources.getString("Background_paint")));
    button=new JButton(localizationResources.getString("Select..."));
    button.setActionCommand("BackgroundPaint");
    button.addActionListener(this);
    interior.add(this.backgroundPaintSample);
    interior.add(button);
    if (this.plotOrientation != null) {
      boolean isVertical=this.plotOrientation.equals(PlotOrientation.VERTICAL);
      int index=isVertical ? ORIENTATION_VERTICAL : ORIENTATION_HORIZONTAL;
      interior.add(new JLabel(localizationResources.getString("Orientation")));
      this.orientationCombo=new JComboBox(orientationNames);
      this.orientationCombo.setSelectedIndex(index);
      this.orientationCombo.setActionCommand("Orientation");
      this.orientationCombo.addActionListener(this);
      interior.add(new JPanel());
      interior.add(this.orientationCombo);
    }
    if (this.drawLines != null) {
      interior.add(new JLabel(localizationResources.getString("Draw_lines")));
      this.drawLinesCheckBox=new JCheckBox();
      this.drawLinesCheckBox.setSelected(this.drawLines.booleanValue());
      this.drawLinesCheckBox.setActionCommand("DrawLines");
      this.drawLinesCheckBox.addActionListener(this);
      interior.add(new JPanel());
      interior.add(this.drawLinesCheckBox);
    }
    if (this.drawShapes != null) {
      interior.add(new JLabel(localizationResources.getString("Draw_shapes")));
      this.drawShapesCheckBox=new JCheckBox();
      this.drawShapesCheckBox.setSelected(this.drawShapes.booleanValue());
      this.drawShapesCheckBox.setActionCommand("DrawShapes");
      this.drawShapesCheckBox.addActionListener(this);
      interior.add(new JPanel());
      interior.add(this.drawShapesCheckBox);
    }
    general.add(interior,BorderLayout.NORTH);
    JPanel appearance=new JPanel(new BorderLayout());
    appearance.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    appearance.add(general,BorderLayout.NORTH);
    JTabbedPane tabs=new JTabbedPane();
    tabs.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
    Axis domainAxis=null;
    if (plot instanceof CategoryPlot) {
      domainAxis=((CategoryPlot)plot).getDomainAxis();
    }
 else {
      if (plot instanceof XYPlot) {
        domainAxis=((XYPlot)plot).getDomainAxis();
      }
    }
    this.domainAxisPropertyPanel=DefaultAxisEditor.getInstance(domainAxis);
    if (this.domainAxisPropertyPanel != null) {
      this.domainAxisPropertyPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
      tabs.add(localizationResources.getString("Domain_Axis"),this.domainAxisPropertyPanel);
    }
    Axis rangeAxis=null;
    if (plot instanceof CategoryPlot) {
      rangeAxis=((CategoryPlot)plot).getRangeAxis();
    }
 else {
      if (plot instanceof XYPlot) {
        rangeAxis=((XYPlot)plot).getRangeAxis();
      }
    }
    this.rangeAxisPropertyPanel=DefaultAxisEditor.getInstance(rangeAxis);
    if (this.rangeAxisPropertyPanel != null) {
      this.rangeAxisPropertyPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
      tabs.add(localizationResources.getString("Range_Axis"),this.rangeAxisPropertyPanel);
    }
    tabs.add(localizationResources.getString("Appearance"),appearance);
    panel.add(tabs);
    add(panel);
  }
  /** 
 * Returns the current plot insets.
 * @return The current plot insets.
 */
  public RectangleInsets getPlotInsets(){
    if (this.plotInsets == null) {
      this.plotInsets=new RectangleInsets(0.0,0.0,0.0,0.0);
    }
    return this.plotInsets;
  }
  /** 
 * Returns the current background paint.
 * @return The current background paint.
 */
  public Paint getBackgroundPaint(){
    return this.backgroundPaintSample.getPaint();
  }
  /** 
 * Returns the current outline stroke.
 * @return The current outline stroke (possibly <code>null</code>).
 */
  public Stroke getOutlineStroke(){
    return this.outlineStrokeSample.getStroke();
  }
  /** 
 * Returns the current outline paint.
 * @return The current outline paint.
 */
  public Paint getOutlinePaint(){
    return this.outlinePaintSample.getPaint();
  }
  /** 
 * Returns a reference to the panel for editing the properties of the domain axis.
 * @return A reference to a panel.
 */
  public DefaultAxisEditor getDomainAxisPropertyEditPanel(){
    return this.domainAxisPropertyPanel;
  }
  /** 
 * Returns a reference to the panel for editing the properties of the range axis.
 * @return A reference to a panel.
 */
  public DefaultAxisEditor getRangeAxisPropertyEditPanel(){
    return this.rangeAxisPropertyPanel;
  }
  /** 
 * Handles user actions generated within the panel.
 * @param event     the event
 */
  public void actionPerformed(  ActionEvent event){
    String command=event.getActionCommand();
    if (command.equals("BackgroundPaint")) {
      attemptBackgroundPaintSelection();
    }
 else {
      if (command.equals("OutlineStroke")) {
        attemptOutlineStrokeSelection();
      }
 else {
        if (command.equals("OutlinePaint")) {
          attemptOutlinePaintSelection();
        }
 else {
          if (command.equals("Orientation")) {
            attemptOrientationSelection();
          }
 else {
            if (command.equals("DrawLines")) {
              attemptDrawLinesSelection();
            }
 else {
              if (command.equals("DrawShapes")) {
                attemptDrawShapesSelection();
              }
            }
          }
        }
      }
    }
  }
  /** 
 * Allow the user to change the background paint.
 */
  private void attemptBackgroundPaintSelection(){
    Color c;
    c=JColorChooser.showDialog(this,localizationResources.getString("Background_Color"),Color.blue);
    if (c != null) {
      this.backgroundPaintSample.setPaint(c);
    }
  }
  /** 
 * Allow the user to change the outline stroke.
 */
  private void attemptOutlineStrokeSelection(){
    StrokeChooserPanel panel=new StrokeChooserPanel(this.outlineStrokeSample,this.availableStrokeSamples);
    int result=JOptionPane.showConfirmDialog(this,panel,localizationResources.getString("Stroke_Selection"),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      this.outlineStrokeSample.setStroke(panel.getSelectedStroke());
    }
  }
  /** 
 * Allow the user to change the outline paint.  We use JColorChooser, so the user can only choose colors (a subset of all possible paints).
 */
  private void attemptOutlinePaintSelection(){
    Color c;
    c=JColorChooser.showDialog(this,localizationResources.getString("Outline_Color"),Color.blue);
    if (c != null) {
      this.outlinePaintSample.setPaint(c);
    }
  }
  /** 
 * Allow the user to modify the plot orientation if this is an editor for a <tt>CategoryPlot</tt> or a <tt>XYPlot</tt>.
 */
  private void attemptOrientationSelection(){
    int index=this.orientationCombo.getSelectedIndex();
    if (index == ORIENTATION_VERTICAL) {
      this.plotOrientation=PlotOrientation.VERTICAL;
    }
 else {
      this.plotOrientation=PlotOrientation.HORIZONTAL;
    }
  }
  /** 
 * Allow the user to modify whether or not lines are drawn between data points by <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s.
 */
  private void attemptDrawLinesSelection(){
    this.drawLines=Boolean.valueOf(this.drawLinesCheckBox.isSelected());
  }
  /** 
 * Allow the user to modify whether or not shapes are drawn at data points by <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s.
 */
  private void attemptDrawShapesSelection(){
    this.drawShapes=Boolean.valueOf(this.drawShapesCheckBox.isSelected());
  }
  /** 
 * Updates the plot properties to match the properties defined on the panel.
 * @param plot  The plot.
 */
  public void updatePlotProperties(  Plot plot){
    plot.setOutlinePaint(getOutlinePaint());
    plot.setOutlineStroke(getOutlineStroke());
    plot.setBackgroundPaint(getBackgroundPaint());
    plot.setInsets(getPlotInsets());
    if (this.domainAxisPropertyPanel != null) {
      Axis domainAxis=null;
      if (plot instanceof CategoryPlot) {
        CategoryPlot p=(CategoryPlot)plot;
        domainAxis=p.getDomainAxis();
      }
 else {
        if (plot instanceof XYPlot) {
          XYPlot p=(XYPlot)plot;
          domainAxis=p.getDomainAxis();
        }
      }
      if (domainAxis != null) {
        this.domainAxisPropertyPanel.setAxisProperties(domainAxis);
      }
    }
    if (this.rangeAxisPropertyPanel != null) {
      Axis rangeAxis=null;
      if (plot instanceof CategoryPlot) {
        CategoryPlot p=(CategoryPlot)plot;
        rangeAxis=p.getRangeAxis();
      }
 else {
        if (plot instanceof XYPlot) {
          XYPlot p=(XYPlot)plot;
          rangeAxis=p.getRangeAxis();
        }
      }
      if (rangeAxis != null) {
        this.rangeAxisPropertyPanel.setAxisProperties(rangeAxis);
      }
    }
    if (this.plotOrientation != null) {
      if (plot instanceof CategoryPlot) {
        CategoryPlot p=(CategoryPlot)plot;
        p.setOrientation(this.plotOrientation);
      }
 else {
        if (plot instanceof XYPlot) {
          XYPlot p=(XYPlot)plot;
          p.setOrientation(this.plotOrientation);
        }
      }
    }
    if (this.drawLines != null) {
      if (plot instanceof CategoryPlot) {
        CategoryPlot p=(CategoryPlot)plot;
        CategoryItemRenderer r=p.getRenderer();
        if (r instanceof LineAndShapeRenderer) {
          ((LineAndShapeRenderer)r).setBaseLinesVisible(this.drawLines.booleanValue());
        }
      }
 else {
        if (plot instanceof XYPlot) {
          XYPlot p=(XYPlot)plot;
          XYItemRenderer r=p.getRenderer();
          if (r instanceof StandardXYItemRenderer) {
            ((StandardXYItemRenderer)r).setPlotLines(this.drawLines.booleanValue());
          }
        }
      }
    }
    if (this.drawShapes != null) {
      if (plot instanceof CategoryPlot) {
        CategoryPlot p=(CategoryPlot)plot;
        CategoryItemRenderer r=p.getRenderer();
        if (r instanceof LineAndShapeRenderer) {
          ((LineAndShapeRenderer)r).setBaseShapesVisible(this.drawShapes.booleanValue());
        }
      }
 else {
        if (plot instanceof XYPlot) {
          XYPlot p=(XYPlot)plot;
          XYItemRenderer r=p.getRenderer();
          if (r instanceof StandardXYItemRenderer) {
            ((StandardXYItemRenderer)r).setBaseShapesVisible(this.drawShapes.booleanValue());
          }
        }
      }
    }
  }
}
