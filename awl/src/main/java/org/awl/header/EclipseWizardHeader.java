/* =============================================================================
 * AWL : Another Wizard Library
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
package org.awl.header;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.awl.ImagePanel;
import org.awl.message.Message;
import org.awl.message.MessageLevel;
import org.awl.message.MessageUtilities;

/**
 *
 * Wizard header that display messages
 * in an other panel that come from the bottom of the component representing the header
 * and which go up progressively
 *
 * @author alexis
 */
public class EclipseWizardHeader extends DefaultWizardHeader implements ActionListener,
                                                                        PropertyChangeListener
{
    /** layered pane */
    private JLayeredPane layer                 = null;
    
    /** panel that support the label displaying the message */
    private ImagePanel   layeredPanel          = null;
    
    /** timer */
    private Timer        timer                 = null;
    
    /** decalage lors de l'affichage ou du d�saffichage du panneau de message */
    private int          scrollUnit            = 2;
    
    /** decalage horizontal du panneau de message par rapport au panneau du header */
    private int          decX                  = 2;
    
    /** decalage vertical du panneau de message par rapport au panneau du header */
    private int          decY                  = 2;
    
    /** le mode d'action du timer
     *  0 --> mont�e
     *  other --> descente
     */
    private int          timerMode             = 1;
    
    /** canal alpha du panneau de message
     *  si non null, cela permet d'ajouter un effet de transparence au panneau de message
     */
    private Integer      msgPaneAlphaComposite = null;
    
    /** rond indice */
    private int          roundIndice           = 15;
    
    /** Creates a new instance of EclipseWizardHeader */
    public EclipseWizardHeader()
    {
        this(20, 2, 2);
    }
    
    /** Creates a new instance of EclipseWizardHeader
     *  @param delay the delay of the timer used to show the message panel
     *  @param layerDecX le decalage horizontal du layer supportant le panneau de message vis-�-vis du panneau du header
     *  @param layerDecY le decalage vertical du layer supportant le panneau de message vis-�-vis du panneau du header
     */
    public EclipseWizardHeader(int delay, int layerDecX, int layerDecY)
    {
        super();
        
        this.timer = new Timer(delay, this);
        
        this.decX = layerDecX;
        this.decY = layerDecY;
        
        this.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                int gap = 0;
//                int height = layer.getSize().height;
                layer.setBounds(decX, decY, 0, 0);
                
//                if ( height > 0 )
                {   layer.setSize(getSize()); }
		layer.setPreferredSize(getSize());
                
                /** on doit replacer le panneau de message pour s'assurer
                 *  que s'il etait invisible, il l'est encore
                 *  s'il etait visible, il le sera toujours dans une position preferentielle
                 */
                Point point = null;
                if ( isMessagePanelCompletelyDisplayed() )
                {
                    point = computeCompletelyDisplayedPosition();
                }
                else if ( isMessagePanelCompletelyHidden() )
                {
                    point = computeCompletelyHiddenPosition();
                }
                
                if ( point != null )
                {
                    layeredPanel.setBounds(point.x, point.y, getOptimalWidthForMsgPane(), layeredPanel.getBounds().height);
		    // must call setSize ?
                }
            }
        });
	        
        this.getMessageLabel().setVisible(true);
        
        this.layer = new JLayeredPane();
        
        this.layeredPanel = new ImagePanel()
        {   
            public void paintComponent(Graphics g)
            {
                if ( getMessagePanelAlphaComposite() != null )
                {
                    Color c = g.getColor();
                    
                    if ( c != null )
                    {
                        Color d = this.getBackground();
                        int alpha = getMessagePanelAlphaComposite().intValue();
                        
                        Color newC = new Color(d.getRed(), d.getGreen(), d.getBlue(), alpha );
                        g.setColor(newC);
                        
                        Rectangle rec = g.getClipBounds();
                        
                        g.fillRoundRect(rec.x, rec.y, rec.width, rec.height, roundIndice, roundIndice);
                        
                        if ( rec.height > roundIndice )
                        {
                            g.fillRect(rec.x, rec.y + rec.width - roundIndice, roundIndice, rec.height);
                        }
                    }
                    
                    g.setColor(c);
                }
                
                super.paintComponent(g);
            }
        };
	
	this.layeredPanel.setBackgroundImage(null);
	
	this.setMessagePanelAlphaComposite(new Integer(200));
        
        BoxLayout layeredLayout = new BoxLayout(this.layeredPanel, BoxLayout.LINE_AXIS);
        this.layeredPanel.setLayout(layeredLayout);
	
	Container container = this.getMessageLabel().getParent();
	if ( container != null )
	{
	    container.remove(this.getMessageLabel());
	}
        
        this.layer.add(this.layeredPanel, 0);
        
//	this.getMessageLabel().setBounds(0, 0, this.getMessageLabel().getPreferredSize().width,
//		this.getMessageLabel().getPreferredSize().height);
        this.layer.setLayer(this.getMessageLabel(), 0);//JLayeredPane.POPUP_LAYER);
        
        this.getMessageLabel().setVerticalTextPosition(SwingConstants.TOP);
        this.getMessageLabel().setAlignmentY(0.0f);
        this.getMessageLabel().setAlignmentX(0.0f);
        this.getMessageLabel().setVerticalAlignment(SwingConstants.TOP);
        this.getMessageLabel().setHorizontalAlignment(SwingConstants.LEFT);
        this.getMessageLabel().setOpaque(false);
        
        this.getMessageLabel().addPropertyChangeListener("text", this);
        this.getMessageLabel().addPropertyChangeListener("icon", this);
        
        this.layeredPanel.setVisible(true);
        
//        this.layeredPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
//                                                                            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        this.layeredPanel.setBorder(BorderFactory.createCompoundBorder(new RoundedLineBorder(Color.BLACK, 1, true, roundIndice),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        
        this.layeredPanel.add(this.getMessageLabel());
        
        this.layer.setAlignmentY(0.0f);
        this.layer.setAlignmentX(0.0f);
        
        this.layer.setBounds(0, 0, 0, 0);
        this.layeredPanel.setBounds(0, 0, 400, 10000);
	
//	GridBagConstraints gbc = new GridBagConstraints();
//	gbc.gridx = 1;
//	gbc.gridy = 1;
//	gbc.gridheight = 1000;
//	gbc.gridwidth = 1000;
//	gbc.fill = GridBagConstraints.BOTH;
        
//	this.layer.setMinimumSize(new java.awt.Dimension(10000, 10000));
	this.layer.setPreferredSize(this.getSize());
        this.add(this.layer, null);//, gbc);
        
	/** add with index instead of setComponentZOrder because of the compilation in 1.4 */
	this.add(this.layer, 0);
//        this.setComponentZOrder(this.layer, 0);
        
//        this.addMouseListener(new MouseAdapter()
//        {
//            public void mouseClicked(MouseEvent e)
//            {
//		System.out.println("this size               : " + getSize());
//                System.out.println("layer bounds            : " + layer.getBounds());
//                System.out.println("layered panel bounds    : " + layeredPanel.getBounds());
//                System.out.println("message label bounds    : " + getMessageLabel().getBounds());
//		System.out.println("message label pref size : " + getMessageLabel().getPreferredSize());
//            }
//        });
    }

    public void doLayout()
    {
	super.doLayout();
	
	/** HACK : 
	 *  doLayout bounds the layer at a wrong place,
	 *  force to replace it :-(
	 */
	Dimension dim = this.getSize();
	this.layer.setBounds(this.decX, this.decY, dim.width, dim.height);
	// must call setSize ?
//	this.layer.setBounds(this.decX, this.decY, 0, 0);
//	this.layer.setSize(this.getSize());
    }
    
    /** return the panel that support the message label
     *	@return an ImagePanel
     */
    public ImagePanel getMessagePanel()
    {
	return this.layeredPanel;
    }
    
    /** indique le nombre de pixel de d�calage lors du scroll du panneau de message
     *  @param nbPix le nombre de pixel de d�calage lors du scroll du panneau de message
     */
    public void setPixelScrollUnit(int nbPix)
    {
        this.scrollUnit = nbPix;
    }
    
    /** retourne le nombre de pixel de d�calage lors du scroll du panneau de message
     *  @return le nombre de pixel de d�calage lors du scroll du panneau de message
     */
    public int getPixelScrollUnit()
    {
        return this.scrollUnit;
    }
    
    /** initialise la composante alpha permettant d'ajouter un effet de transparence
     *  au panneau de message. mettre � null pour retirer tout effet de transparence
     *  @param alpha la composant alpha comprise entre 0 et 255
     */
    public void setMessagePanelAlphaComposite(Integer alpha)
    {
        if ( alpha != null && (alpha.intValue() < 0 || alpha.intValue() > 255) )
        {
            throw new IllegalArgumentException("the alpha composite must be between 0 and 255");
        }
        
        this.msgPaneAlphaComposite = alpha;
        this.repaint();
	
        this.layeredPanel.setOpaque( this.getMessagePanelAlphaComposite() == null );
    }
    
    /** retourne la composante alpha permettant d'ajouter un effet de transparence
     *  au panneau de message. null pour indique 'aucun effet de transparence'.
     *  @return un entier
     */
    public Integer getMessagePanelAlphaComposite()
    {
        return this.msgPaneAlphaComposite;
    }
    
    /** m�thode permettant d'afficher progressivement le panneau de message si celui-ci est affich�
     *  ou de le faire disparaitre progressivement si celui-ci est affich�
     */
    public void animate()
    {
	Runnable runnable = new Runnable()
	{
	    public void run()
	    {
		int height = layer.getSize().height;

		if ( height > 0 )
		{
		    timer.start();
		}
		else
		{
		    /** on lance le timer permettant de remonter progressivement le panneau inclut dans le layer */
		    timer.start();
		}

		timerMode = (timerMode == 0 ? 1 : 0);
	    }
	};
	
	if ( SwingUtilities.isEventDispatchThread() )
	{
	    runnable.run();
	}
	else
	{
	    SwingUtilities.invokeLater(runnable);
	}
    }
    
    /** return true if the message panel is completely displayed
     *  @return true if the message panel is completely displayed
     */
    public boolean isMessagePanelCompletelyDisplayed()
    {
        return (this.timerMode == 0 && ! this.timer.isRunning() );
    }
    
    /** return true if the display of the message panel is in progress
     *  @return true if the display of the message panel is in progress
     */
    public boolean isMessagePanelDisplayInProgress()
    {
        return (this.timerMode == 0 && this.timer.isRunning() );
    }
    
    /** return true if the message panel is completely hidden
     *  @return true if the message panel is completely hidden
     */
    public boolean isMessagePanelCompletelyHidden()
    {
        return (this.timerMode != 0 && ! this.timer.isRunning() );
    }
    
    /** return true if hiding the message panel is in progress
     *  @return true if hiding the message panel is in progress
     */
    public boolean isMessagePanelHideInProgress()
    {
        return (this.timerMode != 0 && this.timer.isRunning() );
    }
    
    /** determine the bounds of the message panel to set it as completely hidden
     *  @return a Point
     */
    protected Point computeCompletelyHiddenPosition()
    {
        Point point = new Point();
        
        int height = this.getSize().height;
        
        point.x      = 0;
        point.y      = height - this.decY + 1;
        
        return point;
    }
    
    /** determine the bounds of the message panel to set it as completely displayed
     *  @return a Point
     */
    protected Point computeCompletelyDisplayedPosition()
    {
        Point point = new Point();
        
        int minus =
                    (this.getMessageLabel().getPreferredSize().height +
//                (this.getMessageLabel().getBounds().height +
                (this.getMessageLabel().getBorder() == null ? 0 : this.getMessageLabel().getBorder().getBorderInsets(this.getMessageLabel()).top) +
                (this.layeredPanel.getBorder()      == null ? 0 : this.layeredPanel.getBorder().getBorderInsets(this.layeredPanel).top));
        
        point.x      = 0;
        point.y      = Math.max(0, this.getSize().height - this.decX - minus);
        
        int t = (this.getMessageLabel().getBounds().y - this.layeredPanel.getBounds().y);
        if ( t > 0 )
        {
            point.y += t;
        }
        
        return point;
    }
    
    /** action venant du timer */
    public void actionPerformed(ActionEvent e)
    {
        if ( ! this.layer.isVisible() )
        {
            this.layer.setVisible(true);
        }
        
	boolean shouldStopTimer = this.shouldStopTimer();
        if ( shouldStopTimer )
        {
            this.timer.stop();
        }
        else
        {
            Rectangle newRec = new Rectangle(this.layeredPanel.getBounds());
            
            if ( this.timerMode == 0 )
            {
                /** on doit remonter le panneau */
                newRec.y -= this.getPixelScrollUnit();
                if ( newRec.y < 0 )
                {
                    newRec.y = 0;
                }
                
                if ( newRec.y == 0 )
                {
                    this.timer.stop();
                }
            }
            else
            {
                newRec.y += this.getPixelScrollUnit();
            }
            newRec.width = this.getOptimalWidthForMsgPane();
            
            this.layeredPanel.setBounds(newRec);
	    // must call setSize ?
//	    this.layeredPanel.setSize(newRec.width, newRec.height);
	    
        }
    }
    
    /** methode permettant de determiner la largeur optimal pour le panneau supportant le message de maniere
     *  a ce qu'il soit le moins large possible mais permette une bonne visibilite du message
     */
    protected int getOptimalWidthForMsgPane()
    {
	Insets insets = this.layeredPanel.getBorder().getBorderInsets(this.getMessageLabel());
        int result = Math.min(this.getSize().width - (2 * this.decX),
			      this.getMessageLabel().getPreferredSize().width + insets.left + insets.right);
        
        return result;
    }
    
    /* m�thodes permettant la d�tection de l'arr�t du timer
     *  @return true si le timer doit �tre arr�t�
     */
    private boolean shouldStopTimer()
    {
        /** on doit arreter le timer si :
         *  - on est en mode 0 et l'integralite du label de message est visible
         *  - on est en mode > 0 et le panneau de message est actuellement en dehors des bords du header
         */
        boolean shouldStop = false;
        
        /** on determine la hauteur limite devant avoir ce panneau pour
         *  respecter la contrainte de visibilite du panneau de message
         */
        int height = this.layer.getLocation().y + this.layeredPanel.getBounds().y +
                this.getMessageLabel().getBounds().y;
        
        if ( this.timerMode == 0 )
        {
            height += this.getMessageLabel().getBounds().height;
            
            if ( getSize().height >= height )
            {
                shouldStop = true;
            }
        }
        else
        {
            height -= this.layeredPanel.getBorder().getBorderInsets(this.layeredPanel).top;
            
            if ( getSize().height < height )
            {
                shouldStop = true;
            }
        }
        
        return shouldStop;
    }
    
    
    public void propertyChange(PropertyChangeEvent evt)
    {
        if ( evt.getSource() == this.getMessageLabel() )
        {
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    if ( timerMode == 0 )
		    {
			/** if the timer is running, the height
			 *  of the message panel is perhaps to large for the content of the message label
			 *  if it is so, we set the bounds of the message panel without animation
			 *  else, we continue timer action
			 */
			boolean setBoundsWithoutAnimation = true;
			
			if ( timer.isRunning() )
			{
			    Point p = computeCompletelyDisplayedPosition();
			    if ( layeredPanel.getBounds().y > p.y )
			    {
				setBoundsWithoutAnimation = false;
			    }
			}
			
			if ( setBoundsWithoutAnimation )
			{
			    if ( timer.isRunning() )
			    {
				timer.stop();
			    }
			    
			    /** the message panel is up and the timer does not run
			     *  we must determine the new bounds for the message panel
			     *  and apply it without animation
			     */
			    int optimalWidth = getOptimalWidthForMsgPane();

			    Point p = computeCompletelyDisplayedPosition();
			    layeredPanel.setBounds(p.x, p.y, optimalWidth, 10000);
			    // must call setSize ?
			}
		    }
		}
	    };
	    
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
    
    /** ########################################################################
     *  #################### MessageHandler implementation #####################
     *  ######################################################################## */
    
    /** return the icon to use for the given message level
     *	@param level a MessageLevel
     *	@return an Icon or null
     */
    protected Icon getIconForLevel(MessageLevel level)
    {
	return MessageUtilities.getIconForLevel(level);
    }
    
    /** process a message
     *	@param message a Message
     */
    public void processMessage(final Message message)
    {
	Runnable runnable = new Runnable()
	{
	    public void run()
	    {
		if ( message == null )
		{
		    if ( timerMode == 0 )
		    {
			animate();
		    }
		}
		else
		{
		    if ( timerMode != 0 && timer.isRunning() )
		    {
			/** we receive a message but the message panel is going down
			 *  set timerMode to 1
			 */
			timerMode = 0;
		    }
		    
		    /** update message label with message content and display if necessary the message */
		    getMessageLabel().setText(message.getContent());
		    getMessageLabel().setIcon(getIconForLevel(message.getLevel()));

		    if ( timerMode != 0 )
		    {
			animate();
		    }
		}
	    }
	};
	    
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
