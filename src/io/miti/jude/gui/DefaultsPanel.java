/**
 * @(#)DefaultsPanel.java
 * 
 * Created on Dec 30, 2007
 */

package io.miti.jude.gui;

import io.miti.jude.model.Defaults;
import io.miti.jude.util.Exporter;
import io.miti.jude.util.LAFAdmin;
import io.miti.jude.util.Utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * The panel for showing UI defaults.
 * 
 * @author Mike
 * @version 1.0
 */
public final class DefaultsPanel extends JPanel
{
  /**
   * Default serial version ID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The Filter text field.
   */
  private JTextField tfFilter = new JTextField(20);
  
  /**
   * The table.
   */
  private JTable table = null;
  
  /**
   * The table model.
   */
  private DefaultsModel model = new DefaultsModel();
  
  /**
   * The pop-up menu.
   */
  private JPopupMenu menu = new JPopupMenu();
  
  /**
   * The Look & Feel popup menu.
   */
  private JPopupMenu lafMenu = new JPopupMenu();
  
  /**
   * The last selected index from the Filters combo box.
   */
  private int selectedFilterIndex = 0;
  
  
  /**
   * Default constructor.
   */
  public DefaultsPanel()
  {
    super();
    
    // Set the layout
    setLayout(new BorderLayout());
    
    // Build the pop-up menu
    buildPopup();
    buildLAFMenu();
    
    // Create the top panel and add an About button
    JPanel top = new JPanel(new BorderLayout());
    top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    addAboutButton(top);
    
    // Create a panel for the left-side of the top panel,
    // and add some fields to it
    JPanel topLeft = new JPanel();
    addLAFButton(topLeft);
    addFilterCombo(topLeft);
    
    // Add the filter label and text field
    JLabel lblFilter = new JLabel("Filter:");
    lblFilter.setDisplayedMnemonic(KeyEvent.VK_F);
    topLeft.add(lblFilter);
    buildFilterListener();
    lblFilter.setLabelFor(tfFilter);
    topLeft.add(tfFilter);
    
    // Add a Reset button, to clear the filter string
    addResetButton(topLeft);
    
    // Add the top-left panel to top, and add top to the frame
    top.add(topLeft, BorderLayout.WEST);
    add(top, BorderLayout.NORTH);
    
    // Build the middle panel (only contains the table)
    buildTable();
    // appPanel = new JPanel(new BorderLayout());
    setBackground(Color.WHITE);
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  
  
  /**
   * Build the look and feel menu.
   */
  private void buildLAFMenu()
  {
    // Build the menu by iterating over the installed look and feels
    final ButtonGroup group = new ButtonGroup();
    String[] names = LAFAdmin.getNames();
    final int selected = LAFAdmin.getDefaultIndex();
    for (int i = 0; i < names.length; ++i)
    {
      // Name the menu item with this L&F
      final JRadioButtonMenuItem miLook = new JRadioButtonMenuItem(names[i]);
      
      // If it's selected, mark it so
      if (i == selected)
      {
        miLook.setSelected(true);
      }
      
      // Create the listener; if the user selects this menu item, select
      // the L&F
      final int index = i;
      miLook.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(final ActionEvent e)
        {
          miLook.setSelected(true);
          LAFAdmin.setByIndex(index);
          Jude.getInstance().refreshScreen();
          handleSelectionChange(selectedFilterIndex);
        }
      });
      
      // Add the menu item to the button group and the menu
      group.add(miLook);
      lafMenu.add(miLook);
    }
  }
  
  
  /**
   * Add a button to set the look and feel.
   * 
   * @param panel the parent panel
   */
  private void addLAFButton(final JPanel panel)
  {
    // Declare the action for the button
    Action action = new AbstractAction("L&F")
    {
      /**
       * The serial ID.
       */
      private static final long serialVersionUID = 1L;
      
      /**
       * Handle a button click.
       * 
       * @param evt the event
       */
      @Override
      public void actionPerformed(final ActionEvent evt)
      {
        // This method is called when the button is pressed
        
        // Show the menu, unless it's already visible, then hide it
        if (lafMenu.isVisible())
        {
          lafMenu.setVisible(false);
        }
        else
        {
          JComponent c = (JComponent) evt.getSource();
          lafMenu.show(c, 0, 0 + c.getHeight());
        }
      }
    };    
    
    // Create the button and set the mnemonic
    final JButton btnLAF = new JButton(action);
    btnLAF.setMnemonic(KeyEvent.VK_L);
    
    // Add to the panel
    panel.add(btnLAF);
    panel.add(javax.swing.Box.createHorizontalStrut(6));
  }
  
  
  /**
   * Build the pop-up menu.
   */
  private void buildPopup()
  {
    // Add a menu item for copying the key to the clipboard
    JMenuItem miKCopy = new JMenuItem("Copy key to clipboard");
    miKCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyCellValue(0);
      }
    });
    menu.add(miKCopy);
    
    // Add a menu item for copying the value (String) to the clipboard
    JMenuItem miVCopy = new JMenuItem("Copy value (String) to clipboard");
    miVCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyCellValue(2);
      }
    });
    menu.add(miVCopy);
    
    // Add a menu item for copying the value (String) to the clipboard
    JMenuItem miVJCopy = new JMenuItem("Copy value (JSON) to clipboard");
    miVJCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        // Get the row's index from the menu's name (hack alert!)
        int row = Utility.getStringAsInteger(menu.getName(), -1, -1);
        if (row >= 0)
        {
          Defaults def = model.getDefaultsAt(row);
          String str = Exporter.export(def.getValueAsObject(), def.getKey());
          StringSelection ss = new StringSelection(str);
          java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
              .setContents(ss, null);
        }
      }
    });
    menu.add(miVJCopy);
    
    // Add a menu item for copying the row (key+value) to the clipboard
    JMenuItem miRowCopy = new JMenuItem("Copy row to clipboard");
    miRowCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyCellValue(-1);
      }
    });
    menu.add(miRowCopy);
    
    // Add a menu item for copying all rows (key+value) to the clipboard
    JMenuItem miRowsCopy = new JMenuItem("Copy all rows to clipboard");
    miRowsCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyCellValue(-2);
      }
    });
    menu.add(miRowsCopy);
  }
  
  
  /**
   * Copy the String in the specified column to the clipboard.
   * The row is pulled from the menu's 'name' field.
   * 
   * @param column the column
   */
  private void copyCellValue(final int column)
  {
    if (column == -2)
    {
      int size = model.getRowCount();
      StringBuilder sb = new StringBuilder(500);
      for (int i = 0; i < size; ++i)
      {
        Defaults def = model.getDefaultsAt(i);
        sb.append(def.getKey()).append(",").append(def.getValue())
          .append(Utility.getLineSeparator());
      }
      copyStringToClipboard(sb.toString());
    }
    else
    {
      // Get the row's index from the menu's name (hack alert!)
      int row = Utility.getStringAsInteger(menu.getName(), -1, -1);
      if (row >= 0)
      {
        if (column >= 0)
        {
          // Copy just this column
          copyStringToClipboard((String) model.getValueAt(row, column));
        }
        else if (column == -1)
        {
          // Copy the whole row (key+value)
          String name = ((String) model.getValueAt(row, 0)) + "," +
                        ((String) model.getValueAt(row, 2));
          copyStringToClipboard(name);
        }
      }
    }
  }
  
  
  /**
   * Copy a string to the clipboard.
   * 
   * @param value the string to copy
   */
  private static void copyStringToClipboard(final String value)
  {
    StringSelection ss = new StringSelection(value);
    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
        .setContents(ss, null);
  }
  
  
  /**
   * Add an About button to the top panel.
   * 
   * @param top the parent panel
   */
  private void addAboutButton(final JPanel top)
  {
    JButton btnAbout = new JButton("About");
    btnAbout.setMnemonic(KeyEvent.VK_A);
    btnAbout.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        showAbout();
      }
    });
    
    // Add the button to a panel so that it looks the same as the controls
    // in the left-side of the top panel
    JPanel p = new JPanel();
    p.add(btnAbout);
    top.add(p, BorderLayout.EAST);
  }
  
  
  /**
   * Add a Reset button to the top panel.
   * 
   * @param topLeft the parent panel
   */
  private void addResetButton(final JPanel topLeft)
  {
    JButton btnReset = new JButton("Reset");
    btnReset.setMnemonic(KeyEvent.VK_R);
    btnReset.addActionListener(new ActionListener()
    {
      /**
       * Handle the button getting pressed.
       * 
       * @param e the event
       */
      public void actionPerformed(final ActionEvent e)
      {
        // Clear the filter text field, the model's filter and show
        // the first row of the table's data
        tfFilter.setText("");
        model.setFilter("");
        showTableTop();
        tfFilter.requestFocusInWindow();
      }
    });
    topLeft.add(btnReset);
  }
  
  
  /**
   * Build the table.
   */
  private void buildTable()
  {
    model.initialize();
    table = new JTable(model)
    {
      /**
       * Default serial version ID.
       */
      private static final long serialVersionUID = 1L;
      
      /**
       * Prepare the renderer.
       * 
       * @param renderer the renderer
       * @param rowIndex the row
       * @param vColIndex the column
       * @return the component for the row and column
       */
      @Override
      public Component prepareRenderer(final TableCellRenderer renderer,
                                       final int rowIndex,
                                       final int vColIndex)
      {
        // Call the parent's implementation
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
        
        // Check if the row is not selected and this is the third column
        boolean colorSet = false;
        boolean sel = isCellSelected(rowIndex, vColIndex);
        if ((vColIndex == 2) && (!sel))
        {
          // Get the Defaults object for this row, and then check
          // if its value is a Color
          Defaults def = model.getDefaultsAt(rowIndex);
          Object val = def.getValueAsObject();
          if ((val != null) && (val instanceof Color))
          {
            // It is a color, so set the background color to match
            c.setBackground((Color) val);
            colorSet = true;
          }
        }
        
        // If we didn't set the color above, set it to the default
        // here, in case the previous cell changed the background color
        if (!colorSet && !sel)
        {
          // If not shaded, match the table's background
          c.setBackground(getBackground());
        }
        
        // Return the component
        return c;
      }
    };
    table.getTableHeader().setReorderingAllowed(false);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Center the column headings
    ((DefaultTableCellRenderer) table.getTableHeader().
        getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Add the pop-up menu to the table
    addTableMouseListener(table, menu);
  }
  
  
  /**
   * Add the listener for right-clicking on a table.
   * 
   * @param table the table
   * @param menu the menu to show
   */
  private static void addTableMouseListener(final JTable table, final JPopupMenu menu)
  {
    // Add the table listener
    table.addMouseListener(new MouseAdapter()
    {
      /**
       * Handle the user right-clicking on the table
       * 
       * @param e the event
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(final MouseEvent e)
      {
        // Check for a right-click on a row
        if (e.getButton() == MouseEvent.BUTTON3)
        {
          // Determine the row
          int row = table.rowAtPoint(new Point(e.getX(), e.getY()));
          if (row >= 0)
          {
            // Save the row and show the popup menu
            menu.setName(Integer.toString(row));
            menu.show(e.getComponent(), e.getX(), e.getY());
          }
        }
      }
    });
  }
  
  
  /**
   * Build the key listener for the filter text field.
   */
  private void buildFilterListener()
  {
    tfFilter.addKeyListener(new KeyAdapter()
    {
      /**
       * Handle the user entering a filter.
       * 
       * @param e the key event
       */
      public void keyTyped(final KeyEvent e)
      {
        // Generate the text string
        super.keyTyped(e);
        
        // Save the character, and check if it's a control character
        char ch = e.getKeyChar();
        String str = tfFilter.getText();
        if (!Character.isISOControl(ch))
        {
          // It's not a control character, so figure out where to
          // insert the character into the text field's string
          int caret = tfFilter.getCaretPosition();
          if (caret <= 0)
          {
            str = ch + str;
          }
          else if (caret >= str.length())
          {
            str += ch;
          }
          else
          {
            String str2 = str.substring(0, caret) + ch + str.substring(caret);
            str = str2;
          }
        }
        
        // Update the table model's contents
        model.setFilter(str.toLowerCase());
      }
    });
  }
  
  
  /**
   * Show the top row of the table.
   */
  private void showTableTop()
  {
    // If the table has at least one row, then show the top row
    if (model.getRowCount() > 0)
    {
      Utility.scrollToVisible(table, 0, 0);
    }
  }
  
  
  /**
   * Show the About dialog box.
   */
  private void showAbout()
  {
    StringBuilder sb = new StringBuilder(100);
    sb.append("JUDE: Java User-Interface Defaults Explorer\n");
    sb.append("Written by Mike Wallace, 2007");
    JOptionPane.showMessageDialog(null, sb.toString(),
                  "About " + Utility.getAppName(),
                  JOptionPane.INFORMATION_MESSAGE);
  }
  
  
  /**
   * Add a combo-box to select a filter on the value's class type.
   * 
   * @param topLeft the parent panel
   */
  private void addFilterCombo(final JPanel topLeft)
  {
    // Declare the list of strings for the combo box
    String[] values = new String[] {"Show All",
                                    "Show Strings",
                                    "Show Fonts",
                                    "Show Colors",
                                    "Show Borders",
                                    "Show Insets",
                                    "Show ActionMaps",
                                    "Show Integers",
                                    "Show Icons",
                                    "Show Dimensions",
                                    "Show Booleans",
                                    "Show InputMaps"};
    final JComboBox<String> cbFilter = new JComboBox<String>(values);
    cbFilter.setEditable(false);
    cbFilter.addItemListener(new ItemListener()
    {
      /**
       * Handle an event on the combo box.
       * 
       * @param e the event
       */
      @Override
      public void itemStateChanged(final ItemEvent e)
      {
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
          selectedFilterIndex = ((JComboBox<?>) e.getSource()).getSelectedIndex();
          handleSelectionChange(selectedFilterIndex);
        }
      }
    });
    
    // Add the combo-box and some space to the parent panel
    topLeft.add(cbFilter);
    topLeft.add(javax.swing.Box.createHorizontalStrut(6));
  }
  
  
  /**
   * Handle a change in the selection of the Filters combo box.
   * 
   * @param selIndex the selected index from the Filters combo box
   */
  private void handleSelectionChange(final int selIndex)
  {
    // Get the selected index from the combo box and
    // update the data list in the model
    model.setValueClass(selIndex);
    
    // Re-apply the filter
    model.setFilter(tfFilter.getText());
    
    // Show the top of the table
    showTableTop();
  }
}
