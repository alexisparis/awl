/* =============================================================================
 * AWL : Another DefaultWizard Library
 * =============================================================================
 *
 * Project Lead:  Alexis Paris
 *
 * (C) Copyright 2006, by Alexis Paris.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
package org.awl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.exception.PagesCycleDetectedException;
import org.awl.header.WizardHeader;
import org.awl.message.Message;
import org.awl.message.MessageHandler;
import org.awl.message.MessageUtilities;

/**
 * 
 * DefaultWizard dialog.
 *
 *  this wizard propose a quick summary of the registered pages
 *  and an optional static header that could display the description of the current page,
 *  an icon and a message.
 * 
 * @author alexis
 */
public class DefaultWizard extends Wizard implements SummarizedWizard
{
    /** label that display the hierarchy of page */
    private JEditorPane            summaryPane      = null;
    
    /** panel that contains the summary */
    private ImagePanel             summaryPanel     = null;
    
    /** separator between summary and pagePanel */
    private JSeparator             verSeparator     = null;
    
    /** separator between content and buttonPanel */
    private JSeparator             horSeparator     = null;
    
    /** default html size for the summary */
    private int                    summaryFontSize  = -1;
    
    /** default font color for the summary */
    private Color                  summaryFontColor = Color.BLACK;
    
    /** header panel */
    private WizardHeader      headerPanel      = null;
    
    /**
     * Creates a new instance of DefaultWizard
     * 
     * @param owner a Frame
     *	@param modal true to set the dialog modal
     */
    public DefaultWizard(Frame owner, boolean modal)
    {   super(owner, modal);
	
	this.placeComponents();
    }
    
    /**
     * Creates a new instance of modal DefaultWizard 
     * 
     * @param owner a Frame
     */
    public DefaultWizard(Frame owner)
    {   this(owner, true); }
    
    /**
     * Creates a new instance of DefaultWizard
     * 
     * @param dialog a Dialog
     *	@param modal true to set the dialog modal
     */
    public DefaultWizard(Dialog dialog, boolean modal)
    {   super(dialog, modal);
	
	this.placeComponents();
    }
    
    /**
     * Creates a new instance of modal DefaultWizard
     * 
     * @param dialog a Dialog
     */
    public DefaultWizard(Dialog dialog)
    {   this(dialog, true); }
    
    /* called on initialization after components initialization
     *	to add them to the content pane of the dialog
     */
    protected void placeComponents()
    {
        this.getContentPane().setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0f;
        gbc.weighty = 1.0f;
        gbc.fill  = gbc.BOTH;
	
	this.summaryPane = new JEditorPane()
	{            
	    public String getToolTipText(MouseEvent e)
            {
		String result = super.getToolTipText(e);
		
		try
		{
		    /** on regarde le texte en dessous de la souris */
		    int position = this.getUI().viewToModel(this, e.getPoint());
		    Rectangle rec = this.getUI().modelToView(this, position);
		    
		    FontMetrics metrics = this.getFontMetrics(this.getFont());
		    int width = metrics.stringWidth("M");
		    
		    if ( Math.abs(rec.x - e.getPoint().x) < width )
		    {
			/** on doit prendre la ligne du document ou
			 * intervient le caractère
			 *  à la position position
			 */
			Element elt = ((HTMLDocument)this.getDocument()).getCharacterElement(position);
			
			try
			{
			    int ends = elt.getEndOffset() - elt.getStartOffset();
			    
			    String text = this.getDocument().getText(elt.getStartOffset(), ends);
			    if ( text.trim().length() > 0 )
			    {
				/** we are now sure that the mouse is under text characters
				 *  each element of the summary is caracterized by an id starting from 1 to ...
				 *  so, we search this id to discover the WizardPageDescriptor associated
				 */
				int index = -1;
				
				int pageCount = getModel().getPageCount();

				for(int i = 1; i <= pageCount; i++)
				{
				    Element current = ((HTMLDocument)this.getDocument()).getElement(Integer.toString(i));

				    if ( position < current.getStartOffset() )
				    {
					index = i - 1;
					break;
				    }
				    else if ( i == pageCount )
				    {
					index = pageCount;
				    }
				}
				
				if ( index >= 1 )
				{
				    index--;
				    
				    List path = null;
				    try
				    {   path = getModel().getCurrentPathDescriptor(); }
				    catch (PagesCycleDetectedException ex)
				    {   /* aie */ }

				    if ( path != null )
				    {
					if ( index < path.size() )
					{
					    Object current = path.get(index);
					    
					    if ( current instanceof WizardPageDescriptor )
					    {
						result = ((WizardPageDescriptor)current).getDescription();
					    }
					}
				    }
				}
			    }
			}
			catch (BadLocationException ex)
			{
			    ex.printStackTrace();
			}
		    }
		    
		}
		catch(Exception ex)
		{
		    ex.printStackTrace();
		}
		
		return result;
	    }
	};
	/** activate tooltip */
	ToolTipManager.sharedInstance().registerComponent(this.summaryPane);
	
	this.summaryPane.setOpaque(false);
	this.summaryPane.setEditorKit(new HTMLEditorKit());
	this.summaryPane.setEditable(false);
	
        JPanel contentPanel = new JPanel();
        BoxLayout contentLayout = new BoxLayout(contentPanel, BoxLayout.LINE_AXIS);
        contentPanel.setLayout(contentLayout);
        
        this.summaryPanel = new ImagePanel();
	GridBagLayout summaryLayout = new GridBagLayout();
	this.summaryPanel.setLayout(summaryLayout);
        this.getSummaryPanel().setBorder(null);
	JScrollPane scroll = new JScrollPane(this.getPageSummary(), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	scroll.setOpaque(false);
	scroll.getViewport().setOpaque(false);
	
	scroll.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	this.getPageSummary().setBorder(null);
	
	GridBagConstraints summaryConstraints = new GridBagConstraints();
	summaryConstraints.gridx   = 1;
	summaryConstraints.gridy   = 1;
	summaryConstraints.weightx = 1.0f;
	summaryConstraints.weighty = 1.0f;
	summaryConstraints.fill    = GridBagConstraints.BOTH;
	
        this.getSummaryPanel().add(scroll, summaryConstraints);
        
        contentPanel.add(this.getSummaryPanel());
        
        this.verSeparator = new JSeparator(SwingConstants.VERTICAL);
        this.verSeparator.getInsets().left  += 20;
        this.verSeparator.getInsets().right += 20;
        contentPanel.add(this.verSeparator);
	
	JPanel subPanel = new JPanel();
	subPanel.setBackground(Color.GREEN);
	GridBagLayout subPanelLayout = new GridBagLayout();
	subPanel.setLayout(subPanelLayout);
	
	GridBagConstraints gbc2 = new GridBagConstraints();
	
	gbc2.gridy = 3;
	gbc2.weightx = 1.0f;
	gbc2.weighty = 1.0f;
	gbc2.fill = GridBagConstraints.BOTH;
	subPanel.add(this.getPagesPanel(), gbc2);
        
        contentPanel.add(subPanel);
        
        this.getContentPane().add(contentPanel, gbc);
        
        this.horSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        this.horSeparator.getInsets().left  += 20;
        this.horSeparator.getInsets().right += 20;
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0f;
        gbc.weighty = 0.0f;
        gbc.fill  = gbc.HORIZONTAL;
        this.getContentPane().add(this.horSeparator, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0f;
        gbc.weighty = 0.0f;
        gbc.fill  = gbc.HORIZONTAL;
        this.getContentPane().add(this.getButtonsContainer(), gbc);
        
        Font f = this.getBackButton().getFont().deriveFont(8);
        this.setButtonsFont(f);
    }
    
    /**
     * return the WizardHeader or null
     * 
     * 
     * @return a WizardHeader
     */
    public WizardHeader getHeader()
    {
	return this.headerPanel;
    }
    
    /**
     * set the WizardHeader
     * 
     * 
     * @param headerPanel a WizardHeader
     */
    public void setHeader(WizardHeader headerPanel)
    {
	if ( this.headerPanel != headerPanel )
	{
	    Container container = this.getPagesPanel().getParent();
	    if ( container.getComponentCount() > 1 )
	    {
		for(int i = 0; container.getComponentCount() > 1; i++)
		{
		    Component c = container.getComponent(i);
		    
		    if ( c != this.getPagesPanel() )
		    {
			container.remove(c);
			i--;
		    }
		}
	    }
	    
	    this.headerPanel = headerPanel;
	    
	    if ( this.headerPanel != null )
	    {
		GridBagConstraints gbc2 = new GridBagConstraints();
		
		container.remove(this.getPagesPanel());

		gbc2.gridy = 1;
		gbc2.weightx = 1.0f;
		gbc2.weighty = 0.0f;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		container.add(this.headerPanel.getComponent(), gbc2);
			
		gbc2.gridy = 2;
		gbc2.weightx = 1.0f;
		gbc2.weighty = 0.0f;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		JSeparator c = new JSeparator(SwingConstants.HORIZONTAL);
		container.add(c, gbc2);
	
		gbc2.gridy = 3;
		gbc2.weightx = 1.0f;
		gbc2.weighty = 1.0f;
		gbc2.fill = GridBagConstraints.BOTH;
		container.add(this.getPagesPanel(), gbc2);
		
		((javax.swing.JComponent)container).revalidate();
		((javax.swing.JComponent)container).repaint();
		
		this.updateHeaderPanel();
	    }
	}
    }
    
    /**
     * set the WizardHeader
     * @param headerPanel a WizardHeader
     * @deprecated use setHeader
     */
    public void setHeaderPanel(WizardHeader headerPanel)
    {
	this.setHeader(headerPanel);
    }
    
    /** update the headers panel states */
    protected void updateHeaderPanel()
    {
	if( this.getHeader() != null )
	{
	    this.getHeader().displayDescriptorInformation(this.getModel().getCurrentDescriptor());
	}
    }
    
    /** ask wizard to show this message by its own
     *	@param message a Message or null to hide current message
     */
    protected void _displayMessage(Message message)
    {
	/* perhaps, we use a WizardHeader that is a MessageHandler but it not registered as MessageHandler
	 *  if so, we ask header to process message, else, we simply use a Popup to display the message
	 */
	WizardHeader header = this.getHeader();
	
	if ( header != null && header instanceof MessageHandler )
	{
	    ((MessageHandler)header).processMessage(message);
	}
	else
	{
	    if ( message != null )
	    {
		JPopupMenu popup = new JPopupMenu();

		JLabel label = new JLabel(message.getContent());
		
		Icon icon = MessageUtilities.getIconForLevel(message.getLevel());
		if ( icon != null )
		{
		    label.setIcon(icon);
		}
		
		popup.add(label);
		popup.setInvoker(this.getPagesPanel());

		/** determine where to display popup */
		Point point = new Point();
		point.x = this.getPagesPanel().getLocationOnScreen().x;
		point.y = this.getPagesPanel().getLocationOnScreen().y + this.getPagesPanel().getSize().height - popup.getPreferredSize().height;

		popup.setLocation(point);
		popup.setVisible(true);
	    }
	}
    }
    
    /** indicate to the wizard that a property change on a descriptor
     *	@param descriptor the descritor that changed
     *	@param evt a PropertyChangeEvent reflecting the modification
     */
    public void descriptorChanged(WizardPageDescriptor descriptor, PropertyChangeEvent evt)
    {
	super.descriptorChanged(descriptor, evt);
	
	if ( descriptor == this.getModel().getCurrentDescriptor() )
	{
	    if ( this.getHeader() != null )
	    {
		this.getHeader().displayDescriptorInformation(descriptor);
	    }
	}
    }
    
    /** return the JEditorPane that display the page summmary
     *  @return a JEditorPane
     */
    public JEditorPane getPageSummary()
    {   return this.summaryPane; }
    
    /** return the panel that support the summary
     *  @return a JPanel
     */
    public ImagePanel getSummaryPanel()
    {   return this.summaryPanel; }
    
    /** return the separator between summary and page panel
     *  @return a JSeparator
     */
    public JSeparator getVerticalSeparator()
    {   return this.verSeparator; }
    
    /** return the separator between button panel and content
     *  @return a JSeparator
     */
    public JSeparator getHorizontalSeparator()
    {   return this.horSeparator; }
    
    /** indicate to display the summary or not
     *  @param summaryVisible true to display the summary
     */
    public void setSummaryVisible(boolean summaryVisible)
    {
        this.getVerticalSeparator().setVisible(summaryVisible);
        this.getSummaryPanel().setVisible(summaryVisible);
    }

    /** return the html font size to use for the summary
     *	@return an integer
     */
    public int getSummaryFontSize()
    {
	return summaryFontSize;
    }

    /** initialize the html font size to use in the summary
     *	@param summaryFontSize an integer
     */
    public void setSummaryFontSize(int summaryFontSize)
    {
	if ( summaryFontSize != this.getSummaryFontSize() )
	{
	    this.summaryFontSize = summaryFontSize;
	    
	    this.refreshSummary();
	}
    }

    /** return the color to use as foreground color for the summary
     *	@return a Color
     */
    public Color getSummaryFontColor()
    {
	return summaryFontColor;
    }

    /** initalize the color to use as foreground color for the summary
     *	@param summaryFontColor a Color
     */
    public void setSummaryFontColor(Color summaryFontColor)
    {
	if ( summaryFontColor == null )
	{
	    throw new IllegalArgumentException("color non null to provide");
	}
	
	if ( ! summaryFontColor.equals(this.getSummaryFontColor()) )
	{
	    this.summaryFontColor = summaryFontColor;
	    
	    this.refreshSummary();
	}
    }
    
    /** method that create a String representing the summary
     *  @return the summary
     */
    protected String createSummary()
    {   StringBuffer summary = new StringBuffer();
        
        List path = null;
        
        try
        {   path = this.getModel().getCurrentPathDescriptor(); }
        catch (PagesCycleDetectedException ex)
        {   throw new RuntimeException(ex); }
        
        for(int i = 0; i < path.size(); i++)
        {   WizardPageDescriptor current = (WizardPageDescriptor)path.get(i);
            boolean isCurrent = current == this.getModel().getCurrentDescriptor();
            
            summary.append(this.descriptorToHtml(current, (i + 1), isCurrent));
	    
	    if ( i < path.size() - 1)
	    {
		summary.append("<br>");
	    }
        }
	
	StringBuffer buffer = new StringBuffer(6);
	
	String r = Integer.toHexString(this.getSummaryFontColor().getRed());
	if ( r.length() == 1 )
	{
	    buffer.append("0");
	}
	buffer.append(r);
	String g = Integer.toHexString(this.getSummaryFontColor().getGreen());
	if ( g.length() == 1 )
	{
	    buffer.append("0");
	}
	buffer.append(g);
	String b = Integer.toHexString(this.getSummaryFontColor().getBlue());
	if ( b.length() == 1 )
	{
	    buffer.append("0");
	}
	buffer.append(b);
                
        summary.insert(0, "<html><font size=\"" + this.getSummaryFontSize() +"\" color=\"#" + buffer.toString() + "\">");
        summary.append("</font></html>");
        
        return summary.toString();
    }
    
    /** method that returns the html representation of a WizardPageDescriptor
     *  @param descriptor the WizardPageDescriptor
     *  @param index the index of the descriptor
     *  @param isCurrent true if the given descriptor is the current descriptor
     */
    protected String descriptorToHtml(WizardPageDescriptor descriptor, int index, boolean isCurrent)
    {   StringBuffer buffer = new StringBuffer();
        
	String title = (descriptor == null ? null : descriptor.getTitle());
	
	/** if title is null, then use description */
	if ( title == null || title.trim().length() == 0 )
	{
	    title = (descriptor == null ? null : descriptor.getDescription());
	}
	
        if ( title == null )
            title = "<i>unknown</i>";
	
	buffer.append("<nobr id=\"" + index + "\">");

        buffer.append(index + ". ");

        if ( isCurrent )
            buffer.append("<u><b>");
        
        buffer.append(title);

        if ( isCurrent )
	    buffer.append("</b></u>");
	
//	buffer.append("\u00A0\u00A0\u00A0\u00A0");
	
	buffer.append("</nobr>");
        
        return buffer.toString();
    }
    
    /* #########################################################################
     * ##################### SummarizedWizard implementation ###################
     * ######################################################################### */
    
    /** refresh summary */
    public void refreshSummary()
    {
	if ( this.getPageSummary() != null )
	{
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    Dimension oldSize = getPageSummary().getSize();

		    String summary = createSummary();
		    getPageSummary().setText(summary);
		    
		    resizeIfNeeded();
		}
	    };
	    
//	    if ( true )
	    if ( SwingUtilities.isEventDispatchThread() )
	    {
		runnable.run();
	    }
	    else
	    {
		SwingUtilities.invokeLater(runnable);
	    }
	}
    }

    /**
     * a new descriptor has been set as current descriptor
     *
     * @param evt a CurrentDescriptorChangedEvent
     */
    public void currentDescriptorChanged(CurrentDescriptorChangedEvent evt)
    {
	/* updateHeaderPanel is called before super
	 *  because in the case new current descriptorblock EDT in aboutToDisplayPanel
	 *  then the header will be updated after provoking to new current descriptor
	 *  to be displayed byt header showing informations about the previous
	 *  current page
	 */
	this.updateHeaderPanel();
	
	super.currentDescriptorChanged(evt);
    }
}
