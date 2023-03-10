/*
 * $Id: JXTaskPaneTest.java,v 1.8 2006/11/01 16:23:12 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.test.PropertyChangeReport;

public class JXTaskPaneTest extends InteractiveTestCase {

  public JXTaskPaneTest(String testTitle) {
    super(testTitle);
  }

  public void testBean() throws Exception {
    PropertyChangeReport report = new PropertyChangeReport();
    JXTaskPane group = new JXTaskPane();
    group.setAnimated(false);
    group.addPropertyChangeListener(report);

    // ANIMATED PROPERTY
    group.setAnimated(true);
    assertTrue(group.isAnimated());
    assertEquals(JXTaskPane.ANIMATED_CHANGED_KEY, report.getLastEvent()
      .getPropertyName());
    assertTrue(report.getLastNewBooleanValue());

    group.setAnimated(false);
    assertFalse(group.isAnimated());
    assertFalse(report.getLastNewBooleanValue());

    UIManager.put("TaskPane.animate", Boolean.FALSE);
    JXTaskPane anotherGroup = new JXTaskPane();
    assertFalse(anotherGroup.isAnimated());

    UIManager.put("TaskPane.animate", null);
    anotherGroup = new JXTaskPane();
    assertTrue(anotherGroup.isAnimated());

    // TITLE
    group.setTitle("the title");
    assertEquals("the title", group.getTitle());
    assertEquals(JXTaskPane.TITLE_CHANGED_KEY, report.getLastEvent()
      .getPropertyName());
    assertEquals("the title", report.getLastNewValue());

    // ICON
    assertNull(group.getIcon());
    Icon icon = new EmptyIcon();
    group.setIcon(icon);
    assertNotNull(group.getIcon());
    assertEquals(JXTaskPane.ICON_CHANGED_KEY, report.getLastEvent()
      .getPropertyName());
    assertEquals(icon, report.getLastNewValue());
    group.setIcon(null);
    assertEquals(icon, report.getLastOldValue());
    assertNull(report.getLastNewValue());

    // SPECIAL
    assertFalse(group.isSpecial());
    group.setSpecial(true);
    assertTrue(group.isSpecial());
    assertEquals(JXTaskPane.SPECIAL_CHANGED_KEY, report.getLastEvent()
      .getPropertyName());
    assertTrue(report.getLastNewBooleanValue());
    assertFalse(report.getLastOldBooleanValue());

    // SCROLL ON EXPAND
    assertFalse(group.isScrollOnExpand());
    group.setScrollOnExpand(true);
    assertTrue(group.isScrollOnExpand());
    assertEquals(JXTaskPane.SCROLL_ON_EXPAND_CHANGED_KEY, report.getLastEvent()
      .getPropertyName());
    assertTrue(report.getLastNewBooleanValue());
    assertFalse(report.getLastOldBooleanValue());

    // EXPANDED
    assertTrue(group.isExpanded());
    group.setExpanded(false);
    assertFalse(group.isExpanded());
    assertEquals(JXTaskPane.EXPANDED_CHANGED_KEY, report.getLastEvent()
      .getPropertyName());
    assertFalse(report.getLastNewBooleanValue());
    assertTrue(report.getLastOldBooleanValue());

    new JXTaskPaneBeanInfo();
  }

  public void testContentPane() {
    JXTaskPane group = new JXTaskPane();
    assertEquals(0, group.getContentPane().getComponentCount());

    // Objects are not added to the taskPane but to its contentPane
    JButton button = new JButton();
    group.add(button);
    assertEquals(group.getContentPane(), button.getParent());
    group.remove(button);
    assertNull(button.getParent());
    assertEquals(0, group.getContentPane().getComponentCount());
    group.add(button);
    group.removeAll();
    assertEquals(0, group.getContentPane().getComponentCount());
    group.add(button);
    group.remove(0);
    assertEquals(0, group.getContentPane().getComponentCount());

    BorderLayout layout = new BorderLayout();
    group.setLayout(layout);
    assertEquals(layout, group.getContentPane().getLayout());
    assertFalse(layout == group.getLayout());
  }

  public void testActions() throws Exception {
    JXTaskPane taskPane = new JXTaskPane();
    Action action = new AbstractAction() {
      public void actionPerformed(java.awt.event.ActionEvent e) {}
    };
    assertEquals(0, taskPane.getContentPane().getComponentCount());
    Component component = taskPane.add(action);
    assertEquals(taskPane.getContentPane(), component.getParent());
    assertEquals(1, taskPane.getContentPane().getComponentCount());
  }

  public void testAnimationListeners() throws Exception {
    JXTaskPane taskPane = new JXTaskPane();
    // start with a not expanded or animated taskPane
    taskPane.setAnimated(false);
    taskPane.setExpanded(false);
    assertFalse(taskPane.isExpanded());

    class ListenForEvents implements PropertyChangeListener {
      private boolean expandedEventReceived;
      private boolean collapsedEventReceived;
      private int animationStart;
      
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if ("expanded".equals(evt.getNewValue())) {
          expandedEventReceived = true;
        } else if ("collapsed".equals(evt.getNewValue())) {
          collapsedEventReceived = true;
        } else if ("reinit".equals(evt.getNewValue())) {
          animationStart++;
        }
      }
    }

    ListenForEvents listener = new ListenForEvents();

    // register a listener on the animation
    taskPane.addPropertyChangeListener(JXCollapsiblePane.ANIMATION_STATE_KEY,
      listener);
    taskPane.setAnimated(true);
    
    // expand the taskPane and...
    taskPane.setExpanded(true);
    // ...wait until listener has been notified
    while (!listener.expandedEventReceived) { Thread.sleep(100); }
    
    // collapse the taskPane and...
    // ...wait until listener has been notified
    taskPane.setExpanded(false);
    while (!listener.collapsedEventReceived) { Thread.sleep(100); }
    
    assertEquals(2, listener.animationStart);
  }

  public void testAddon() throws Exception {
    // move around all addons
    TestUtilities.cycleAddons(new JXTaskPane());
  }

  public void testIssue344() throws Exception {
    new JXTaskPane();
    LookAndFeelAddons.setAddon(new MetalLookAndFeelAddons());
    String uiClass = UIManager.getString(JXTaskPane.uiClassID);
    assertTrue("org.jdesktop.swingx.plaf.metal.MetalTaskPaneUI".equals(uiClass)
      || "org.jdesktop.swingx.plaf.misc.GlossyTaskPaneUI".equals(uiClass));
  }
  
}
