/*
 * $Id: PainterVisualCheck.java,v 1.23 2007/08/24 11:19:01 kleopatra Exp $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate.ColumnHighlightPredicate;
import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.ShapePainter;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.test.AncientSwingTeam;

/**
 * Experiments with highlighters using painters.<p>
 * 
 * Links
 * <ul>
 * <li> <a href="">Sneak preview II - Transparent LegacyHighlighter</a>
 * </ul>
 * 
 * 
 * @author Jeanette Winzenburg
 */
public class PainterVisualCheck extends InteractiveTestCase {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger
            .getLogger(PainterVisualCheck.class.getName());
    public static void main(String args[]) {
//      setSystemLF(true);
      PainterVisualCheck test = new PainterVisualCheck();
      try {
        test.runInteractiveTests();
//         test.runInteractiveTests(".*Label.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
    
    public void interactiveTriangleRenderer() {
        JXTable table = new JXTable(new AncientSwingTeam());
        ShapePainter painter = new ShapePainter();
        Shape polygon = new Polygon(new int[] { 0, 5, 5 },
                new int[] { 0, 0, 5 }, 3);
        painter.setShape(polygon);
        painter.setFillPaint(Color.RED);
        painter.setStyle(ShapePainter.Style.FILLED);
        painter.setPaintStretched(false);
        // hmm.. how to make this stick to the trailing upper corner?
        painter.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        painter.setVerticalAlignment(VerticalAlignment.TOP);
        Highlighter hl = new PainterHighlighter(painter, new ColumnHighlightPredicate(3)); 
        table.addHighlighter(hl);
        showWithScrollingInFrame(table, "Renderer with Triangle marker");
    }

    /**
     * Use Painter for an underline-rollover effect.
     */
    public void interactiveRolloverPainter() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        MattePainter<JComponent> matte = new MattePainter<JComponent>(getTransparentColor(Color.RED, 80));
        RelativePainter painter = new RelativePainter<JComponent>(matte);
        painter.setYFactor(0.2);
        painter.setVerticalAlignment(VerticalAlignment.BOTTOM);
        Highlighter hl = new PainterHighlighter(painter, HighlightPredicate.ROLLOVER_ROW);
        table.addHighlighter(hl);
        JXFrame frame = showWithScrollingInFrame(table, 
                "painter-aware renderer rollover");
        getStatusBar(frame).add(new JLabel("gradient background of cells with value's containing 'y'"));
    }

    /**
     * Creates and returns a predicate for filtering labels whose text
     * property contains the given text.
     * @return 
     */
    private HighlightPredicate createComponentTextBasedPredicate(final String substring) {
        HighlightPredicate predicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (!(renderer instanceof JLabel)) return false;
                String text = ((JLabel) renderer).getText();
                 return text.contains(substring);
            }
            
        };
        return predicate;
    }
   
    /**
     * Use ?? for fixed portion background highlighting
     * Use SwingX extended default renderer.
     */
    public void interactiveTableBarHighlight() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        MattePainter<JComponent> p =  new MattePainter<JComponent>(getTransparentColor(Color.BLUE, 125));
        RelativePainter relativePainter = new RelativePainter<JComponent>(p);
        relativePainter.setXFactor(.5);
        Highlighter hl = new PainterHighlighter(relativePainter, createComponentTextBasedPredicate("y"));
        table.addHighlighter(hl);
        JXFrame frame = showWithScrollingInFrame(table, 
                "painter-aware renderer with value-based highlighting");
        getStatusBar(frame).add(new JLabel("gradient background of cells with value's containing 'y'"));
    }
   

//------------------------ Transparent painter aware button as rendering component
    
    /**
     * Use a custom button controller to show both checkbox icon and text to
     * render Actions in a JXList. Apply striping and a simple gradient highlighter.
     */
    public void interactiveTableWithListColumnControl() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        JXList list = new JXList();
        Highlighter highlighter = HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER);
        table.addHighlighter(highlighter);
        Painter gradient = createGradientPainter(Color.YELLOW, .7f, true);
        list.setHighlighters(highlighter, new PainterHighlighter(gradient));
        // quick-fill and hook to table columns' visibility state
        configureList(list, table, false);
        // a custom rendering button controller showing both checkbox and text
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof AbstractActionExt) {
                    return ((AbstractActionExt) value).getName();
                }
                return "";
            }
            
        };
        BooleanValue bv = new BooleanValue() {

            public boolean getBoolean(Object value) {
                if (value instanceof AbstractActionExt) {
                    return ((AbstractActionExt) value).isSelected();
                }
                return false;
            }
            
        };
        ButtonProvider wrapper = new ButtonProvider(new MappedValue(sv, null, bv), JLabel.LEADING);
        list.setCellRenderer(new DefaultListRenderer(wrapper));
        JXFrame frame = showWithScrollingInFrame(table, list,
                "checkbox list-renderer - striping and gradient");
        addStatusMessage(frame, "fake editable list: space/doubleclick on selected item toggles column visibility");
        frame.pack();
    }


    /**
     * Creates and returns a Painter with a gradient paint starting with
     * startColor to WHITE.
     * 
     * @param startColor
     * @param percentage
     * @param transparent
     * @return
     */
    protected Painter createGradientPainter(Color startColor, float end,
            boolean transparent) {
        startColor = getTransparentColor(startColor, transparent ? 125 : 254);
        Color endColor = getTransparentColor(Color.WHITE, 0);
        GradientPaint paint = new GradientPaint(
                    new Point2D.Double(0, 0),
                    startColor,
                   new Point2D.Double(1000, 0),
                   endColor);

        MattePainter<JComponent> painter = new MattePainter<JComponent>(paint);
        painter.setPaintStretched(true);
        // not entirely successful - the relative stretching is on
        // top of a .5 stretched gradient in matte
        RelativePainter wrapper = new RelativePainter<JComponent>(painter);
        wrapper.setXFactor(end);
        return wrapper;
    }

    private static Color getTransparentColor(Color base, int transparency) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(),
                transparency);
    }
    // ------------------------
    /**
     * Use highlighter with background image painter. Shared by table and list.
     */
    public void interactiveIconPainterHighlight() throws Exception {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        ComponentProvider<JLabel> controller = new LabelProvider(
                JLabel.RIGHT);
        table.getColumn(0).setCellRenderer(
                new DefaultTableRenderer(controller));
        final ImagePainter imagePainter = new ImagePainter(ImageIO.read(JXPanel.class
                .getResource("resources/images/kleopatra.jpg")));
        HighlightPredicate predicate = new ColumnHighlightPredicate(0);
        Highlighter iconHighlighter = new PainterHighlighter(imagePainter, predicate );
        Highlighter alternateRowHighlighter = HighlighterFactory.createSimpleStriping();
        table.addHighlighter(alternateRowHighlighter);
        table.addHighlighter(iconHighlighter);
        // re-use component controller and highlighter in a JXList
        JXList list = new JXList(createListNumberModel(), true);
        list.setCellRenderer(new DefaultListRenderer(controller));
        list.addHighlighter(alternateRowHighlighter);
        list.addHighlighter(iconHighlighter);
        list.toggleSortOrder();
        final JXFrame frame = showWithScrollingInFrame(table, list,
                "image highlighting plus striping");
        frame.pack();
    }
  
//  ----------------- Transparent gradient on default (swingx) rendering label

    
    /**
     * Use transparent gradient painter for value-based background highlighting
     * with SwingX extended default renderer. Shared by table and list with
     * striping.
     */
    public void interactiveNumberProportionalGradientHighlightPlusStriping() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        ComponentProvider<JLabel> controller = new LabelProvider(
                JLabel.RIGHT) ;
        final ValueBasedGradientHighlighter gradientHighlighter = createTransparentGradientHighlighter();
        Highlighter alternateRowHighlighter = HighlighterFactory.createSimpleStriping();
        table.addHighlighter(alternateRowHighlighter);
        table.addHighlighter(gradientHighlighter);
        // re-use component controller and highlighter in a JXList
        JXList list = new JXList(createListNumberModel(), true);
        list.setCellRenderer(new DefaultListRenderer(controller));
        list.addHighlighter(alternateRowHighlighter);
        list.addHighlighter(gradientHighlighter);
        list.toggleSortOrder();
        final JXFrame frame = showWithScrollingInFrame(table, list,
                "transparent value relative highlighting plus striping");
        addStatusMessage(frame,
                "uses a PainterAwareLabel in renderer");
        // crude binding to play with options - the factory is incomplete...
        getStatusBar(frame).add(createTransparencyToggle(gradientHighlighter));
        frame.pack();
    }

    /**
     * Use transparent gradient painter for value-based background highlighting
     * with SwingX extended default renderer. Shared by table and list with
     * background color.
     */
    @SuppressWarnings("deprecation")
    public void interactiveNumberProportionalGradientHighlight() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        table.setBackground(HighlighterFactory.LEDGER);
        ComponentProvider<JLabel> controller = new LabelProvider(
                JLabel.RIGHT);
//        table.setDefaultRenderer(Number.class, new DefaultTableRenderer(
//                controller));
        ValueBasedGradientHighlighter gradientHighlighter = createTransparentGradientHighlighter();
        table.addHighlighter(gradientHighlighter);
        // re-use component controller and highlighter in a JXList
        JXList list = new JXList(createListNumberModel(), true);
        list.setBackground(table.getBackground());
        list.setCellRenderer(new DefaultListRenderer(controller));
        list.addHighlighter(gradientHighlighter);
        list.toggleSortOrder();
        JXFrame frame = showWithScrollingInFrame(table, list,
                "transparent value relative highlighting");
        addStatusMessage(frame,
                "uses the default painter-aware label in renderer");
        // crude binding to play with options - the factory is incomplete...
        getStatusBar(frame).add(createTransparencyToggle(gradientHighlighter));
        frame.pack();
    }

    //--------- hack around missing size proportional painters
    
    public static class RelativePainter<T> extends AbstractLayoutPainter<T> {

        private Painter<T> painter;
        private double xFactor;
        private double yFactor;

        public RelativePainter(Painter<T> delegate) {
            this.painter = delegate;
        }
        
        public void setXFactor(double xPercent) {
            this.xFactor = xPercent;
        }
        
        public void setYFactor(double yPercent) {
            this.yFactor = yPercent;
        }
        @Override
        protected void doPaint(Graphics2D g, T object, int width, int height) {
            // use epsilon
            if (xFactor != 0.0) {
                width = (int) (xFactor * width);
            }
            if (yFactor != 0.0) {
                int oldHeight = height;
                height = (int) (yFactor * height);
                if (getVerticalAlignment() == VerticalAlignment.BOTTOM) {
                    g.translate(0, oldHeight - height);
                }
            }
            
            painter.paint(g, object, width, height);
        }
        
    }
    // -------------------- Value-based transparent gradient highlighter

    /**
     * A LegacyHighlighter which applies a value-proportional gradient to PainterAware
     * rendering components if the value is a Number. The gradient is a simple
     * yellow to white-transparent paint. The yellow can be toggled to
     * half-transparent.<p>
     * 
     * PENDING: How to the same but not use a gradient but a solid colered bar,
     * covering a relative portion of the comp?
     */
    public static class ValueBasedGradientHighlighter extends
            PainterHighlighter {
        float maxValue = 100;

        private MattePainter<JComponent> painter;
        private RelativePainter<JComponent> wrapper;
        private boolean yellowTransparent;

        
        @Override
        protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
            if (adapter.getValue() instanceof Number) {
                float end = getEndOfGradient((Number) adapter.getValue());
                if (end > 1) {
                    renderer.setBackground(Color.YELLOW.darker());
                } else if (end > 0.02) {
                    Painter painter = getPainter(end);
                    ((PainterAware) renderer).setPainter(painter);
                }
            }            
            return renderer;
        }

        /**
         * @param yellowTransparent
         */
        public void setYellowTransparent(boolean yellowTransparent) {
            if (this.yellowTransparent == yellowTransparent) return;
            this.yellowTransparent = yellowTransparent;
            fireStateChanged();
        }


        private Painter getPainter(float end) {
            if (painter == null) {
                Color startColor = getTransparentColor(Color.YELLOW,
                        yellowTransparent ? 125 : 254);
                Color endColor = getTransparentColor(Color.WHITE, 0);
                GradientPaint paint = new GradientPaint(new Point2D.Double(0, 0),
                        startColor, new Point2D.Double(100, 0), endColor);
                // LinearGradientPaint paint = new LinearGradientPaint(0.0f, 0.0f,
                // 1f, 0f,
                // new float[] {0,end}, new Color[] {startColor
                // , endColor});
                painter = new MattePainter<JComponent>(paint);
                painter.setPaintStretched(true);
                wrapper = new RelativePainter<JComponent>(painter);
            } 
            wrapper.setXFactor(end);
            return wrapper;
        }

        private Color getTransparentColor(Color base, int transparency) {
            return new Color(base.getRed(), base.getGreen(), base.getBlue(),
                    transparency);
        }

        private float getEndOfGradient(Number number) {
            float end = number.floatValue() / maxValue;
            return end;
        }


    }
    /**
     * creates and returns a highlighter with a value-based transparent gradient
     * if the cell content type is a Number.
     * 
     * @return
     */
    private ValueBasedGradientHighlighter createTransparentGradientHighlighter() {
        return new ValueBasedGradientHighlighter();
    }

    /**
     * Creates and returns a checkbox to toggle the gradient's yellow
     * transparency.
     * 
     * @param gradientHighlighter
     * @return
     */
    private JCheckBox createTransparencyToggle(
            final ValueBasedGradientHighlighter gradientHighlighter) {
        ActionContainerFactory factory = new ActionContainerFactory();
        // toggle opaque optimatization
        AbstractActionExt toggleTransparent = new AbstractActionExt(
                "yellow transparent") {

            public void actionPerformed(ActionEvent e) {
                gradientHighlighter.setYellowTransparent(isSelected());
            }

        };
        toggleTransparent.setStateAction();
        JCheckBox box = new JCheckBox();
        factory.configureButton(box, toggleTransparent, null);
        return box;
    }

//----------------- Utility    
    /**
     * 
     * @return a ListModel wrapped around the AncientSwingTeam's Number column.
     */
    private ListModel createListNumberModel() {
        AncientSwingTeam tableModel = new AncientSwingTeam();
        int colorColumn = 3;
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            model.addElement(tableModel.getValueAt(i, colorColumn));
        }
        return model;
    }

    /**
     * Fills the list with a collection of actions (as returned from the 
     * table's column control). Binds space and double-click to toggle
     * the action's selected state.
     * 
     * note: this is just an example to show-off the button renderer in a list!
     * ... it's very dirty!!
     * 
     * @param list
     * @param table
     */
    private void configureList(final JXList list, final JXTable table, boolean useRollover) {
        final List<Action> actions = new ArrayList<Action>();
        @SuppressWarnings("all")
        ColumnControlButton columnControl = new ColumnControlButton(table, null) {

            @Override
            protected void addVisibilityActionItems() {
                actions.addAll(Collections
                        .unmodifiableList(getColumnVisibilityActions()));
            }

        };
        list.setModel(createListeningListModel(actions));
        // action toggling selected state of selected list item
        final Action toggleSelected = new AbstractActionExt(
                "toggle column visibility") {

            public void actionPerformed(ActionEvent e) {
                if (list.isSelectionEmpty())
                    return;
                AbstractActionExt selectedItem = (AbstractActionExt) list
                        .getSelectedValue();
                selectedItem.setSelected(!selectedItem.isSelected());
            }

        };
        if (useRollover) {
            list.setRolloverEnabled(true);
        } else {
            // bind action to space
            list.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                    "toggleSelectedActionState");
        }
        list.getActionMap().put("toggleSelectedActionState", toggleSelected);
        // bind action to double-click
        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    toggleSelected.actionPerformed(null);
                }
            }

        };
        list.addMouseListener(adapter);

    }

    /**
     * Creates and returns a ListModel containing the given actions. 
     * Registers a PropertyChangeListener with each action to get
     * notified and fire ListEvents.
     * 
     * @param actions the actions to add into the model.
     * @return the filled model.
     */
    private ListModel createListeningListModel(final List<Action> actions) {
        final DefaultListModel model = new DefaultListModel() {

            DefaultListModel reallyThis = this;
            @Override
            public void addElement(Object obj) {
                super.addElement(obj);
                ((Action) obj).addPropertyChangeListener(l);
                
            }
            
            PropertyChangeListener l = new PropertyChangeListener() {
                
                public void propertyChange(PropertyChangeEvent evt) {
                    int index = indexOf(evt.getSource());
                    if (index >= 0) {
                        fireContentsChanged(reallyThis, index, index);
                    }
                }
                
            };
        };
        for (Action action : actions) {
            model.addElement(action);
        }
        return model;
    }

    /**
     * do-nothing method - suppress warning if there are no other
     * test fixtures to run.
     *
     */
    public void testDummy() {
        
    }

}
