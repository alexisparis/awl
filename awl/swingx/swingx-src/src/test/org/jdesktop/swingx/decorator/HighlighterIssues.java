/*
 * Created on 14.10.2005
 *
 */
package org.jdesktop.swingx.decorator;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXEditorPaneTest;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.LinkModel;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.HighlighterFactory.UIColorHighlighter;
import org.jdesktop.test.AncientSwingTeam;


public class HighlighterIssues extends HighlighterTest {
    private static final Logger LOG = Logger.getLogger(HighlighterIssues.class
            .getName());
    
    protected Color ledger = new Color(0xF5, 0xFF, 0xF5);
//    protected boolean systemLF;
    
    public static void main(String args[]) {
//        setSystemLF(true);
        HighlighterIssues test = new HighlighterIssues();
        try {
           test.runInteractiveTests(".*UITable.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }
    
    //---------------- uidependent
    

    /**
     * something's wrong with the testing: temporarily setting the
     * UIManager striping color here blows the tests below. 
     * 
     *
     */
    public void testCustomUIColorHighlighter() {
        UIColorHighlighter h = new UIColorHighlighter();
        Color uiBackground = h.getBackground();
        Color uiColor = UIManager.getColor("UIColorHighlighter.stripingBackground");
        // very unusual ui striping
        Color color = new ColorUIResource(Color.BLACK);
        if (color.equals(uiBackground)) {
            LOG.info("cannot run testUIColorHighlight - ui striping same as test color");
            return;
        }
        UIManager.put("UIColorHighlighter.stripingBackground", color);
        try {
            h.updateUI();
            assertEquals(color, h.getBackground());
        } finally {
            LOG.info("finally?");
            UIManager.put("UIColorHighlighter.stripingBackground", uiColor);
        }
        // sanity - reset
        h.updateUI();
        assertEquals(uiColor, h.getBackground());
    }

    /**
     * test if background changes with LF.
     * 
     * PENDING: this is not entirely correct, might fail because
     *   both LFs fall back to GenericGray.
     */
    public void testLookupUIColor() {
        UIColorHighlighter hl = new UIColorHighlighter();
        Color color = hl.getBackground();
        String lf = UIManager.getLookAndFeel().getName();
        Color uiColor = UIManager.getColor("UIColorHighlighter.stripingBackground");
        assertNotNull(uiColor);
        // switch LF
        setSystemLF(!defaultToSystemLF);
        if (lf.equals(UIManager.getLookAndFeel().getName())) {
            LOG.info("cannot run lookupUIColor - same LF" + lf);
            return;
        }
        Color uiColor2 = UIManager.getColor("UIColorHighlighter.stripingBackground");
        assertNotNull(uiColor2);
        // hmm ... how to force the reloading in the addon?
        LOG.info("color must be different " + uiColor + "/" + uiColor2);
        assertFalse("color must be different " + uiColor + "/" + uiColor2 , uiColor2.equals(uiColor));
        hl.updateUI();
        assertFalse("highlighter background must be changed", 
                color.equals(hl.getBackground()));
    }
    /**
     * test if background changes with LF.
     * 
     * PENDING: this is not entirely correct, might fail because
     *   both LFs fall back to GenericGray.
     */
    public void testLookupUIColorInCompound() {
        UIColorHighlighter hl = new UIColorHighlighter();
        Color color = hl.getBackground();
        CompoundHighlighter compound = new CompoundHighlighter(hl);
        String lf = UIManager.getLookAndFeel().getName();
        // switch LF
        setSystemLF(!defaultToSystemLF);
        if (lf.equals(UIManager.getLookAndFeel().getName())) {
            LOG.info("cannot run lookupUIColor - same LF" + lf);
            return;
        }
        compound.updateUI();
        assertFalse("highlighter background must be changed", 
                color.equals(hl.getBackground()));
    }

    //---------------
    
    /**
     * Issue #258-swingx: Background LegacyHighlighter must not change custom
     * foreground.
     * <p>
     * 
     * Visualizing effect of hack: table-internally, a ResetDTCRColorHighlighter
     * tries to neutralize DefaultTableCellRenderer's color memory.
     * 
     * <ul>
     * <li> a DTCR subclass with value-based custom foreground
     * <li> the renderer is shared between a table with background highlighter
     * (alternateRowHighlighter) and a table without highlighter
     * <li> the custom value-based foreground must show in both
     * (AlternateRowHighlighter overwrite both striped and unstriped back)
     * </ul>
     * 
     * This behaves as expected after moving the hack to _before_ calling 
     * super.prepareRenderer.
     */
    public void interactiveTableCustomCoreRendererColorBasedOnValue() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                // TODO Auto-generated method stub
                super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
                if (!isSelected) {
                    if (getText().contains("y")) {
                        setForeground(Color.RED);
                    } else {
                        setForeground(Color.GREEN);
                    }
                }
                return this;
            }

        };
        table.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));

        table.setDefaultRenderer(Object.class, renderer);
        JXTable nohighlight = new JXTable(model);
        nohighlight.setDefaultRenderer(Object.class, renderer);
        showWithScrollingInFrame(table, nohighlight,
                "core - value-based fg renderer with bg highlighter <--> shared without highl");
    }
    

    
    /**
     * Issue #258-swingx: Background LegacyHighlighter must not change custom
     * foreground.
     * <p>
     * 
     * Visualizing effect of hack: table-internally, a ResetDTCRColorHighlighter
     * tries to neutralize DefaultTableCellRenderer's color memory.
     * 
     * <ul>
     * <li> a DTCR with custom foreground and custom background
     * <li> the renderer is shared between a table with background highlighter
     * (alternateRowHighlighter) and a table without highlighter
     * <li> the custom foreground must show in both
     * <li> the custom background must show in the table without highlighter
     * <li> the custom background must not show in the table with highlighter
     * (AlternateRowHighlighter overwrite both striped and unstriped back)
     * </ul>
     * 
     */
    public void interactiveTableCustomCoreRendererColor() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(foreground);
        renderer.setBackground(background);
        table.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));

        table.setDefaultRenderer(Object.class, renderer);
        JXTable nohighlight = new JXTable(model);
        nohighlight.setDefaultRenderer(Object.class, renderer);
        showWithScrollingInFrame(table, nohighlight,
                "core: custom colored renderer with bg highlighter <--> shared without highl");
    }
    
    
    /**
     * Issue #258-swingx: Background LegacyHighlighter must not change custom
     * foreground.
     * <p>
     * 
     * Visualizing effect of hack: table-internally, a ResetDTCRColorHighlighter
     * tries to neutralize DefaultTableCellRenderer's color memory.
     * 
     * Problem: if pre-hack-highlighting and selected on first rendering,
     * the renderer always uses the selection color. Bug in hacking highlighter?
     * Not much it can do about it: the hacking does not store the color if
     * the adapter isSelected - so in the next round the previously configured
     * color - which was for selected state - is stored as to color to restore to.
     * <p>
     * Arrggghhh...
     * 
     */
    public void interactiveTableCoreRendererFirstSelected() {
        TableModel model = new AncientSwingTeam();
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        JXTable table = new JXTable(model);
//        table.addHighlighter(AlternateRowHighlighter.genericGrey);
//        table.setDefaultRenderer(Object.class, renderer);
        JXTable nohighlight = new JXTable(model);
        nohighlight.setDefaultRenderer(Object.class, renderer);
        nohighlight.setRowSelectionInterval(0, 0);
//        showWithScrollingInFrame(table, nohighlight,
        showWithScrollingInFrame(nohighlight,
                "core: very first match is against selected");
    }
    
    
    /**
     * UIHighlighter: check if highlighter is updated when toggling LF.
     */
    public void interactiveUITableWithAlternateRow() {
        JXTable table = new JXTable(10, 2);
        table.setBackground(ledger);
        table.setHighlighters(HighlighterFactory.createSimpleStriping());
        JXTable nohighlight = new JXTable(10, 2);
        nohighlight.setBackground(ledger);
        final JXFrame frame = wrapWithScrollingInFrame(table, nohighlight, "colored table with ui highlighter <--> without highlighter");
        Action action = new AbstractActionExt("toggle LF") {
            boolean systemLF = defaultToSystemLF;
            public void actionPerformed(ActionEvent e) {
                systemLF = !systemLF;
                setSystemLF(systemLF);
                SwingUtilities.updateComponentTreeUI(frame);
                
            }
            
        };
        addAction(frame, action);
        frame.setVisible(true);
    }

    /**
     * Effect of background highlighters on table with custom background.
     * 
     */
    public void interactiveColoredTableWithAlternateRow() {
        JXTable table = new JXTable(10, 2);
        table.setBackground(ledger);
        table.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));

        JXTable nohighlight = new JXTable(10, 2);
        nohighlight.setBackground(ledger);
        JXFrame frame = wrapWithScrollingInFrame(table, nohighlight, "colored table with bg highlighter <--> without highlighter");
        frame.setVisible(true);
    }
    
    /**
     * Effect of background highlighters on list with custom background.
     * 
     */
    public void interactiveColoredListWithAlternateRow() {
        JXList list = new JXList(createListModel());
        list.setBackground(ledger);
        list.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));

        JXList nohighlight = new JXList(createListModel());
        nohighlight.setBackground(ledger);
        showWithScrollingInFrame(list, nohighlight, "colored list with bg highlighter <--> without highlighter");
    }

    /**
     * 
     * Effect of background highlighters on tree with custom background. Note:
     * background highlighters don't work at all with DefaultTreeCellRenderers.
     */
    public void interactiveColoredTreeWithAlternateRow() {
        JXTree nohighlightTree = new JXTree();
        nohighlightTree.setBackground(ledger);
        JXTree tree = new JXTree();
        CompoundHighlighter pipeline = new CompoundHighlighter();
        pipeline.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));

        tree.setHighlighters(pipeline);
        tree.setBackground(ledger);
        showWithScrollingInFrame(tree, nohighlightTree, "colored tree with bg highlighter <--> without highlighter");
    }
    
    /**
     * Issue #258-swingx: DefaultTableCellRenderer has memory. 
     * How to formulate as test?
     * this is testing the hack (reset the memory in ResetDTCR to null), not
     * any highlighter!
     */
    public void testTableUnSelectedDoNothingHighlighter() {
        JXTable table = new JXTable(10, 2);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(foreground);
        table.setHighlighters(new ColorHighlighter());
        Component comp = table.prepareRenderer(renderer, 0, 0);
        assertEquals("do nothing highlighter must not change foreground", foreground, comp.getForeground());
        fail("testing the hack around DefaultTableCellRenderer memory - not the memory itself");
    }

    /**
     * Issue #178-swingx: Highlighters always change the selection color.
     * 
     */
    public void interactiveTableUnSelectedDoNothingHighlighter() {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(foreground);
        table.addHighlighter(emptyHighlighter);
        table.setDefaultRenderer(Object.class, renderer);
        showWithScrollingInFrame(table,  
                "foreground colored renderer with empty highlighter");
    }

    @SuppressWarnings("all")
    private TableModel createTableModelWithLinks() {
        String[] columnNames = { "text only", "Link editable", "Link not-editable", "Bool editable", "Bool not-editable" };
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                    return !getColumnName(column).contains("not");
            }
            
        };
        for (int i = 0; i < 4; i++) {
            try {
                LinkModel link = new LinkModel("a link text " + i, null, new URL("http://some.dummy.url" + i));
                if (i == 1) {
                    URL url = JXEditorPaneTest.class.getResource("resources/test.html");

                    link = new LinkModel("a link text " + i, null, url);
                }
                model.addRow(new Object[] {"text only " + i, link, link, Boolean.TRUE, Boolean.TRUE });
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return model;
    }

    
    /**
     * Issue #178-swingx: Highlighters always change the selection color.
     * Compare table with empty highlighter to table without highlighter.
     */
    public void interactiveTableWithDoNothingHighlighter() {
        JXTable table = new JXTable(10, 2);
        table.addHighlighter(emptyHighlighter);
        showWithScrollingInFrame(table, new JXTable(10, 2), "empty highlighter <--> no highlighter");
        
    }
    
    /**
     * Issue #178-swingx: Highlighters always change the selection color.
     * Compare list with empty highlighter to list without highlighter.
     */
    public void interactiveListWithDoNothingHighlighter() {
        JXList list = new JXList(createListModel());
        list.addHighlighter(emptyHighlighter);
        showWithScrollingInFrame(list, new JXList(createListModel()), "empty highlighter <--> no highlighter");
    }

    /**
     * Issue #178-swingx: Highlighters always change the selection color.
     * Compare tree with empty highlighter to tree without highlighter.
     */
    public void interactiveTreeWithDoNothingHighlighter() {
        JXTree tree = new JXTree();
        tree.setHighlighters(emptyHighlighter);
        showWithScrollingInFrame(tree, new JXTree(), "empty highlighter <--> no highlighter");
    }
    


    /**
     * Issue #178-swingx: Highlighters always change the selection color.
     * 
     * All three labels must have same background and foreground.
     */
    public void interactiveDoNothingHighlighter() {
        JComponent box = Box.createVerticalBox();
        allColored.setText("unhighlighted");
        allColored.setOpaque(true);
        box.add(allColored);
        JLabel label = new JLabel("highlighted - unselected");
        label.setBackground(allColored.getBackground());
        label.setForeground(allColored.getForeground());
        label.setOpaque(true);
        ComponentAdapter adapter = createComponentAdapter(label, false);
        emptyHighlighter.highlight(label, adapter);
        box.add(label);
        label = new JLabel("highlighted - selected");
        label.setBackground(allColored.getBackground());
        label.setForeground(allColored.getForeground());
        label.setOpaque(true);
        adapter = createComponentAdapter(label, true);
        emptyHighlighter.highlight(label, adapter);
        box.add(label);
        JXFrame frame = wrapInFrame(box, "labels with empty highlighter");
        frame.setVisible(true);
    }

//------------------ helpers
    
    private ListModel createListModel() {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < 10; i++) {
            model.add(i, i);
        }
        return model;
    }


}
