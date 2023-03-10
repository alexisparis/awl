/*
 * $Id: JXTreeUnitTest.java,v 1.25 2007/08/16 14:34:37 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTableUnitTest.InsertTreeTableModel;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.tree.DefaultXTreeCellEditor;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.FileSystemModel;
import org.jdesktop.swingx.treetable.TreeTableModel;


/**
 * Unit tests for JXTree.
 * 
 * @author Jeanette Winzenburg
 */
public class JXTreeUnitTest extends InteractiveTestCase {

    protected TreeTableModel treeTableModel;
        
    public JXTreeUnitTest() {
        super("JXTree Test");
    }

    /**
     * focus issues with popup in editors: tweak with 
     * custom cellEditorListener.
     *
     */
    public void testEditorListenerRemovedOnEditorStopped() {
        JXTree tree = createEditingTree();
        DefaultTreeCellEditor cellEditor = (DefaultTreeCellEditor) tree.getCellEditor();
        // core doesn't remove the listener ... dooh
        cellEditor.stopCellEditing();
        // but we do ... 
        assertEquals(1, cellEditor.getCellEditorListeners().length);
    }


    /**
     * focus issues with popup in editors: tweak with 
     * custom cellEditorListener.
     *
     */
    public void testEditorListenerRemovedOnEditorCancel() {
        JXTree tree = createEditingTree();
        DefaultTreeCellEditor cellEditor = (DefaultTreeCellEditor) tree.getCellEditor();
        // core doesn't remove the listener ... dooh
        cellEditor.cancelCellEditing();
        // but we do ... 
        assertEquals(1, cellEditor.getCellEditorListeners().length);
    }

    /**
     * focus issues with popup in editors: tweak with 
     * custom cellEditorListener.
     *
     */
    public void testEditorListenerRemovedOnTreeCancel() {
        JXTree tree = createEditingTree();
        DefaultTreeCellEditor cellEditor = (DefaultTreeCellEditor) tree.getCellEditor();
        // core doesn't remove the listener ... dooh
        tree.cancelEditing();
        // but we do ... 
        assertEquals(1, cellEditor.getCellEditorListeners().length);
    }
    
    /**
     * focus issues with popup in editors: tweak with 
     * custom cellEditorListener.
     *
     */
    public void testEditorListenerRemovedOnTreeStop() {
        JXTree tree = createEditingTree();
        DefaultTreeCellEditor cellEditor = (DefaultTreeCellEditor) tree.getCellEditor();
        // core doesn't remove the listener ... dooh
        tree.stopEditing();
        // but we do ... 
        assertEquals(1, cellEditor.getCellEditorListeners().length);
    }

    /**
     * focus issues with popup in editors: tweak with 
     * custom cellEditorListener.
     *
     */
    public void testEditorListenerOnXTree() {
        JTree core = new JTree();
        int coreCount = getListenerCountAfterStartEditing(core);
        JXTree tree = createEditingTree();
        DefaultTreeCellEditor cellEditor = (DefaultTreeCellEditor) tree.getCellEditor();
        assertEquals("need one more listener than core", 
                coreCount + 1, cellEditor.getCellEditorListeners().length);
    }
    
    /**
     * @return a tree with default model and editing started on row 2
     */
    private JXTree createEditingTree() {
        JXTree tree = new JXTree();
        tree.setEditable(true);
        // sanity
        assertTrue(tree.getRowCount() > 2);
        TreePath path = tree.getPathForRow(2);
        tree.startEditingAtPath(path);
        return tree;
    }

    /**
     * characterization: listeners in core tree.
     *
     */
    public void testEditorListenerOnCoreTree() {
        JTree tree = new JTree();
        int listenerCount = getListenerCountAfterStartEditing(tree);
        assertEquals(1, listenerCount);
        tree.stopEditing();
        // doesn't remove the listener ... dooh
        assertEquals(1, ((DefaultTreeCellEditor) tree.getCellEditor()).getCellEditorListeners().length);
        
    }


    /**
     * Starts editing on row 2 and returns the cell editor listener count after.
     * 
     * @param tree
     * @return
     */
    private int getListenerCountAfterStartEditing(JTree tree) {
        tree.setEditable(true);
        // sanity
        assertTrue(tree.getRowCount() > 2);
        TreePath path = tree.getPathForRow(2);
        tree.startEditingAtPath(path);
        DefaultTreeCellEditor cellEditor = (DefaultTreeCellEditor) tree.getCellEditor();
        int listenerCount = cellEditor.getCellEditorListeners().length;
        return listenerCount;
    }
    

    
    /**
     * Issue #473-swingx: NPE in JXTree with highlighter.<p>
     * 
     * Renderers are doc'ed to cope with invalid input values.
     * Highlighters can rely on valid ComponentAdapter state. 
     * JXTree delegatingRenderer is the culprit which does set
     * invalid ComponentAdapter state. Negative invalid index.
     *
     */
    public void testIllegalNegativeTreeRowIndex() {
        JXTree tree = new JXTree();
        tree.expandAll();
        assertTrue(tree.getRowCount() > 0);
        TreeCellRenderer renderer = tree.getCellRenderer();
        renderer.getTreeCellRendererComponent(tree, "dummy", false, false, false, -1, false);
        SearchPredicate predicate = new SearchPredicate(Pattern.compile("\\QNode\\E"));
        Highlighter searchHighlighter = new ColorHighlighter(null, Color.RED, predicate);
        tree.addHighlighter(searchHighlighter);
        renderer.getTreeCellRendererComponent(tree, "dummy", false, false, false, -1, false);
    }
    
    /**
     * Issue #473-swingx: NPE in JXTree with highlighter.<p>
     * 
     * Renderers are doc'ed to cope with invalid input values.
     * Highlighters can rely on valid ComponentAdapter state. 
     * JXTree delegatingRenderer is the culprit which does set
     * invalid ComponentAdapter state. Invalid index > valid range.
     *
     */
    public void testIllegalExceedingTreeRowIndex() {
        JXTree tree = new JXTree();
        tree.expandAll();
        assertTrue(tree.getRowCount() > 0);
        TreeCellRenderer renderer = tree.getCellRenderer();
        renderer.getTreeCellRendererComponent(tree, "dummy", false, false, false, tree.getRowCount(), false);
        SearchPredicate predicate = new SearchPredicate(Pattern.compile("\\QNode\\E"));
        Highlighter searchHighlighter = new ColorHighlighter(null, Color.RED, predicate);
        tree.addHighlighter(searchHighlighter);
        renderer.getTreeCellRendererComponent(tree, "dummy", false, false, false, tree.getRowCount(), false);
    }

    /**
     * test convenience method accessing the configured adapter.
     *
     */
    public void testConfiguredComponentAdapter() {
        JXTree list = new JXTree();
        list.expandAll();
        assertTrue(list.getRowCount() > 0);
        ComponentAdapter adapter = list.getComponentAdapter();
        assertEquals(0, adapter.column);
        assertEquals(0, adapter.row);
        adapter.row = 1;
        // corrupt adapter
        adapter.column = 1;
        adapter = list.getComponentAdapter(0);
        assertEquals(0, adapter.column);
        assertEquals(0, adapter.row);
    }

    
    /**
     * Issue #254-swingx: expandAll doesn't expand if root not shown?
     *
     */
    public void testExpandAllWithInvisible() {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root);
        int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            model.addChild(root);
        }
        final JXTree treeTable = new JXTree(model);
        // sanity...
        assertTrue(treeTable.isRootVisible());
        assertEquals("all children visible", childCount + 1, treeTable.getRowCount());
        treeTable.collapseAll();
        assertEquals(" all children invisible", 1, treeTable.getRowCount());
        treeTable.setRootVisible(false);
        assertEquals("no rows with invisible root", 0, treeTable.getRowCount());
        treeTable.expandAll();
        assertTrue(treeTable.getRowCount() > 0);
        
    }

    /**
     * test enhanced getSelectedRows contract: returned 
     * array != null
     *
     */
    public void testNotNullGetSelectedRows() {
        JXTree tree = new JXTree(treeTableModel);
        // sanity: no selection
        assertEquals(0, tree.getSelectionCount());
        assertNotNull("getSelectedRows guarantees not null array", tree.getSelectionRows());
    }
    
    /**
     * test enhanced getSelectedRows contract: returned 
     * array != null
     *
     */
    public void testNotNullGetSelectedPaths() {
        JXTree tree = new JXTree(treeTableModel);
        // sanity: no selection
        assertEquals(0, tree.getSelectionCount());
        assertNotNull("getSelectedPaths guarantees not null array", tree.getSelectionPaths());
    }
    /**
     * Issue #221-swingx: actionMap not initialized in JXTreeNode constructor.
     * Issue #231-swingx: icons lost in editor, enhanced default editor not installed.
     * 
     * PENDING: test all constructors!
     *
     */
    public void testInitInConstructors() {
        assertXTreeInit(new JXTree());
        assertXTreeInit(new JXTree(new Object[] {}));
        assertXTreeInit(new JXTree(new Vector()));
        assertXTreeInit(new JXTree(new Hashtable()));
        assertXTreeInit(new JXTree(new DefaultMutableTreeNode("dummy"), false));
        assertXTreeInit(new JXTree(new DefaultMutableTreeNode("dummy")));
        assertXTreeInit(new JXTree(new DefaultTreeModel(new DefaultMutableTreeNode("dummy"))));
    }

    /**
     * @param tree
     */
    private void assertXTreeInit(JXTree tree) {
        assertNotNull("Actions must be initialized", tree.getActionMap().get("find"));
        assertTrue("Editor must be DefaultXTreeCellEditor", 
                tree.getCellEditor() instanceof DefaultXTreeCellEditor);
        // JW: wrong assumption, available for TreeTableModel impl only?
//        assertNotNull("conversionMethod must be initialized", 
//                tree.getValueConversionMethod(tree.getModel()));
//        tree.getValueConversionMethod(tree.getModel());
    }

    /** 
     * JTree allows null model.
     * learning something new every day :-)
     *
     */
    public void testNullModel() {
        JXTree tree = new JXTree();
        assertNotNull(tree.getModel());
        tree.setModel(null);
        assertEquals(0, tree.getRowCount());
        // tree.getComponentAdapter().isLeaf();
    }

    
    protected void setUp() throws Exception {
        super.setUp();
        treeTableModel = new FileSystemModel();
    }

}
