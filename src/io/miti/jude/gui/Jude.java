/**
 * @(#)Jude.java
 * 
 * Created on Dec 27, 2007
 */

package io.miti.jude.gui;

import io.miti.jude.util.ThemeLoader;
import io.miti.jude.util.Utility;
import io.miti.jude.util.WindowState;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * This is the main class for the application.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Jude
{
  /**
   * The one instance of this class.
   */
  private static final Jude app = new Jude();
  
  /**
   * The name of the properties file.
   */
  public static final String PROPS_FILE_NAME = "jude.prop";
  
  /**
   * The application frame.
   */
  private JFrame frame = null;
  
  /**
   * The window state (position and size).
   */
  private WindowState windowState = null;
  
  
  /**
   * Default constructor.
   */
  private Jude()
  {
    super();
  }
  
  
  /**
   * Create the application's GUI.
   */
  private void createGUI()
  {
    // Load the properties file
    windowState = WindowState.getInstance();
    
    // Set up the frame
    setupFrame();
    
    // Create the empty middle window
    initScreen();
    
    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }
  
  
  /**
   * Set up the application frame.
   */
  private void setupFrame()
  {
    // Create and set up the window.
    frame = new JFrame(Utility.getAppName() +
                       " - Java User-Interface Defaults Explorer");
    
    // Have the frame call exitApp() whenever it closes
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter()
    {
      /**
       * Close the windows.
       * 
       * @param e the event
       */
      @Override
      public void windowClosing(final WindowEvent e)
      {
        exitApp();
      }
    });
    
    // Set up the size of the frame
    frame.setPreferredSize(windowState.getSize());
    frame.setSize(windowState.getSize());
    
    // Set the position
    if (windowState.shouldCenter())
    {
      frame.setLocationRelativeTo(null);
    }
    else
    {
      frame.setLocation(windowState.getPosition());
    }
  }
  
  
  /**
   * Initialize the main screen (middle window).
   */
  @SuppressWarnings("unused")
  private void initScreen()
  {
    // Add the main panel (with the table) to the frame's center.
    // Leave this in a boolean for now in case we want to revert the
    // behavior to the previous version of only showing the UI Defaults
    // page, instead of a tabbed pane with two pages.
    if (false)
    {
      // Only show the UI Defaults
      frame.getContentPane().add(new DefaultsPanel());
    }
    else
    {
      // Show a tabbed pane with the UI Defaults and System Properties
      JTabbedPane tp = new JTabbedPane();
      tp.addTab("UI Defaults", new DefaultsPanel());
      tp.addTab("System", new SystemPanel());
      frame.getContentPane().add(tp);
    }
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static Jude getInstance()
  {
    return app;
  }
  
  
  /**
   * Refresh the screen (helpful after changing the look and feel).
   */
  public void refreshScreen()
  {
    SwingUtilities.updateComponentTreeUI(frame);
    frame.pack();
  }
  
  
  /**
   * Exit the application.
   */
  private void exitApp()
  {
    // Store the window state in the properties file
    windowState.update(frame.getBounds());
    windowState.saveToFile(PROPS_FILE_NAME);
    
    // Close the application by disposing of the frame
    frame.dispose();
  }
  
  
  /**
   * Entry point to the application.
   * 
   * @param args arguments passed to the application
   */
  public static void main(final String[] args)
  {
    // Make the application Mac-compatible
    Utility.makeMacCompatible();
    
    // Load the properties file data
    WindowState.load(PROPS_FILE_NAME);
    
    // Initialize the look and feel to the default for this OS
    Utility.initLookAndFeel();
    
    // Load the themes
    ThemeLoader.getInstance().loadThemes();
    
    // Check the version number
    if (!Utility.hasRequiredJVMVersion())
    {
      System.exit(0);
    }
    
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // Run the application
        app.createGUI();
      }
    });
  }
}
