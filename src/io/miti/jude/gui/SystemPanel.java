/**
 * @(#)SystemPanel.java
 * 
 * Created on Dec 30, 2007
 */

package io.miti.jude.gui;

import io.miti.jude.util.Utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel for showing the system settings.
 * 
 * @author Mike
 * @version 1.0
 */
public final class SystemPanel extends JPanel
{
  /**
   * Default serial version ID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The table.
   */
  private JTable table = null;
  
  /**
   * The table model.
   */
  private SystemModel model = new SystemModel();
  
  /**
   * The pop-up menu.
   */
  private JPopupMenu menu = new JPopupMenu();
  
  
  /**
   * Default constructor.
   */
  public SystemPanel()
  {
    super();
    
    // Set the layout for this panel
    setLayout(new BorderLayout());
    
    // Build the pop-up menu
    buildPopup();
    
    // Build the middle panel (only contains the table)
    buildTable();
    setBackground(Color.WHITE);
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  
  
  /**
   * Build the table.
   */
  private void buildTable()
  {
    // model.initialize();
    table = new JTable(model);
    table.getTableHeader().setReorderingAllowed(false);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
    // Center the column headings
    ((DefaultTableCellRenderer) table.getTableHeader().
        getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Add the pop-up menu to the table
    addTableMouseListener(table, menu);
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
    JMenuItem miVCopy = new JMenuItem("Copy value to clipboard");
    miVCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyCellValue(1);
      }
    });
    menu.add(miVCopy);
    
    // Add a menu item for copying the row to the clipboard
    JMenuItem miRCopy = new JMenuItem("Copy row to clipboard");
    miRCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyCellValue(-1);
      }
    });
    menu.add(miRCopy);
    
    // Add a menu item for copying all rows to the clipboard
    JMenuItem miAllCopy = new JMenuItem("Copy all rows to clipboard");
    miAllCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copyAllRows();
      }
    });
    menu.add(miAllCopy);
    
    // Add a menu item for copying selected rows to the clipboard
    JMenuItem miSelCopy = new JMenuItem("Copy selected rows to clipboard");
    miSelCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        copySelectedRows();
      }
    });
    menu.add(miSelCopy);
  }
  
  
  /**
   * Copy selected rows to the clipboard.
   */
  private void copySelectedRows()
  {
    // Build the string of all rows
    final int count = table.getSelectedRowCount();
    if (count < 1)
    {
      // No rows are selected
      return;
    }
    
    // Get the selected rows
    final int[] rows = table.getSelectedRows();
    if ((rows == null) || (rows.length < 1))
    {
      return;
    }
    
    // Copy the selected rows
    StringBuilder sb = new StringBuilder(2000);
    for (int i = 0; i < rows.length; ++i)
    {
      // -1 for the column means get both columns
      sb.append(getRowValue(rows[i], -1))
        .append('\n');
    }
    
    copyToClipboard(sb.toString());
  }
  
  
  /**
   * Copy all rows to the clipboard.
   */
  private void copyAllRows()
  {
    // Build the string of all rows
    final int count = model.getRowCount();
    StringBuilder sb = new StringBuilder(2000);
    for (int i = 0; i < count; ++i)
    {
      // -1 for the column means get both columns
      sb.append(getRowValue(i, -1))
        .append('\n');
    }
    
    copyToClipboard(sb.toString());
  }
  
  
  /**
   * Copy the String in the specified column to the clipboard.
   * The row is pulled from the menu's 'name' field.
   * 
   * @param column the column
   */
  private void copyCellValue(final int column)
  {
    // Get the row's index from the menu's name (hack alert!)
    int row = Utility.getStringAsInteger(menu.getName(), -1, -1);
    if (row >= 0)
    {
      String name = getRowValue(row, column);
      copyToClipboard(name);
    }
  }
  
  
  /**
   * Copy the specified string to the clipboard.
   * 
   * @param str the string to copy
   */
  private void copyToClipboard(final String str)
  {
    StringSelection ss = new StringSelection(str);
    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
        .setContents(ss, null);
  }
  
  
  /**
   * Get the specified row/column string.  If column is negative,
   * it joins the two columns for the row into a single string.
   * 
   * @param row the row index
   * @param column the column index
   * @return the string for the row
   */
  private String getRowValue(final int row, final int column)
  {
    StringBuilder sb = new StringBuilder(100);
    if (column < 0)
    {
      // Append the two columns
      sb.append(model.getValueAt(row, 0).toString())
        .append(": ")
        .append(model.getValueAt(row, 1).toString());
    }
    else
    {
      // Get just the requested column
      sb.append(model.getValueAt(row, column).toString());
    }
    
    return sb.toString();
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
            // Enable/disable a menu item for copying the
            // selected rows to the clipboard
            final int selCount = table.getSelectedRowCount();
            menu.getComponent(4).setEnabled((selCount > 0));
            
            // Save the row and show the popup menu
            menu.setName(Integer.toString(row));
            menu.show(e.getComponent(), e.getX(), e.getY());
          }
        }
      }
    });
  }
}
