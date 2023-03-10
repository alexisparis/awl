/*
 * $Id: JXTreeTableUnitTest.java,v 1.52 2007/09/19 10:43:33 kleopatra Exp $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.test.ComponentTreeTableModel;
import org.jdesktop.swingx.test.TreeTableUtils;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.FileSystemModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TableModelReport;
import org.jdesktop.test.TreeSelectionReport;

public class JXTreeTableUnitTest extends InteractiveTestCase {
    private static final Logger LOG = Logger
            .getLogger(JXTreeTableUnitTest.class.getName());
    
    protected TreeTableModel treeTableModel;
    protected TreeTableModel simpleTreeTableModel;
    
    public JXTreeTableUnitTest() {
        super("JXTreeTable Unit Test");
    }
    /**
     * #561-swingx: KeyEvent on hierarchical column doesn't start editing.
     * 
     * Here: test method without event (delegates to event == null)
     */
    public void testEditCellAt() {
        JXTreeTable treeTable = prepareTreeTable(true);
        treeTable.expandAll();
        // sanity
        assertTrue(treeTable.isCellEditable(0, 0));
        assertTrue(treeTable.editCellAt(0, 0));
    }
    
    /**
     * #561-swingx: KeyEvent on hierarchical column doesn't start editing.
     * 
     * Here: test method taking the event
     */
    public void testEditCellAtByKeyEvent() {
        JXTreeTable treeTable = prepareTreeTable(true);
        treeTable.expandAll();
        // sanity
        assertTrue(treeTable.isCellEditable(0, 0));
        KeyEvent e = new KeyEvent(treeTable, KeyEvent.KEY_PRESSED, 0L,
                0, KeyEvent.VK_T, KeyEvent.CHAR_UNDEFINED);
        assertTrue("keyEvent must start editing", treeTable.editCellAt(0, 0, e));
    }
    
    /**
     * #561-swingx: KeyEvent on hierarchical column doesn't start editing.
     * 
     * Here: compare with plain table (in fact plain DefaultCellEditor)
     */
    public void testTableEditCellAtByKeyEvent() {
        JXTable table = new JXTable(10, 20);
        // sanity
        assertTrue(table.isCellEditable(0, 0));
        KeyEvent e = new KeyEvent(table, KeyEvent.KEY_PRESSED, 0L,
                0, KeyEvent.VK_T, KeyEvent.CHAR_UNDEFINED);
        assertTrue(table.editCellAt(0, 0, e));
    }

    /**
     * Issue #493-swingx: incorrect table events fired.
     * Issue #592-swingx: (no structureChanged table events) is a special
     *   case of the former.
     *    
     * Here: must fire structureChanged on setRoot(null).
     * fails - because the treeStructureChanged is mapped to a 
     * tableDataChanged.
     *
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventOnSetNullRoot() throws InterruptedException, InvocationTargetException {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        ((DefaultTreeTableModel) model).setRoot(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals("tableModel must have fired", 1, report.getEventCount());
                assertTrue("event type must be structureChanged " + TableModelReport.printEvent(report.getLastEvent()), 
                        report.isStructureChanged(report.getLastEvent()));
            }
        });        
        
    }
    
    /**
     * Issue #493-swingx: incorrect table events fired.
     * Issue #592-swingx: (no structureChanged table events) is a special
     *   case of the former.
     * 
     * Here: must fire structureChanged on setRoot(otherroot).
     * failed - because the treeStructureChanged is mapped to a 
     * tableDataChanged.
     * 
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventOnSetRoot() throws InterruptedException, InvocationTargetException {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        ((DefaultTreeTableModel) model).setRoot(new DefaultMutableTreeTableNode("other"));  
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals("tableModel must have fired", 1, report.getEventCount());
                assertTrue("event type must be structureChanged " + TableModelReport.printEvent(report.getLastEvent()), 
                        report.isStructureChanged(report.getLastEvent()));
            }
        });        
        
    }

    /**
     * Issue #493-swingx: incorrect table events fired.
     * Issue #592-swingx: (no structureChanged table events) is a special
     *   case of the former.
     * 
     * Here: must not fire structureChanged on reload(subtree).
     * failed - because the treeStructureChanged is mapped to a 
     * tableDataChanged.
     * 
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventOnReloadNonRoot() throws InterruptedException, InvocationTargetException {
        DefaultTreeTableModel model = new DefaultTreeTableModel(
                (TreeTableNode) createCustomTreeTableModelFromDefault().getRoot()) {

            /**
             * overridden to do nothing but fire a structureChange
             * on the node. This is a trick
             * for testing only!
             */
           @Override
            public void removeNodeFromParent(MutableTreeTableNode node) {
               TreePath treePath = new TreePath(getPathToRoot(node));
               modelSupport.fireTreeStructureChanged(treePath);
            }
    
        };
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        // second child of root
        MutableTreeTableNode node = (MutableTreeTableNode) model.getRoot().getChildAt(1);
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        model.removeNodeFromParent(node);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals("tableModel must have fired", 1, report.getEventCount());
                assertTrue("event type must be dataChanged " + TableModelReport.printEvent(report.getLastEvent()), 
                        report.isDataChanged(report.getLastEvent()));
            }
        });        
        
    }
    /**
     * Issue #493-swingx: incorrect table events fired.
     * Issue #592-swingx: (no structureChanged table events) is a special
     *   case of the former.
     * 
     * Here: must fire structureChanged on setModel.
     * This was okay, because the setTreeTableModel on the table is handled
     * directly (through tableChanged)
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventOnSetModel() throws InterruptedException, InvocationTargetException {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        table.setTreeTableModel(createCustomTreeTableModelFromDefault());  
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                LOG.info("sanity - did testTableEventOnSetModel run?");
                assertEquals("tableModel must have fired", 1, report.getEventCount());
                assertTrue("event type must be structureChanged " + TableModelReport.printEvent(report.getLastEvent()), 
                        report.isStructureChanged(report.getLastEvent()));
            }
        });        
        
    }


    /**
     * Issue #531-swingx: IllegalArgumentException on setModel.
     *
     */
    public void testSetModel() {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        JXTreeTable treeTable = new JXTreeTable(model);
        treeTable.setRootVisible(true);
        treeTable.setTreeTableModel(createCustomTreeTableModelFromDefault());
    }
    
    /**
     * Issue #531-swingx: IllegalArgumentException on setModel.
     *
     */
    public void testSetModelOnTree() {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        JXTree treeTable = new JXTree(model);
        treeTable.setRootVisible(true);
        treeTable.setModel(createCustomTreeTableModelFromDefault());
    }
    /**
     * Issue #531-swingx: IllegalArgumentException on setModel.
     *
     */
    public void testSetModelEmptyContructor() {
        JXTreeTable treeTable = new JXTreeTable();
        treeTable.setRootVisible(true);
        treeTable.setTreeTableModel(createCustomTreeTableModelFromDefault());
    }
    
    /**
     * Issue #493-swingx: JXTreeTable.TreeTableModelAdapter: Inconsistency
     * firing update.
     * 
     * Test update events after updating treeTableModel.
     * 
     * from tiberiu@dev.java.net
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventUpdateOnTreeTableModelSetValue() throws InterruptedException, InvocationTargetException {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        final int row = 6;
        // sanity
        assertEquals("sports", table.getValueAt(row, 0).toString());
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        model.setValueAt("games",
                table.getPathForRow(6).getLastPathComponent(), 0);   
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals("tableModel must have fired", 1, report.getEventCount());
                assertEquals("the event type must be update", 1, report.getUpdateEventCount());
                TableModelEvent event = report.getLastUpdateEvent();
                assertEquals("the updated row ", row, event.getFirstRow());
            }
        });        
    }

    /**
     * Issue #493-swingx: JXTreeTable.TreeTableModelAdapter: Inconsistency
     * firing update.
     * 
     * Test delete events after tree table model.
     * 
     * from tiberiu@dev.java.net
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventDeleteOnTreeTableModel() throws InterruptedException, InvocationTargetException {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();
        MutableTreeTableNode sportsNode = (MutableTreeTableNode) root.getChildAt(1);
        int childrenToDelete = sportsNode.getChildCount() - 1;
        
        for (int i = 0; i < childrenToDelete; i++) {
            MutableTreeTableNode firstChild = (MutableTreeTableNode) sportsNode.getChildAt(0);
            ((DefaultTreeTableModel) model).removeNodeFromParent(firstChild);
        }
        // sanity
        assertEquals(1, sportsNode.getChildCount());
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        final int row = 6;
        // sanity
        assertEquals("sports", table.getValueAt(row, 0).toString());
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        // remove the last child from sports node
        MutableTreeTableNode firstChild = (MutableTreeTableNode) sportsNode.getChildAt(0);
        ((DefaultTreeTableModel) model).removeNodeFromParent(firstChild);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals("tableModel must have fired exactly one event", 1, report.getEventCount());
                TableModelEvent event = report.getLastEvent();
                assertEquals("event type must be delete", TableModelEvent.DELETE, event.getType());
                assertEquals("the deleted row ", row + 1, event.getFirstRow());
            }
        });        
    }
    /**
     * Issue #493-swingx: JXTreeTable.TreeTableModelAdapter: Inconsistency
     * firing update.
     * 
     * Test update events after updating table.
     * 
     * from tiberiu@dev.java.net
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    public void testTableEventUpdateOnTreeTableSetValue() throws InterruptedException, InvocationTargetException {
        TreeTableModel model = createCustomTreeTableModelFromDefault();
        final JXTreeTable table = new JXTreeTable(model);
        table.setRootVisible(true);
        table.expandAll();
        final int row = 6;
        // sanity
        assertEquals("sports", table.getValueAt(row, 0).toString());
        assertTrue("cell must be editable at row " + row, table.getModel().isCellEditable(row, 0));
        final TableModelReport report = new TableModelReport();
        table.getModel().addTableModelListener(report);
        // doesn't fire or isn't detectable? 
        // Problem was: model was not-editable.
        table.setValueAt("games", row, 0);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                assertEquals("tableModel must have fired", 1, report.getEventCount());
                assertEquals("the event type must be update", 1, report.getUpdateEventCount());
                TableModelEvent event = report.getLastUpdateEvent();
                assertEquals("the updated row ", row, event.getFirstRow());
            }
        });        
    }


    /**
     * Issue #465-swingx: NPE if editing node with null icon.
     */
    public void testNPEEditingNullIcon() {
        JXTreeTable treeTable = new JXTreeTable(new ComponentTreeTableModel(new JXErrorPane()));
        treeTable.setEditable(true);
        treeTable.expandAll();
        treeTable.setOpenIcon(null);
        treeTable.setLeafIcon(null);
        treeTable.setClosedIcon(null);
        assertTrue(treeTable.isCellEditable(1, 0));
        treeTable.editCellAt(1, 0, null);
    }

    /**
     * Issue #??-swingx: ComponentAdapter not fully implemented - leaf always true.
     *
     */
    public void testComponentAdapterIsLeaf() {
        // build the test treeTableModel
        JPanel root = new JPanel();
        JLabel label = new JLabel();
        JPanel inner = new JPanel();
        // last row is leaf
        inner.add(label);
        // first row is folder
        root.add(inner);
        TreeTableModel model = new ComponentTreeTableModel(root);
        // sanity 
        assertTrue("label is leaf", model.isLeaf(label));
        JXTreeTable table = new JXTreeTable(model);
        table.expandAll();
        // sanity
        assertEquals("number of expanded rows", 2, table.getRowCount());
        
        // test leafness of last
        int lastRow = table.getRowCount() - 1;
        TreePath leafPath = table.getPathForRow(lastRow);
        assertEquals(label, leafPath.getLastPathComponent());
        assertEquals("adapter must report same leafness as model", 
                model.isLeaf(label), table.getComponentAdapter(lastRow, 0).isLeaf());
        // test folderness of first
        int firstRow = 0;
        TreePath folderPath = table.getPathForRow(firstRow);
        assertEquals(inner, folderPath.getLastPathComponent());
        assertEquals("adapter must report same leafness as model", 
                model.isLeaf(inner), table.getComponentAdapter(firstRow, 0).isLeaf());
    }
    
    /**
     * Issue #??-swingx: ComponentAdapter not fully implemented - expanded always true.
     *
     */
    public void testComponentAdapterIsExpanded() {
        // build the test treeTableModel
        JPanel root = new JPanel();
        JLabel label = new JLabel();
        JPanel inner = new JPanel();
        // last row is leaf
        inner.add(label);
        // first row is folder
        root.add(inner);
        TreeTableModel model = new ComponentTreeTableModel(root);
        // sanity 
        assertTrue("label is leaf", model.isLeaf(label));
        JXTreeTable table = new JXTreeTable(model);
        // sanity
        assertEquals("number of expanded rows", 1, table.getRowCount());
        
        // test folderness of first
        int firstRow = 0;
        TreePath folderPath = table.getPathForRow(firstRow);
        assertEquals(inner, folderPath.getLastPathComponent());
        assertEquals("adapter must report same expansion state as tree", table.isExpanded(firstRow), 
                table.getComponentAdapter(firstRow, 0).isExpanded());
    }
    

    /**
     * Issue #342-swingx: default margins in JXTreeTable.
     * 
     * This is not only a treeTable issue: the coupling of 
     * margins to showing gridlines (and still get a "nice" 
     * looking selection) is not overly obvious in JTable as
     * well. Added convenience method to JXTable to adjust margins to 
     * 0/1 if hiding/showing grid lines. 
     *
     */
    public void testShowGrid() {
        JXTreeTable table = new JXTreeTable(simpleTreeTableModel);
        // sanity: initial margins are (0, 0), grid off
        boolean show = false;
        assertEquals(0, table.getRowMargin());
        assertEquals(show, table.getShowHorizontalLines());
        assertEquals(0, table.getColumnMargin());
        assertEquals(show, table.getShowVerticalLines());
        // show lines
        table.setShowGrid(!show, !show);
        assertEquals(1, table.getRowMargin());
        assertEquals(!show, table.getShowHorizontalLines());
        assertEquals(1, table.getColumnMargin());
        assertEquals(!show, table.getShowVerticalLines());
        
    }

    /**
     * Issue #120-jdnc: data corruption if collapsed while editing.
     * Note: this tests programatic collapse while editing! 
     * Don't know how to test mouse-triggered collapse/expand, "looked"
     * at it in the visualCheck. 
     */
    public void testEditOnCollapse() {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("ROOT");
        DefaultMutableTreeTableNode a = new DefaultMutableTreeTableNode("A");
        DefaultMutableTreeTableNode a1 = new DefaultMutableTreeTableNode("A1");
        DefaultMutableTreeTableNode b = new DefaultMutableTreeTableNode("B");
        a.add(a1);
        root.add(a);
        root.add(b);
        
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("A");
        
        TreeTableModel model = new DefaultTreeTableModel(root, columnNames) {
            public boolean isCellEditable(Object obj,int col) {
                return true;
              }
                                                                                      
              public void setValueAt(Object value,Object node,int col) {
                  MutableTreeTableNode treeNode = (MutableTreeTableNode) node;
                 treeNode.setUserObject(value);
                 MutableTreeTableNode parent = (MutableTreeTableNode) treeNode.getParent();
                 modelSupport.fireChildChanged(new TreePath(
                        getPathToRoot(parent)), parent.getIndex(treeNode),
                        treeNode);
              }
                                                                                      
              public Object getValueAt(Object node,int col) {
                  return ((DefaultMutableTreeTableNode) node).getUserObject();
              }
            };
            
        JXTreeTable treeTable = new JXTreeTable(model);
        treeTable.setEditable(true);
        treeTable.expandAll();
        assertEquals(3, treeTable.getRowCount());
        Object valueBelow = treeTable.getValueAt(2, 0);
        treeTable.editCellAt(1, 0);
        ((JTextField) treeTable.getEditorComponent()).setText("other");
        treeTable.collapseRow(0);
        assertEquals(2, treeTable.getRowCount());
        if (treeTable.isEditing()) {
            treeTable.getCellEditor().stopCellEditing();
        }
        assertEquals(valueBelow, treeTable.getValueAt(1, 0));
    }

    /**
     * Issue #399-swingx: editing terminated by selecting editing row.<p>
     * Assert workaround: setExpandsSelectedPaths(false)
     */
    public void testSelectionKeepsEditingWithExpandsFalse() {
        JXTreeTable treeTable = new JXTreeTable(new FileSystemModel()) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
            
        };
        boolean canEdit = treeTable.editCellAt(1, 2);
        // sanity: editing started
        assertTrue(canEdit);
        // sanity: nothing selected
        assertTrue(treeTable.getSelectionModel().isSelectionEmpty());
        int editingRow = treeTable.getEditingRow();
        treeTable.setExpandsSelectedPaths(false);
        treeTable.setRowSelectionInterval(editingRow, editingRow);
        assertEquals("after selection treeTable editing state must be unchanged", canEdit, treeTable.isEditing());
    }

    
    /**
     * Issue #4-, #340-swingx: duplicate notification
     * 
     * starting from unselected, the event count is 1 as expected 
     */
    public void testSelectionEvents() {
        JXTreeTable treeTable = prepareTreeTable(false);
        TreeSelectionReport report = new TreeSelectionReport();
        treeTable.getTreeSelectionModel().addTreeSelectionListener(report);
        treeTable.setRowSelectionInterval(1, 1);
        assertEquals(1, report.getEventCount());
    }


    /**
     * Issue #4-, #340-swingx: duplicate notification
     * 
     * Hmm... unexpected: the eventCount (2) is not effected by 
     * catching isAdjusting listSelectionEvents. The reason is 
     * an intermediate clearSelection which fires the additional.
     */
    public void testSelectionChangedEvents() {
        JXTreeTable treeTable = prepareTreeTable(true);
        TreeSelectionReport report = new TreeSelectionReport();
        treeTable.getTreeSelectionModel().addTreeSelectionListener(report);
        treeTable.setRowSelectionInterval(1, 1);
        assertEquals(1, report.getEventCount());
    }

    /**
     * Issue #4-, #340-swingx: duplicate notification
     * 
     * The old in the event must be the last selected. 
     */
    public void testSelectionChangedHasFirstOldPath() {
        JXTreeTable treeTable = prepareTreeTable(true);
        TreeSelectionReport report = new TreeSelectionReport();
        treeTable.getTreeSelectionModel().addTreeSelectionListener(report);
        treeTable.setRowSelectionInterval(1, 1);
        TreeSelectionEvent event = report.getLastEvent();
        assertEquals(treeTable.getPathForRow(1), event.getNewLeadSelectionPath());
        assertEquals(treeTable.getPathForRow(0), event.getOldLeadSelectionPath());
    }


    /**
     * creates and configures a treetable for usage in selection tests.
     * 
     * @param selectFirstRow boolean to indicate if the first row should
     *   be selected.
     * @return a tree table configured for selection tests
     */
    protected JXTreeTable prepareTreeTable(boolean selectFirstRow) {
        JXTreeTable treeTable = new JXTreeTable(simpleTreeTableModel);
        treeTable.setRootVisible(true);
        // sanity: assert that we have at least two rows to change selection
        assertTrue(treeTable.getRowCount() > 1);
        if (selectFirstRow) {
            treeTable.setRowSelectionInterval(0, 0);
        }
        return treeTable;
    }

    /**
     * Issue #4-, #340-swingx: duplicate notification
     * 
     * sanity: check if there's only one event fired if selection is 
     * set directly via the treeSelectionModel. Characterize normal
     * treeSelection to mimic.
     */
    public void testSelectionChangedOnTreeSelection() {
        JXTreeTable treeTable = prepareTreeTable(true);
        TreePath oldSelected = treeTable.getPathForRow(0);
        TreeSelectionReport report = new TreeSelectionReport();
        treeTable.getTreeSelectionModel().addTreeSelectionListener(report);
        TreePath newSelected = treeTable.getPathForRow(1);
        treeTable.getTreeSelectionModel().setSelectionPath(newSelected);
        assertEquals(1, report.getEventCount());
        // check the paths
        TreeSelectionEvent event = report.getLastEvent();
        assertEquals(oldSelected, event.getOldLeadSelectionPath());
        assertEquals(newSelected, event.getNewLeadSelectionPath());
    }

    /**
     * Issue #270-swingx: NPE in some contexts when accessing the 
     * TreeTableModelAdapter.
     *
     */
    public void testConservativeRowForNodeInAdapter() {
        // for testing we need a model which relies on 
        // node != null
        TreeTableModel model = new DefaultTreeTableModel((TreeTableNode) simpleTreeTableModel.getRoot()) {
            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public Object getValueAt(Object node, int column) {
                // access node
                node.toString();
                return super.getValueAt(node, column);
            }

            @Override
            public void setValueAt(Object value, Object node, int column) {
                // access node
                node.toString();
                super.setValueAt(value, node, column);
            }

            @Override
            public boolean isCellEditable(Object node, int column) {
                // access node
                node.toString();
                return super.isCellEditable(node, column);
            }
            
        };
        // can't use ComponentTreeTableModel with JXFrame in headless environment
//        JXTreeTable treeTable = new JXTreeTable(new ComponentTreeTableModel(new JXFrame()));
        JXTreeTable treeTable = new JXTreeTable(model);
        treeTable.setRootVisible(true);
        TableModel adapter = treeTable.getModel();
        treeTable.collapseAll();
        assertEquals(1, treeTable.getRowCount());
        // simulate contexts where the accessed row isn't currently visible
        adapter.getValueAt(treeTable.getRowCount(), 0);
        adapter.isCellEditable(treeTable.getRowCount(), 0);
        adapter.setValueAt("somename", treeTable.getRowCount(), 0);
    }
    /**
     * test if table and tree rowHeights are the same.
     *
     */
    public void testAdjustedRowHeights() {
        JXTreeTable treeTable = new JXTreeTable(simpleTreeTableModel);
        JXTree tree = (JXTree) treeTable.getCellRenderer(0, 0);
        // sanity: same initially
        assertEquals("table and tree rowHeights must be equal", 
                treeTable.getRowHeight(), tree.getRowHeight());
        // change treeTable height
        treeTable.setRowHeight(treeTable.getRowHeight() * 2);
        assertEquals("table and tree rowHeights must be equal", 
                treeTable.getRowHeight(), tree.getRowHeight());
        // change treeTable height
        tree.setRowHeight(tree.getRowHeight() * 2);
        assertEquals("table and tree rowHeights must be equal", 
                treeTable.getRowHeight(), tree.getRowHeight());

    }
    /**
     * #321-swingx: missing tree property toggleClickCount, largeModel.
     *
     */
    public void testToggleClickCount() {
        JXTreeTable treeTable = new JXTreeTable(simpleTreeTableModel);
        int clickCount = treeTable.getToggleClickCount();
        // asserting documented default clickCount == 2
        assertEquals("default clickCount", 2, clickCount);
        int newClickCount = clickCount + 1;
        treeTable.setToggleClickCount(newClickCount);
        assertEquals("toggleClickCount must be changed", 
                newClickCount, treeTable.getToggleClickCount());
        boolean largeModel = treeTable.isLargeModel();
        assertFalse("initial largeModel", largeModel);
        treeTable.setLargeModel(!largeModel);
        assertTrue("largeModel property must be toggled", treeTable.isLargeModel());
        
    }
    /**
     * Issue #168-jdnc: dnd enabled breaks node collapse/expand.
     * testing auto-detection of dragHackEnabled.
     * 
     */
    public void testDragHackFlagOn() {
        JXTreeTable treeTable = new JXTreeTable(simpleTreeTableModel);
        assertNull(treeTable.getClientProperty(JXTreeTable.DRAG_HACK_FLAG_KEY));
        treeTable.getTreeTableHacker().expandOrCollapseNode(0, 
                new MouseEvent(treeTable, MouseEvent.MOUSE_PRESSED, 0, InputEvent.BUTTON1_MASK, 0, 0, 1, false));
        Boolean dragHackFlag = (Boolean) treeTable.getClientProperty(JXTreeTable.DRAG_HACK_FLAG_KEY);
        assertNotNull(dragHackFlag);
        assertTrue(dragHackFlag);
    }

    /**
     * Issue #168-jdnc: dnd enabled breaks node collapse/expand.
     * testing auto-detection of dragHackEnabled.
     * 
     */
    public void testDragHackFlagOff() {
        System.setProperty("sun.swing.enableImprovedDragGesture", "true");
        JXTreeTable treeTable = new JXTreeTable(simpleTreeTableModel);
        assertNull(treeTable.getClientProperty(JXTreeTable.DRAG_HACK_FLAG_KEY));
        treeTable.getTreeTableHacker().expandOrCollapseNode(0, 
                new MouseEvent(treeTable, MouseEvent.MOUSE_PRESSED, 0, InputEvent.BUTTON1_MASK, 0, 0, 1, false));
        Boolean dragHackFlag = (Boolean) treeTable.getClientProperty(JXTreeTable.DRAG_HACK_FLAG_KEY);
        assertNotNull(dragHackFlag);
        assertFalse(dragHackFlag);
        System.getProperties().remove("sun.swing.enableImprovedDragGesture");
    }

    /**
     * loosely related to Issue #248-swingx: setRootVisible (true) after
     * initial rootInvisible didn't show the root.
     * 
     * this here is a sanity test that there is exactly one row, the problem
     * is a missing visual update of the table.
     *
     */
    public void testEmptyModelInitiallyInvisibleRoot() {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root);
        final JXTreeTable treeTable = new JXTreeTable(model);
        // sanity...
        assertFalse(treeTable.isRootVisible());
        assertEquals("no rows with invisible root", 0, treeTable.getRowCount());
        treeTable.setRootVisible(true);
        // sanity...
        assertTrue(treeTable.isRootVisible());
        assertEquals("one row with visible root", 1, treeTable.getRowCount());

    }
    
    /**
     * Issue #247-swingx: update probs with insert node.
     * The insert under a collapsed node fires a dataChanged on the table 
     * which results in the usual total "memory" loss (f.i. selection)
     *
     * The tree model is after setup is (see the bug report as well):
     * root
     *   childA
     *     childB
     *     
     * In the view childA is collapsed:
     * root
     *   childA  
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     * 
     */
    public void testInsertUnderCollapsedNode() throws InterruptedException, InvocationTargetException {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root);
        DefaultMutableTreeTableNode childA = model.addChild(root);
        final DefaultMutableTreeTableNode childB = model.addChild(childA);
        final JXTreeTable treeTable = new JXTreeTable(model);
        treeTable.setRootVisible(true);
        // sanity...
        assertEquals(2, treeTable.getRowCount());
        final int selected = 1;
        // select childA
        treeTable.setRowSelectionInterval(selected, selected);
        model.addChild(childB);
        // need to invoke - the tableEvent is fired delayed as well
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                int selectedAfterInsert = treeTable.getSelectedRow();
                assertEquals(selected, selectedAfterInsert);

            }
        });
    }
    
    /**
     * Model used to show insert update issue.
     */
    public static class InsertTreeTableModel extends DefaultTreeTableModel {
        private boolean rootIsFolder;
        
        public InsertTreeTableModel(TreeTableNode root) {
            super(root);
        }

        public InsertTreeTableModel(TreeTableNode root, boolean rootIsFolder) {
            super(root);
            this.rootIsFolder = rootIsFolder;
        }
        
        @Override
        public boolean isLeaf(Object node) {
            if (rootIsFolder && (node == getRoot())) {
                return false;
            }
            return super.isLeaf(node);
        }


        public int getColumnCount() {
            return 2;
        }

        public DefaultMutableTreeTableNode addChild(DefaultMutableTreeTableNode parent) {
            DefaultMutableTreeTableNode newNode = new DefaultMutableTreeTableNode("Child");
            parent.add(newNode);
            modelSupport.fireChildAdded(new TreePath(getPathToRoot(parent)),
                    parent.getIndex(newNode), newNode);

            return newNode;
        }
    }


    /**
     * Issue #230-swingx: 
     * JXTreeTable should fire property change on setTreeTableModel.
     * 
     * 
     *
     */
    public void testTreeTableModelIsBoundProperty() {
        JXTreeTable treeTable = new JXTreeTable();
        PropertyChangeReport report = new PropertyChangeReport();
        treeTable.addPropertyChangeListener(report);
        treeTable.setTreeTableModel(simpleTreeTableModel);
        int allPropertyCount = report.getEventCount();
        int treeTMPropertyCount = report.getEventCount("treeTableModel");
        assertEquals("treeTable must have fired exactly one event for property treeTableModel", 
                1, treeTMPropertyCount);
        assertEquals("treeTable must have fired event for property treeTableModel only",
                allPropertyCount, treeTMPropertyCount);
        // sanity: must not fire when setting to same
        report.clear();
        treeTable.setTreeTableModel(simpleTreeTableModel);
        assertEquals("treeTable must not have fired", 0, report.getEventCount()); 
    }
    /**
     * Issue #54: hidden columns not removed on setModel.
     * sanity test (make sure nothing evil introduced in treeTable as 
     * compared to table)
     */
    public void testRemoveAllColumsAfterModelChanged() {
        JXTreeTable table = new JXTreeTable(new FileSystemModel());
        TableColumnExt columnX = table.getColumnExt(1);
        columnX.setVisible(false);
        int columnCount = table.getColumnCount(true);
        assertEquals("total column count must be same as model", table.getModel().getColumnCount(), columnCount);
        assertEquals("visible column count must one less as total", columnCount - 1, table.getColumnCount());
        table.setTreeTableModel(new FileSystemModel());
        assertEquals("visible columns must be same as total", 
                table.getColumnCount(), table.getColumnCount(true));
      }

    /**
     * Issue #241: treeModelListeners not removed.
     *
     */
    public void testRemoveListeners() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        treeTable.setTreeTableModel(new FileSystemModel());
        assertEquals(0, ((FileSystemModel) treeTableModel).getTreeModelListeners().length);
    }
    
    /**
     * Clarify contract of isHierarchical.
     */
    public void testIsHierarchical() {
        TreeTableModel model = new DefaultTreeTableModel();
        //sanity
        assertEquals(-1, model.getHierarchicalColumn());
        JXTreeTable treeTable = new JXTreeTable(model);
        assertEquals(model.getHierarchicalColumn(), treeTable.getHierarchicalColumn());
        // either this or throw an exception for -1 index
        // illegal view index anyway
        try {
            treeTable.isHierarchical(-1);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //success
        }
    }
    
    public void testRowForPath() {
        JXTreeTable treeTable = new JXTreeTable(simpleTreeTableModel);
        // @todo - make sure we find an expandible row instead of hardcoding
        int rowCount = treeTable.getRowCount();
        int row = 2;
        TreePath path = treeTable.getPathForRow(row);
        assertEquals("original row must be retrieved", row, treeTable.getRowForPath(path));
        treeTable.expandRow(row - 1);
        // sanity assert
        assertTrue("really expanded", treeTable.getRowCount() > rowCount);
        TreePath expanded = treeTable.getPathForRow(row);
        assertNotSame("path at original row must be different when expanded", path, expanded);
        assertEquals("original row must be retrieved", row, treeTable.getRowForPath(expanded));
        
    }
    
    public void testPathForRowContract() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        assertNull("row < 0 must return null path", treeTable.getPathForRow(-1));
        assertNull("row >= getRowCount must return null path", treeTable.getPathForRow(treeTable.getRowCount()));
    }
    
    public void testTableRowAtNegativePoint() {
        JXTable treeTable = new JXTable(1, 4);
        int negativeYRowHeight = - treeTable.getRowHeight();
        int negativeYRowHeightPlusOne = negativeYRowHeight + 1;
        int negativeYMinimal = -1;
        assertEquals("negative y location rowheight " + negativeYRowHeight + " must return row -1", 
                -1,  treeTable.rowAtPoint(new Point(-1, negativeYRowHeight)));
        assertEquals("negative y location " + negativeYRowHeightPlusOne +" must return row -1", 
                -1,  treeTable.rowAtPoint(new Point(-1, negativeYRowHeightPlusOne)));
        assertEquals("minimal negative y location must return row -1", 
                -1,  treeTable.rowAtPoint(new Point(-1, negativeYMinimal)));
        
    }


    public void testPathForLocationContract() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        // this is actually a JTable rowAtPoint bug: falsely calculates
        // row == 0 if  - 1 >= y > - getRowHeight()
        
        //assertEquals("location outside must return null path", null, treeTable.getPathForLocation(-1, -(treeTable.getRowHeight() - 1)));
        int negativeYRowHeight = - treeTable.getRowHeight();
        int negativeYRowHeightPlusOne = negativeYRowHeight + 1;
        int negativeYMinimal = -1;
        assertEquals("negative y location rowheight " + negativeYRowHeight + " must return row -1", 
                -1,  treeTable.rowAtPoint(new Point(-1, negativeYRowHeight)));
        assertEquals("negative y location " + negativeYRowHeightPlusOne +" must return row -1", 
                -1,  treeTable.rowAtPoint(new Point(-1, negativeYRowHeightPlusOne)));
        assertEquals("minimal negative y location must return row -1", 
                -1,  treeTable.rowAtPoint(new Point(-1, negativeYMinimal)));
    }
    /**
     * Issue #151: renderer properties ignored after setting treeTableModel.
     * 
     */
    public void testRendererProperties() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        // storing negates of properties
        boolean expandsSelected = !treeTable.getExpandsSelectedPaths();
        boolean scrollsOnExpand = !treeTable.getScrollsOnExpand();
        boolean showRootHandles = !treeTable.getShowsRootHandles();
        boolean rootVisible = !treeTable.isRootVisible();
        // setting negates properties
        treeTable.setExpandsSelectedPaths(expandsSelected);
        treeTable.setScrollsOnExpand(scrollsOnExpand);
        treeTable.setShowsRootHandles(showRootHandles);
        treeTable.setRootVisible(rootVisible);
        // assert negates are set - sanity assert
        assertEquals("expand selected", expandsSelected, treeTable
                .getExpandsSelectedPaths());
        assertEquals("scrolls expand", scrollsOnExpand, treeTable
                .getScrollsOnExpand());
        assertEquals("shows handles", showRootHandles, treeTable
                .getShowsRootHandles());
        assertEquals("root visible", rootVisible, treeTable.isRootVisible());
        // setting a new model
        treeTable.setTreeTableModel(new DefaultTreeTableModel());
        // assert negates are set
        assertEquals("expand selected", expandsSelected, treeTable
                .getExpandsSelectedPaths());
        assertEquals("scrolls expand", scrollsOnExpand, treeTable
                .getScrollsOnExpand());
        assertEquals("shows handles", showRootHandles, treeTable
                .getShowsRootHandles());
        assertEquals("root visible", rootVisible, treeTable.isRootVisible());

    }

    /**
     * Issue #148: line style client property not respected by renderer.
     * 
     */
    public void testLineStyle() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        String propertyName = "JTree.lineStyle";
        treeTable.putClientProperty(propertyName, "Horizontal");
        JXTree renderer = (JXTree) treeTable.getCellRenderer(0, 0);
        assertEquals(propertyName + " set on renderer", "Horizontal", renderer
                .getClientProperty(propertyName));
    }

    /**
     * sanity test: arbitrary client properties not passed to renderer.
     * 
     */
    public void testArbitraryClientProperty() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        String propertyName = "someproperty";
        treeTable.putClientProperty(propertyName, "Horizontal");
        JXTree renderer = (JXTree) treeTable.getCellRenderer(0, 0);
        assertNull(propertyName + " not set on renderer", renderer
                .getClientProperty(propertyName));

    }

    //copied from JTree and modified to use TTNs
    protected static TreeTableModel getDefaultTreeTableModel() {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(
                "JXTreeTable");
        DefaultMutableTreeTableNode parent;

        parent = new DefaultMutableTreeTableNode("colors");
        root.add(parent);
        parent.add(new DefaultMutableTreeTableNode("blue"));
        parent.add(new DefaultMutableTreeTableNode("violet"));
        parent.add(new DefaultMutableTreeTableNode("red"));
        parent.add(new DefaultMutableTreeTableNode("yellow"));

        parent = new DefaultMutableTreeTableNode("sports");
        root.add(parent);
        parent.add(new DefaultMutableTreeTableNode("basketball"));
        parent.add(new DefaultMutableTreeTableNode("soccer"));
        parent.add(new DefaultMutableTreeTableNode("football"));
        parent.add(new DefaultMutableTreeTableNode("hockey"));

        parent = new DefaultMutableTreeTableNode("food");
        root.add(parent);
        parent.add(new DefaultMutableTreeTableNode("hot dogs"));
        parent.add(new DefaultMutableTreeTableNode("pizza"));
        parent.add(new DefaultMutableTreeTableNode("ravioli"));
        parent.add(new DefaultMutableTreeTableNode("bananas"));
        return new DefaultTreeTableModel(root);
    }

    /**
     * Creates and returns a custom model from JXTree default model. The model
     * is of type DefaultTreeModel, allowing for easy insert/remove.
     * 
     * @return
     */
    private TreeTableModel createCustomTreeTableModelFromDefault() {
        JXTree tree = new JXTree();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        TreeTableModel customTreeTableModel = TreeTableUtils
                .convertDefaultTreeModel(treeModel);

        return customTreeTableModel;
    }

    // ------------------ init
    protected void setUp() throws Exception {
        super.setUp();
        simpleTreeTableModel = getDefaultTreeTableModel();
        this.treeTableModel = new FileSystemModel();
    }

}
