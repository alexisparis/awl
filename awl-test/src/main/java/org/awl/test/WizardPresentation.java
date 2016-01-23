package org.awl.test;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.awl.DefaultWizard;
import org.awl.test.test1.BookWizard;
import org.awl.test.test2.UpdateWizard;
import org.awl.test.test4.SinglePageWizard;
import org.awl.test.test5.SimpleWizard;
import org.awl.test.test6.InternationalizationWizard;
import org.awl.test.test7.WizardPositioned;
import org.awl.test.test8.WizardAuthorization;
import org.awl.test.test9.PageDescChangeWizard;
import org.awl.test.testA.DynamicComponentWizard;
import org.awl.test.testB.DynamicPathWizard;
import org.awl.test.testC.BookWizard2;
import org.awl.test.testD.WizardWithHeader;
import org.awl.test.testE.WizardWithMessage;
import org.awl.test.testG.EclipseWizard;

/**
 *
 * @author alexis
 */
public class WizardPresentation
{
    
    /** Creates a new instance of WizardPresentation */
    public WizardPresentation()
    {
    }
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* add content */
	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(layout);
        
	gbc.gridy = 1;
	
	gbc.gridx = 1;
        panel.add(new JLabel("open a book"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
        panel.add(new WizardLauncher(BookWizard.class, frame, "Simple test case").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("open a book with only one page"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
        panel.add(new WizardLauncher(SinglePageWizard.class, frame, "with only one page").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("update something"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
        panel.add(new WizardLauncher(UpdateWizard.class, frame, "Second simple test case").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("open another ugly book with customs buttons' label"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
        panel.add(new WizardLauncher(SimpleWizard.class, frame, "customs buttons").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("internationalized wizard"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(InternationalizationWizard.class, frame, "Changing locale will change text buttons").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard position test"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(WizardPositioned.class, frame, "test wizard position").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard nav authorization"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(WizardAuthorization.class, frame, "test navigation authorization. change authorization and check.").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard mutable page"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(PageDescChangeWizard.class, frame, "change dynamicaly the title and description of the page and check taht the summary is updated").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard with dynamic page component"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(DynamicComponentWizard.class, frame, "When a page is displayed, a timer is launched and 1 second after, the component of the page changed").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard with dynamic path"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(DynamicPathWizard.class, frame, "Choose what will be the next page").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("simple wizard using addPage Wizard's method"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(BookWizard2.class, frame, "Test Wizard' addPage method").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard with header"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(WizardWithHeader.class, frame, null).getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("wizard with messages"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(WizardWithMessage.class, frame, "test messages").getButton(), gbc);
	
	gbc.gridy += 2;
	
	gbc.gridx = 1;
        panel.add(new JLabel("eclipse wizard"), gbc);
	gbc.gridx = 2;
        panel.add(Box.createHorizontalStrut(2), gbc);
	gbc.gridx = 3;
	panel.add(new WizardLauncher(EclipseWizard.class, frame, null).getButton(), gbc);
	
        frame.getContentPane().add(panel);
	frame.setTitle("Awl presentation");
        
        frame.pack();
        
        frame.setVisible(true);
    }
    
}