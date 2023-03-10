/*
 * $Id: JXHeaderVisualCheck.java,v 1.3 2007/11/10 22:15:38 rah003 Exp $
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
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXHeader.IconPosition;
import org.jdesktop.swingx.test.XTestUtils;

/**
 * Tests previously created to expose known issues of <code>JXHeader</code>, now fixed. 
 * Still need visual inspection from time to time.
 * <p>
 * 
 * @author Jeanette Winzenburg
 */
public class JXHeaderVisualCheck extends InteractiveTestCase {

    /**
     * This issue has been fixed, but remains here (otherwise I get a warning
     * when running this test. Not sure if this JXHeaderIssues should just be
     * removed, or what).
     *
     * Issue #403-swingx: JXHeader doesn't show custom values.
     * <p>
     * 
     * Breaking if values are passed in the constructor.
     */
    public void testTitleInContructor() {
        String title = "customTitle";
        JXHeader header = new JXHeader(title, null);
        // sanity: the property is set
        assertEquals(title, header.getTitle());
        // fishing in the internals ... not really safe, there are 2 labels
        JLabel label = null;
        for (int i = 0; i < header.getComponentCount(); i++) {
            if (header.getComponent(i) instanceof JLabel && !(header.getComponent(i) instanceof JXLabel)) {
                label = (JLabel) header.getComponent(i);
                break;
            }
        }
        assertEquals("the label's text must be equal to the headers title", 
                header.getTitle(), label.getText());
    }

    // ------------------ interactive

    /**
     * #647-swingx JXLabel looses html rendering on font change.
     */
	public void interactiveFontLayoutReset() {
		JFrame f = new JFrame("Header Test");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JXHeader header = new JXHeader("My Title",
				"<html>My short description<br>some text<html>");
		f.add(header, BorderLayout.CENTER);
 
		JButton changeFont = new JButton("Font");
		f.add(changeFont, BorderLayout.SOUTH);
		changeFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						header.setFont(new Font("Tahoma", Font.PLAIN, 18));
					}
				});
			}
		});
	     
		f.setSize(400, 200);
		f.setVisible(true);
	}

    /**
     * Short description in header produces unexpected line wraps in
     * footer.
     * 
     * Note: the frame is not packed to simulate the appframework
     * context.
     */
    public void interactiveHTMLTextWrapShort() {
        JXHeader header = new JXHeader();
        header.setTitle("AlbumManager");
        String headerShort = "An adaption from JGoodies Binding Tutorial in the context"
            + " of BeansBinding/AppFramework. ";
        header.setDescription(headerShort);
        header.setIcon(XTestUtils.loadDefaultIcon());
        JXHeader footer = new JXHeader();
        footer.setTitle("Notes:");
        String footerDescription = "<html>"
                + " <ul> "
                + " <li> code: in the jdnc-incubator, section kleopatra, package appframework."
                + " <li> technique: back the view by a shared presentation model "
                + " <li> technique: veto selection change until editing is completed "
                + " <li> issue: selection of tab should be vetoable "
                + " <li> issue: empty selection should disable editing pane "
                + " </ul> " + " </html>";
        footer.setDescription(footerDescription);

        JComponent panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
        panel.add(footer, BorderLayout.SOUTH);
        JXFrame frame = new JXFrame("html wrapping in SOUTh: short text in NORTH");
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }
    
    /**
     * Long description in header produces expected line-wrap in footer.
     * 
     * Note: the frame is not packed to simulate the appframework
     * context.
     */
    public void interactiveHTMLTextWrapLong() {
        JXHeader header = new JXHeader();
        header.setTitle("AlbumManager");
        String headerLong = "An adaption from JGoodies Binding Tutorial in the context"
                + " of BeansBinding/AppFramework. "
                + "The Tabs show different styles of typical interaction "
                + "setups (in-place editing vs. dialog-editing). ";
        header.setDescription(headerLong);
        header.setIcon(XTestUtils.loadDefaultIcon());
        JXHeader footer = new JXHeader();
        footer.setTitle("Notes:");
        String footerDescription = "<html>"
                + " <ul> "
                + " <li> code: in the jdnc-incubator, section kleopatra, package appframework."
                + " <li> technique: back the view by a shared presentation model "
                + " <li> technique: veto selection change until editing is completed "
                + " <li> issue: selection of tab should be vetoable "
                + " <li> issue: empty selection should disable editing pane "
                + " </ul> " + " </html>";
        footer.setDescription(footerDescription);

        JComponent panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
//        panel.add(new JScrollPane(table));
        panel.add(footer, BorderLayout.SOUTH);
        JXFrame frame = new JXFrame("html wrapping in SOUTh: long text in NORTH");
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    /**
     * Issue #403-swingx: JXHeader doesn't show custom values.
     * <p>
     * 
     * All values are passed in the constructor.
     */
    public void interactiveCustomProperties() {
        URL url = getClass().getResource("resources/images/wellTop.gif");;
        
        assertNotNull(url);
        JPanel p = new JPanel(new BorderLayout());
        JXHeader header = new JXHeader("MyTitle", "MyDescription", new ImageIcon(url));
        header.setIconPosition(IconPosition.LEFT);
        p.add(header);
        // added just to better visualize bkg gradient in the JXHeader.
        p.add(new JLabel("Reference component"), BorderLayout.SOUTH);
        showInFrame(p, "JXHeader with custom properties");
    }
    
    /**
     * Issue #469-swingx: JXHeader doesn't wrap words in description.<p>
     * Issue #634-swingx: JXHeader uses incorrect font for description.<p>
     * 
     * All values are passed in the constructor.
     */
    public void interactiveWordWrapping() {
        URL url = getClass().getResource("resources/images/wellTop.gif");
        assertNotNull(url);
        JPanel p = new JPanel(new BorderLayout());
        JXHeader header = new JXHeader("MyTitle", "this is a long test with veeeeeeeeeeeeeery looooooong wooooooooooooords", new ImageIcon(url));
        p.add(header);
        p.setPreferredSize(new Dimension(200,150));
        showInFrame(p, "word wrapping JXHeader");
    }
    
    /**
     * Test custom title font and color use.
     * 
     * All values are passed in the constructor.
     */
    public void interactiveCustomTitle() {
        URL url = getClass().getResource("resources/images/wellTop.gif");
        assertNotNull(url);
        JPanel p = new JPanel(new BorderLayout());
        JXHeader header = new JXHeader("MyBigUglyTitle", "this is a long test with veeeeeeeeeeeeeery looooooong wooooooooooooords", new ImageIcon(url));
        header.setTitleFont(new Font("serif", Font.BOLD, 36));
        header.setTitleForeground(Color.GREEN);
        p.add(header);
        p.setPreferredSize(new Dimension(400,150));
        showInFrame(p, "word wrapping JXHeader");
    }
    
    public static void main(String args[]) {
        JXHeaderVisualCheck test = new JXHeaderVisualCheck();
        try {
          test.runInteractiveTests();
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        } 
    }

    @Override
    protected void setUp() throws Exception {
        setSystemLF(true);
    }
    
    
}
