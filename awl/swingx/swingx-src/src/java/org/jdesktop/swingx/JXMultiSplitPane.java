/*
 * $Id: JXMultiSplitPane.java,v 1.5 2007/10/30 14:14:53 luano Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * <p>
 * All properties in this class are bound: when a properties value
 * is changed, all PropertyChangeListeners are fired.
 * 
 * @author Hans Muller
 */
public class JXMultiSplitPane extends JPanel {
    private AccessibleContext accessibleContext = null;
    private boolean continuousLayout = true;
    private DividerPainter dividerPainter = new DefaultDividerPainter();
    private Painter backgroundPainter;

    /**
     * Creates a MultiSplitPane with it's LayoutManager set to 
     * to an empty MultiSplitLayout.
     */
    public JXMultiSplitPane() {
	super(new MultiSplitLayout());
	InputHandler inputHandler = new InputHandler();
	addMouseListener(inputHandler);
	addMouseMotionListener(inputHandler);
	addKeyListener(inputHandler);
	setFocusable(true);
    }

    /** 
     * A convenience method that returns the layout manager cast 
     * to MutliSplitLayout.
     * 
     * @return this MultiSplitPane's layout manager
     * @see java.awt.Container#getLayout
     * @see #setModel
     */
    public final MultiSplitLayout getMultiSplitLayout() {
	return (MultiSplitLayout)getLayout();
    }

    /** 
     * A convenience method that sets the MultiSplitLayout model.
     * Equivalent to <code>getMultiSplitLayout.setModel(model)</code>
     * 
     * @param model the root of the MultiSplitLayout model
     * @see #getMultiSplitLayout
     * @see MultiSplitLayout#setModel
     */
    public final void setModel(Node model) {
	getMultiSplitLayout().setModel(model);
    }

    /** 
     * A convenience method that sets the MultiSplitLayout dividerSize
     * property. Equivalent to 
     * <code>getMultiSplitLayout().setDividerSize(newDividerSize)</code>.
     * 
     * @param dividerSize the value of the dividerSize property
     * @see #getMultiSplitLayout
     * @see MultiSplitLayout#setDividerSize
     */
    public final void setDividerSize(int dividerSize) {
	getMultiSplitLayout().setDividerSize(dividerSize);
    }

    /** 
     * A convenience method that returns the MultiSplitLayout dividerSize
     * property. Equivalent to 
     * <code>getMultiSplitLayout().getDividerSize()</code>.
     * 
     * @see #getMultiSplitLayout
     * @see MultiSplitLayout#getDividerSize
     */
    public final int getDividerSize() {
	return getMultiSplitLayout().getDividerSize();
    }

    /**
     * Sets the value of the <code>continuousLayout</code> property.
     * If true, then the layout is revalidated continuously while
     * a divider is being moved.  The default value of this property
     * is true.
     *
     * @param continuousLayout value of the continuousLayout property
     * @see #isContinuousLayout
     */
    public void setContinuousLayout(boolean continuousLayout) {
        boolean oldContinuousLayout = continuousLayout;
        this.continuousLayout = continuousLayout;
        firePropertyChange("continuousLayout", oldContinuousLayout, continuousLayout);
    }

    /**
     * Returns true if dragging a divider only updates
     * the layout when the drag gesture ends (typically, when the 
     * mouse button is released).
     *
     * @return the value of the <code>continuousLayout</code> property
     * @see #setContinuousLayout
     */
    public boolean isContinuousLayout() {
        return continuousLayout;
    }

    /** 
     * Returns the Divider that's currently being moved, typically
     * because the user is dragging it, or null.
     * 
     * @return the Divider that's being moved or null.
     */
    public Divider activeDivider() {
	return dragDivider;
    }

    /**
     * Draws a single Divider.  Typically used to specialize the
     * way the active Divider is painted.  
     * 
     * @see #getDividerPainter
     * @see #setDividerPainter
     */
    public static abstract class DividerPainter extends AbstractPainter<Divider> {
    }

    private class DefaultDividerPainter extends DividerPainter {
	public void doPaint(Graphics2D g, Divider divider, int width, int height) {
	    if ((divider == activeDivider()) && !isContinuousLayout()) {
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
	    }
	}
    }

    /** 
     * The DividerPainter that's used to paint Dividers on this MultiSplitPane.
     * This property may be null.
     * 
     * @return the value of the dividerPainter Property
     * @see #setDividerPainter
     */
    public DividerPainter getDividerPainter() {
	return dividerPainter;
    }

    /** 
     * Sets the DividerPainter that's used to paint Dividers on this 
     * MultiSplitPane.  The default DividerPainter only draws
     * the activeDivider (if there is one) and then, only if 
     * continuousLayout is false.  The value of this property is 
     * used by the paintChildren method: Dividers are painted after
     * the MultiSplitPane's children have been rendered so that 
     * the activeDivider can appear "on top of" the children.
     * 
     * @param dividerPainter the value of the dividerPainter property, can be null
     * @see #paintChildren
     * @see #activeDivider
     */
    public void setDividerPainter(DividerPainter dividerPainter) {
	this.dividerPainter = dividerPainter;
    }

    /**
     * Calls the UI delegate's paint method, if the UI delegate
     * is non-<code>null</code>.  We pass the delegate a copy of the
     * <code>Graphics</code> object to protect the rest of the
     * paint code from irrevocable changes
     * (for example, <code>Graphics.translate</code>).
     * <p>
     * If you override this in a subclass you should not make permanent
     * changes to the passed in <code>Graphics</code>. For example, you
     * should not alter the clip <code>Rectangle</code> or modify the
     * transform. If you need to do these operations you may find it
     * easier to create a new <code>Graphics</code> from the passed in
     * <code>Graphics</code> and manipulate it. Further, if you do not
     * invoker super's implementation you must honor the opaque property,
     * that is
     * if this component is opaque, you must completely fill in the background
     * in a non-opaque color. If you do not honor the opaque property you
     * will likely see visual artifacts.
     * <p>
     * The passed in <code>Graphics</code> object might
     * have a transform other than the identify transform
     * installed on it.  In this case, you might get
     * unexpected results if you cumulatively apply
     * another transform.
     *
     * @param g the <code>Graphics</code> object to protect
     * @see #paint
     * @see ComponentUI
     */
    protected void paintComponent(Graphics g)
    {
      if (backgroundPainter != null) {
          Graphics2D g2 = (Graphics2D)g.create();
          Insets ins = this.getInsets();
          g2.translate(ins.left, ins.top);
          backgroundPainter.paint(g2, this, 
                  this.getWidth()  - ins.left - ins.right,
                  this.getHeight() - ins.top  - ins.bottom);
          g2.dispose();
      } else {
          super.paintComponent(g);
      }
    }
    
    /**
     * Specifies a Painter to use to paint the background of this JXPanel.
     * If <code>p</code> is not null, then setOpaque(false) will be called
     * as a side effect. A component should not be opaque if painters are
     * being used, because Painters may paint transparent pixels or not
     * paint certain pixels, such as around the border insets.
     */
    public void setBackgroundPainter(Painter p)
    {
        Painter old = getBackgroundPainter();
        this.backgroundPainter = p;
        
        if (p != null) {
            setOpaque(false);
        }
        
        firePropertyChange("backgroundPainter", old, getBackgroundPainter());
        repaint();
    }
    
    public Painter getBackgroundPainter() {
        return backgroundPainter;
    }    
    /**
     * Uses the DividerPainter (if any) to paint each Divider that
     * overlaps the clip Rectangle.  This is done after the call to
     * <code>super.paintChildren()</code> so that Dividers can be 
     * rendered "on top of" the children.
     * <p>
     * {@inheritDoc}
     */
    protected void paintChildren(Graphics g) {
      super.paintChildren(g);
      DividerPainter dp = getDividerPainter();
      Rectangle clipR = g.getClipBounds();
      if ((dp != null) && (clipR != null)) {
        MultiSplitLayout msl = getMultiSplitLayout();
        if ( msl.hasModel()) {
          for(Divider divider : msl.dividersThatOverlap(clipR)) {
            Rectangle bounds = divider.getBounds();
            Graphics cg = g.create( bounds.x, bounds.y, bounds.width, bounds.height );
            try {
              dp.paint((Graphics2D)cg, divider, bounds.width, bounds.height );
            } finally {
              cg.dispose();
            }
          }
        }
      }
    }

    private boolean dragUnderway = false;
    private MultiSplitLayout.Divider dragDivider = null;
    private Rectangle initialDividerBounds = null;
    private boolean oldFloatingDividers = true;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private int dragMin = -1;
    private int dragMax = -1;
    
    private void startDrag(int mx, int my) {
	requestFocusInWindow();
	MultiSplitLayout msl = getMultiSplitLayout();
	MultiSplitLayout.Divider divider = msl.dividerAt(mx, my);
	if (divider != null) {
	    MultiSplitLayout.Node prevNode = divider.previousSibling();
	    MultiSplitLayout.Node nextNode = divider.nextSibling();
	    if ((prevNode == null) || (nextNode == null)) {
		dragUnderway = false;
	    }
	    else {
		initialDividerBounds = divider.getBounds();
		dragOffsetX = mx - initialDividerBounds.x;
		dragOffsetY = my - initialDividerBounds.y;
		dragDivider  = divider;
		Rectangle prevNodeBounds = prevNode.getBounds();
		Rectangle nextNodeBounds = nextNode.getBounds();
		if (dragDivider.isVertical()) {
		    dragMin = prevNodeBounds.x;
		    dragMax = nextNodeBounds.x + nextNodeBounds.width;
		    dragMax -= dragDivider.getBounds().width;
		}
		else {
		    dragMin = prevNodeBounds.y;
		    dragMax = nextNodeBounds.y + nextNodeBounds.height;
		    dragMax -= dragDivider.getBounds().height;
		}
		oldFloatingDividers = getMultiSplitLayout().getFloatingDividers();
		getMultiSplitLayout().setFloatingDividers(false);
		dragUnderway = true;
	    }
	}
	else {
	    dragUnderway = false;
	}
    }

    private void repaintDragLimits() {
	Rectangle damageR = dragDivider.getBounds();
	if (dragDivider.isVertical()) {
	    damageR.x = dragMin;
	    damageR.width = dragMax - dragMin;
	}
	else {
	    damageR.y = dragMin;
	    damageR.height = dragMax - dragMin;
	}
	repaint(damageR);
    }

    private void updateDrag(int mx, int my) {
	if (!dragUnderway) {
	    return;
	}
	Rectangle oldBounds = dragDivider.getBounds();
	Rectangle bounds = new Rectangle(oldBounds);
	if (dragDivider.isVertical()) {
	    bounds.x = mx - dragOffsetX;
	    bounds.x = Math.max(bounds.x, dragMin);
	    bounds.x = Math.min(bounds.x, dragMax);
	}
	else {
	    bounds.y = my - dragOffsetY;
	    bounds.y = Math.max(bounds.y, dragMin);
	    bounds.y = Math.min(bounds.y, dragMax);
	}
	dragDivider.setBounds(bounds);
	if (isContinuousLayout()) {
	    revalidate();
	    repaintDragLimits();
	}
	else {
	    repaint(oldBounds.union(bounds));
	}
    }

    private void clearDragState() {
	dragDivider = null;
	initialDividerBounds = null;
	oldFloatingDividers = true;
	dragOffsetX = dragOffsetY = 0;
	dragMin = dragMax = -1;
	dragUnderway = false;
    }

    private void finishDrag(int x, int y) {
	if (dragUnderway) {
	    clearDragState();
	    if (!isContinuousLayout()) {
		revalidate();
		repaint();
	    }
	}
    }
    
    private void cancelDrag() {       
	if (dragUnderway) {
	    dragDivider.setBounds(initialDividerBounds);
	    getMultiSplitLayout().setFloatingDividers(oldFloatingDividers);
	    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    repaint();
	    revalidate();
	    clearDragState();
	}
    }

    private void updateCursor(int x, int y, boolean show) {
	if (dragUnderway) {
	    return;
	}
	int cursorID = Cursor.DEFAULT_CURSOR;
	if (show) {
	    MultiSplitLayout.Divider divider = getMultiSplitLayout().dividerAt(x, y);
	    if (divider != null) {
		cursorID  = (divider.isVertical()) ? 
		    Cursor.E_RESIZE_CURSOR : 
		    Cursor.N_RESIZE_CURSOR;
	    }
	}
	setCursor(Cursor.getPredefinedCursor(cursorID));
    }


    private class InputHandler extends MouseInputAdapter implements KeyListener {

	public void mouseEntered(MouseEvent e) {
	    updateCursor(e.getX(), e.getY(), true);
	}

	public void mouseMoved(MouseEvent e) {
	    updateCursor(e.getX(), e.getY(), true);
	}

	public void mouseExited(MouseEvent e) {
	    updateCursor(e.getX(), e.getY(), false);
	}

	public void mousePressed(MouseEvent e) {
	    startDrag(e.getX(), e.getY());
	}
	public void mouseReleased(MouseEvent e) {
	    finishDrag(e.getX(), e.getY());
	}
	public void mouseDragged(MouseEvent e) {
	    updateDrag(e.getX(), e.getY());	    
	}
        public void keyPressed(KeyEvent e) { 
	    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		cancelDrag();
	    }
	}
        public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) { }
    }

    public AccessibleContext getAccessibleContext() {
        if( accessibleContext == null ) {
            accessibleContext = new AccessibleMultiSplitPane();
        }
        return accessibleContext;
    }
    
    protected class AccessibleMultiSplitPane extends AccessibleJPanel {
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SPLIT_PANE;
        }
    }
}
