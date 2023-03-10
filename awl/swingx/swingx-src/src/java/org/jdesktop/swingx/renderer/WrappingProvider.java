/*
 * Created on 08.01.2007
 *
 */
package org.jdesktop.swingx.renderer;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.RolloverRenderer;
import org.jdesktop.swingx.treetable.TreeTableNode;


/**
 * Wrapping ComponentProvider for usage in tree rendering. Handles the icon
 * itself, delegates the node content to the wrappee. Value-based icon and
 * content mapping can be configured by custom <code>IconValue</code>s and
 * <b>StringValue</b>, respectively.
 * <p>
 * 
 * An example of how to configure a file tree by using the system icons and
 * display names
 * 
 * <pre><code>
 * StringValue sv = new StringValue() {
 * 
 *     public String getString(Object value) {
 *         if (value instanceof File) {
 *             return FileSystemView.getFileSystemView().getSystemDisplayName(
 *                     (File) value);
 *         }
 *         return TO_STRING.getString(value);
 *     }
 * 
 * };
 * IconValue iv = new IconValue() {
 * 
 *     public Icon getIcon(Object value) {
 *         if (value instanceof File) {
 *             return FileSystemView.getFileSystemView().getSystemIcon(
 *                     (File) value);
 *         }
 *         return null;
 *     }
 * };
 * TreeCellRenderer r = new DefaultTreeRenderer(iv, sv);
 * tree.setCellRenderer(r);
 * treeTable.setTreeCellRenderer(r);
 * </code></pre>
 * 
 * PENDING: ui specific focus rect variation (draw rect around icon) missing
 * <p>
 */
public class WrappingProvider extends 
    ComponentProvider<WrappingIconPanel>  implements RolloverRenderer {

    protected ComponentProvider wrappee;

    /**
     * Instantiates a WrappingProvider with default LabelProvider.
     * 
     */
    public WrappingProvider() {
        this((ComponentProvider) null);
    }

    /**
     * Instantiates a WrappingProvider with default wrappee. Uses the 
     * given IconValue to configure the icon. 
     * 
     * @param iconValue the IconValue to use for configuring the icon.
     */
    public WrappingProvider(IconValue iconValue, StringValue wrappeeStringValue) {
        this(wrappeeStringValue);
        setToStringConverter(new MappedValue(null, iconValue));
    }

    /**
     * Instantiates a WrappingProvider with default wrappee. Uses the 
     * given IconValue to configure the icon. 
     * 
     * @param iconValue the IconValue to use for configuring the icon.
     */
    public WrappingProvider(IconValue iconValue) {
        this();
        setToStringConverter(new MappedValue(null, iconValue));
    }
   
    /**
     * Instantiates a WrappingProvider with default wrappee configured
     * with the given StringValue. 
     * 
     * PENDING: we have a slight semantic glitch compared to super because
     * the given StringValue is <b>not</b> for use in this provider but for use 
     * in the wrappee!
     * 
     * @param wrappeeStringValue the StringValue to use in the wrappee.
     */
    public WrappingProvider(StringValue wrappeeStringValue) {
        this(new LabelProvider(wrappeeStringValue));
    }

    /**
     * Instantiates a WrappingProvider with the given delegate
     * provider for the node content. If null, a default 
     * LabelProvider will be used. 
     * 
     * @param delegate the provider to use as delegate
     */
    public WrappingProvider(ComponentProvider delegate) {
        super();
        // PENDING JW: this is inherently unsafe - must not call 
        // non-final methods from constructor
        setWrappee(delegate);
        setToStringConverter(StringValue.EMPTY);
    }
    
    /**
     * Sets the given provider as delegate for the node content. 
     * If the delegate is null, a default LabelProvider is set.<p>
     * 
     *  PENDING: rename to setDelegate?
     *  
     * @param delegate the provider to use as delegate. 
     */
    public void setWrappee(ComponentProvider delegate) {
        if (delegate == null) {
            delegate = new LabelProvider();
        }
        this.wrappee = delegate;
        rendererComponent.setComponent(delegate.rendererComponent);
    }

    /**
     * Returns the delegate provider used to render the node content.
     * 
     * @return the provider used for rendering the node content.
     */
    public ComponentProvider getWrappee() {
        return wrappee;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public WrappingIconPanel getRendererComponent(CellContext context) {
        if (context != null) {
            rendererComponent.setComponent(wrappee.rendererComponent);
            Object oldValue = adjustContextValue(context);
            WrappingIconPanel panel = super.getRendererComponent(context);
            wrappee.getRendererComponent(context);
            restoreContextValue(context, oldValue);
            return rendererComponent;
        }
        return super.getRendererComponent(context);
    }

    /**
     * Restores the context value to the old value.
     * 
     * @param context the CellContext to restore.
     * @param oldValue the value to restore the context to.
     */
    protected void restoreContextValue(CellContext context, Object oldValue) {
        context.value = oldValue;
    }

    /**
     * Replace the context's value with the userobject 
     * if it's a treenode. <p>
     * Subclasses may override but must guarantee to return the original 
     * value for restoring. 
     * 
     * @param context the context to adjust
     * @return the old context value
     */
    protected Object adjustContextValue(CellContext context) {
        Object oldValue = context.getValue();
        if (oldValue instanceof DefaultMutableTreeNode) {
            context.value = ((DefaultMutableTreeNode) oldValue).getUserObject();
        } else if (oldValue instanceof TreeTableNode) {
            TreeTableNode node = (TreeTableNode) oldValue;
            context.value = node.getUserObject();
            
        }
        return oldValue;
    }

    @Override
    protected void configureState(CellContext context) {
        rendererComponent.setBorder(BorderFactory.createEmptyBorder());
    }

//    /**
//     * @return
//     */
//    private boolean isBorderAroundIcon() {
//        return Boolean.TRUE.equals(UIManager.get("Tree.drawsFocusBorderAroundIcon"));
//    }

    @Override
    protected WrappingIconPanel createRendererComponent() {
        return new WrappingIconPanel();
    }

    /**
     * {@inheritDoc} <p>
     * 
     * Here: implemented to set the icon.
     */
    @Override
    protected void format(CellContext context) {
        rendererComponent.setIcon(getValueAsIcon(context));
    }

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to fallback to the default icons supplied by the 
     * context if super returns null.
     * 
     * PENDING: make fallback configurable - null icons might be
     *   valid.
     *   
     */
    @Override
    protected Icon getValueAsIcon(CellContext context) {
        Icon icon = super.getValueAsIcon(context);
        if (icon == null) {
            return context.getIcon();
        }
        return icon;
    }
    
    //----------------- implement RolloverController
    

    /**
     * {@inheritDoc}
     */
    public void doClick() {
        if (isEnabled()) {
            ((RolloverRenderer) wrappee).doClick(); 
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return (wrappee instanceof RolloverRenderer) && 
           ((RolloverRenderer) wrappee).isEnabled();
    }


    
}
