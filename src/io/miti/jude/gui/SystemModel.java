/**
 * @(#)SystemModel.java
 * 
 * Created on Dec 30, 2007
 */

package io.miti.jude.gui;

import io.miti.jude.model.SysProps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for showing the system properties.
 * 
 * @author mwallace
 * @version 1.0 */
public final class SystemModel extends AbstractTableModel
{
  /**
   * The default serial version ID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The names of the columns.
   */
  private static final String[] columnNames = {"Key", "Value"};
  
  /**
   * The list of UI defaults.
   */
  private List<SysProps> list = new ArrayList<SysProps>(200);
  
  
  /**
   * Default constructor.
   */
  public SystemModel()
  {
    super();
    initialize();
  }
  
  
  /**
   * Returns the number of rows.
   * 
   * @return the number of rows
   */
  @Override
  public int getRowCount()
  {
    return list.size();
  }
  
  
  /**
   * Returns the number of columns.
   * 
   * @return the number of columns
   */
  @Override
  public int getColumnCount()
  {
    return 2;
  }
  
  
  /**
   * Returns the name of the column.
   * 
   * @param col the column to get the name for
   * @return the name of the specified column
   */
  @Override
  public String getColumnName(final int col)
  {
    return columnNames[col];
  }
  
  
  /**
   * Retrieves a value from a row/column.
   * 
   * @param rowIndex the row index
   * @param columnIndex the column index
   * @return the value at the specified row/column
   */
  @Override
  public Object getValueAt(final int rowIndex,
                           final int columnIndex)
  {
    // Get the appropriate object, based on the index
    SysProps sp = list.get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        return sp.getKey();
        
      case 1:
        return sp.getValue();
      
      default:
        return "x";
    }
  }
  
  
  /**
   * Replace certain control characters with a String representation
   * of those characters.
   * 
   * @param value the string to check
   * @return the modified string
   */
  private static String replaceControlCharacters(final String value)
  {
    final int len = value.length();
    StringBuilder sb = new StringBuilder(len);
    
    // Iterate over all characters
    for (int i = 0; i < len; ++i)
    {
      final char ch = value.charAt(i);
      if (Character.isISOControl(ch))
      {
        // Check for certain control characters
        if (ch == '\r')
        {
          sb.append("\\r");
        }
        else if (ch == '\n')
        {
          sb.append("\\n");
        }
        else if (ch == '\t')
        {
          sb.append("\\t");
        }
        else if (ch == '\f')
        {
          sb.append("\\f");
        }
        else if (ch == '\b')
        {
          sb.append("\\b");
        }
        else
        {
          sb.append(ch);
        }
      }
      else
      {
        // It's not a control character, so just add it
        sb.append(ch);
      }
    }
    
    // Return the string
    return sb.toString();
  }
  
  
  /**
   * Initialize the table with all UI defaults.
   */
  private void initialize()
  {
    // Iterate over all of the system properties
    Properties props = System.getProperties();
    Set<Entry<Object, Object>> entrySet = props.entrySet();
    for (Entry<Object, Object> entry : entrySet)
    {
      // Print the key and value (both are Strings)
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();
      String val = replaceControlCharacters(value);
      list.add(new SysProps(key, val));
    }
    
    // Sort the list
    Collections.sort(list);
  }
}
